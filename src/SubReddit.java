import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.DateTime;

import javax.imageio.ImageIO;
import javax.naming.AuthenticationException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

/**
 * User: Erik
 * Date: 5-6-13
 * Time: 15:09
 * Copyright (c) 2013 Erik Deijl
 */
public class Subreddit extends Thing {
    //region Final Fields
    private final String SubredditPostUrl = "/r/%1$s.json";
    private final String SubredditNewUrl = "/r/%1$s/new.json?sort=new";
    private final String SubscribeUrl = "/api/subscribe";
    private final String GetSettingsUrl = "/r/%1$s/about/edit.json";
    private final String GetReducedSettingsUrl = "/r/%1$s/about.json";
    private final String ModqueueUrl = "/r/%1$s/about/modqueue.json";
    private final String UnmoderatedUrl = "/r/%1$s/about/unmoderated.json";
    private final String FlairTemplateUrl = "/api/flairtemplate";
    private final String ClearFlairTemplatesUrl = "/api/clearflairtemplates";
    private final String SetUserFlairUrl = "/api/flair";
    private final String StylesheetUrl = "/r/%1$s/about/stylesheet.json";
    private final String UploadImageUrl = "/api/upload_sr_img";
    private final String FlairSelectorUrl = "/api/flairselector";
    private final String AcceptModeratorInviteUrl = "/api/accept_moderator_invite";
    private final String LeaveModerationUrl = "/api/unfriend";
    private final String FrontPageUrl = "/.json";
    private final String SubmitLinkUrl = "/api/submit";
    //endregion

    //region Fields
    @JsonIgnore
    private Reddit Reddit;
    @SerializedName("created")
    private DateTime Created;
    @SerializedName("description")
    private String Description;
    @SerializedName("description_html")
    private String DescriptionHTML;
    @SerializedName("display_name")
    private String DisplayName;
    @SerializedName(("header_img"))
    private String HeaderImage;
    @SerializedName("header_title")
    private String HeaderTitle;
    @SerializedName("over18")
    private Boolean NSFW;
    @SerializedName("public_description")
    private String PublicDescription;
    @SerializedName("subscribers")
    private Integer Subscribers;
    @SerializedName("accounts_active")
    private Integer ActiveUsers;
    @SerializedName("title")
    private String Title;
    @SerializedName("url")
    private String Url;
    @JsonIgnore
    private String Name;

    //endregion
    public Subreddit() {
        super(null);
    }

    /**
     * Subreddit Constructor.
     *
     * @param reddit current reddit
     * @param json   json object with data
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    protected Subreddit(Reddit reddit, JsonObject json) throws InvocationTargetException, IllegalAccessException {
        super(json);
        Reddit = reddit;
        Type subredditType = new TypeToken<Subreddit>() {
        }.getType();
        Gson gson = new GsonBuilder().setExclusionStrategies(new MyExlusionStrategy(JsonIgnore.class)).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).serializeNulls().create();
        Subreddit subreddit = gson.fromJson(json, subredditType);
        BeanUtils.copyProperties(this, subreddit);
        Name = Url;
        if (Name.startsWith("/r/"))
            Name = Name.substring(3);
        if (Name.startsWith("r/"))
            Name = Name.substring(2);
        if (Name.endsWith("/"))
            Name = Name.substring(0, Name.length() - 1);
    }

    /**
     * Get the All page of reddit
     *
     * @param reddit current reddit.
     * @return the page as subreddit.
     */
    public Subreddit GetRSlashAll(final Reddit reddit) {
        Subreddit rSlashAll = new Subreddit() {
            {
                DisplayName = "/r/all";
                Title = "/r/all";
                Url = "/r/all";
                Name = "all";
                Reddit = reddit;
            }
        };
        return rSlashAll;
    }

    /**
     * Get the frontpage of reddit
     *
     * @param reddit current reddit
     * @return the page as subreddit.
     */
    public Subreddit GetFrontPage(final Reddit reddit) {
        Subreddit frontPage = new Subreddit() {
            {
                DisplayName = "Front Page";
                Title = "reddit: the front page of the internet";
                Url = "/";
                Name = "/";
                Reddit = reddit;
            }
        };
        return frontPage;
    }

    /**
     * get all posts in current Subreddit
     *
     * @return a Listing with the posts
     */
    public Listing<Post> GetPosts() {
        if (Name == "/")
            return new Listing<Post>(Reddit, "/.json");
        return new Listing<Post>(Reddit, String.format(SubredditPostUrl, Name));
    }

    /**
     * Get new posts in the current subreddit
     *
     * @return a Listing with the posts
     */
    public Listing<Post> GetNew() {
        if (Name == "/")
            return new Listing<Post>(Reddit, "/new.json");
        return new Listing<Post>(Reddit, String.format(SubredditNewUrl, Name));
    }

    /**
     * Get the mod queue
     *
     * @return the modqueue as a listing of votableThings
     */
    public Listing<VotableThing> GetModQueue() {
        return new Listing<VotableThing>(Reddit, String.format(ModqueueUrl, Name));
    }

    /**
     * Get all unmoderated links
     *
     * @return a Listing with all unmoderated links as Posts
     */
    public Listing<Post> GetUnmoderatedLinks() {
        return new Listing<Post>(Reddit, String.format(UnmoderatedUrl, Name));
    }

    /**
     * Subscribe to a subreddit
     *
     * @throws AuthenticationException
     * @throws IOException
     * @throws IllegalAccessException
     */
    public void Subscribe() throws AuthenticationException, IOException, IllegalAccessException {
        if (Reddit.User == null)
            throw new AuthenticationException("No user logged in.");
        HttpUriRequest request = Reddit.CreatePost(SubscribeUrl, true);
        HttpResponse response = Reddit.WritePostBody(request, new Object() {
            String action = "sub";
            String sr = getFullName();
            String uh = Reddit.User.getModHash();
        });
        String data = Reddit.GetResponseString(response);
        //Discard results

    }

    /**
     * Unsubscribe from the subreddit
     *
     * @throws AuthenticationException
     * @throws IOException
     * @throws IllegalAccessException
     */
    public void Unsubscribe() throws AuthenticationException, IOException, IllegalAccessException {
        if (Reddit.User == null)
            throw new AuthenticationException("No user logged in.");
        HttpUriRequest request = Reddit.CreatePost(SubscribeUrl, true);
        HttpResponse response = Reddit.WritePostBody(request, new Object() {
            String action = "unsub";
            String sr = getFullName();
            String uh = Reddit.User.getModHash();
        });
        String data = Reddit.GetResponseString(response);
        //Discard results

    }

    /**
     * Get the settings for a subreddit.
     *
     * @return the settings as a SubredditSettings
     * @throws ClientProtocolException
     * @throws IOException
     */
    public SubredditSettings GetSettings() throws ClientProtocolException, IOException {
        try {
            if (Reddit.User == null)
                throw new AuthenticationException("No user logged in.");
            HttpUriRequest request = Reddit.CreateGet(String.format(GetSettingsUrl, Name), true);
            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(request);
            String data = Reddit.GetResponseString(response);
            JsonObject json = (JsonObject) new JsonParser().parse(data);
            return new SubredditSettings(this, Reddit, json);
        } catch (AuthenticationException e) {
            HttpUriRequest request = Reddit.CreateGet(String.format(GetReducedSettingsUrl, Name), true);
            DefaultHttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(request);
            String data = Reddit.GetResponseString(response);
            JsonObject json = (JsonObject) new JsonParser().parse(data);
            return new SubredditSettings(this, Reddit, json);
        }
    }

    /**
     * Clears the flair templates
     *
     * @param flairType the type of flair. restricted to Link or User
     * @throws IOException
     * @throws IllegalAccessException
     */
    public void ClearFlairTemplates(final FlairType flairType) throws IOException, IllegalAccessException {
        HttpUriRequest request = Reddit.CreatePost(ClearFlairTemplatesUrl, true);
        HttpResponse response = Reddit.WritePostBody(request, new Object() {
            String flair_type = flairType == FlairType.Link ? "LINK_FLAIR" : "USER_FLAIR";
            String uh = Reddit.User.getModHash();
            String r = Name;
        });
        String data = Reddit.GetResponseString(response);
    }

    /**
     * Adds a flair template
     *
     * @param cssClass     the cssClass
     * @param flairType    the type of flair. restricted to Link or User
     * @param textin       the text in the flair
     * @param userEditable user editable true or false/
     * @throws IOException
     * @throws IllegalAccessException
     */
    public void AddFlairTemplate(final String cssClass, final FlairType flairType, final String textin, final boolean userEditable) throws IOException, IllegalAccessException {
        HttpUriRequest request = Reddit.CreatePost(FlairTemplateUrl, true);
        HttpResponse response = Reddit.WritePostBody(request, new Object() {
            String css_class = cssClass;
            String flair_type = flairType == FlairType.Link ? "LINK_FLAIR" : "USER_FLAIR";
            String text = textin;
            boolean text_editable = userEditable;
            String uh = Reddit.User.getModHash();
            String r = Name;
            String api_type = "json";

        });
        String data = Reddit.GetResponseString(response);
        JsonObject json = (JsonObject) new JsonParser().parse(data);
    }

    /**
     * Sets the usersFlair
     *
     * @param user     username to set the flair for
     * @param cssClass the flair's css class
     * @param textIn   the flair's text
     * @throws IOException
     * @throws IllegalAccessException
     */
    public void SetUserFlair(final String user, final String cssClass, final String textIn) throws IOException, IllegalAccessException {
        HttpUriRequest request = Reddit.CreatePost(SetUserFlairUrl, true);
        HttpResponse response = Reddit.WritePostBody(request, new Object() {
            String css_class = cssClass;
            String text = textIn;
            String uh = Reddit.User.getModHash();
            String r = Name;
            String name = user;
        });
        String data = Reddit.GetResponseString(response);

    }

    /**
     * Uploads the header image for a Subreddit
     *
     * @param name      name of image
     * @param imageType type of the image. Restricted to jpg or png
     * @param file      image file as a byte array
     * @throws IOException
     */
    public void UploadHeaderImage(String name, ImageType imageType, byte[] file) throws IOException {
        HttpPost request = (HttpPost) Reddit.CreatePost(UploadImageUrl, true);

        BufferedImage image = ImageIO.read(new ByteArrayInputStream(file));
        File newFile = new File(String.valueOf(image));

        try {
            //convert array of bytes into file
            FileOutputStream fileOutputStream = new FileOutputStream("file");
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        MultipartEntity multipartEntity = new MultipartEntity();

        multipartEntity.addPart("name", new StringBody(name));
        multipartEntity.addPart("uh", new StringBody(Reddit.User.getModHash()));
        multipartEntity.addPart("r", new StringBody(Name));
        multipartEntity.addPart("img_type", new StringBody(imageType == ImageType.PNG ? "png" : "jpg"));
        multipartEntity.addPart("upload", new StringBody(""));
        multipartEntity.addPart("header", new StringBody("1"));
        multipartEntity.addPart("file", new FileBody(newFile));
        request.setEntity(multipartEntity);
        DefaultHttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        String data = Reddit.GetResponseString(response);

        //TODO: Detect Errors
    }

    /**
     * Gets the stylesheet for the subreddit
     *
     * @return a SubredditStyle object
     * @throws IOException
     */
    public SubredditStyle GetStyleSheet() throws IOException {
        HttpUriRequest request = Reddit.CreateGet(String.format(StylesheetUrl, Name), true);
        HttpResponse response = new DefaultHttpClient().execute(request);
        String result = Reddit.GetResponseString(response);
        JsonObject json = (JsonObject) new JsonParser().parse(result);
        return new SubredditStyle(Reddit, this, json);
    }

    /**
     * Accept the moderator invite.
     *
     * @throws IOException
     * @throws IllegalAccessException
     */
    public void AcceptModeratorInvite() throws IOException, IllegalAccessException {
        HttpUriRequest request = Reddit.CreatePost(AcceptModeratorInviteUrl, true);
        HttpResponse response = Reddit.WritePostBody(request, new Object() {
            String api_type = "json";
            String uh = Reddit.User.getModHash();
            String r = Name;
        });
    }

    /**
     * remove an moderator
     *
     * @param idIn id to be removed
     * @throws IOException
     * @throws IllegalAccessException
     */
    public void RemoveModerator(final String idIn) throws IOException, IllegalAccessException {
        HttpUriRequest request = Reddit.CreatePost(LeaveModerationUrl, true);
        HttpResponse response = Reddit.WritePostBody(request, new Object() {
            String api_type = "json";
            String uh = Reddit.User.getModHash();
            String r = Name;
            String type = "moderator";
            String id = idIn;
        });
    }

    @Override
    public String toString() {
        return "/r/" + DisplayName;
    }

    /**
     * Submit a text post
     *
     * @param titleIn title of post
     * @param textIn  text of post
     * @return a Post object
     * @throws Exception
     */
    public Post SubmitTextPost(final String titleIn, final String textIn) throws Exception {
        if (Reddit.User == null)
            throw new Exception("No user logged in.");
        HttpUriRequest request = Reddit.CreatePost(SubmitLinkUrl, true);

        HttpResponse response = Reddit.WritePostBody(request, new Object() {
            String api_type = "json";
            String kind = "self";
            String text = textIn;
            String title = titleIn;
            String uh = Reddit.User.getModHash();
        });
        String result = Reddit.GetResponseString(response);
        JsonObject json = (JsonObject) new JsonParser().parse(result);
        return new Post(Reddit, (JsonObject) json.get("json"));
        //TODO: Error handling
    }

    /**
     * Submit a post with a link
     *
     * @param titleIn title of the post
     * @param urlIn   url of the post
     * @return a post object
     * @throws Exception
     */
    public Post SubmitPost(final String titleIn, final String urlIn) throws Exception {
        if (Reddit.User == null)
            throw new Exception("No user logged in.");
        HttpUriRequest request = Reddit.CreatePost(SubmitLinkUrl, true);
        HttpResponse response = Reddit.WritePostBody(request, new Object() {
            String api_type = "json";
            String extension = "json";
            String kind = "link";
            String sr = Title;
            String title = titleIn;
            String uh = Reddit.User.getModHash();
            String url = urlIn;
        });
        String result = Reddit.GetResponseString(response);
        JsonObject json = (JsonObject) new JsonParser().parse(result);
        return new Post(Reddit, (JsonObject) json.get("json"));
        //TODO: Error Handling
    }


    //region Getters and Setters
    public Reddit getReddit() {
        return Reddit;
    }

    public void setReddit(Reddit reddit) {
        Reddit = reddit;
    }

    public DateTime getCreated() {
        return Created;
    }

    public void setCreated(DateTime created) {
        Created = created;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDescriptionHTML() {
        return DescriptionHTML;
    }

    public void setDescriptionHTML(String descriptionHTML) {
        DescriptionHTML = descriptionHTML;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getHeaderImage() {
        return HeaderImage;
    }

    public void setHeaderImage(String headerImage) {
        HeaderImage = headerImage;
    }

    public String getHeaderTitle() {
        return HeaderTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        HeaderTitle = headerTitle;
    }

    public boolean isNSFW() {
        return NSFW;
    }

    public void setNSFW(boolean NSFW) {
        this.NSFW = NSFW;
    }

    public String getPublicDescription() {
        return PublicDescription;
    }

    public void setPublicDescription(String publicDescription) {
        PublicDescription = publicDescription;
    }

    public int getSubscribers() {
        return Subscribers;
    }

    public void setSubscribers(int subscribers) {
        Subscribers = subscribers;
    }

    public int getActiveUsers() {
        return ActiveUsers;
    }

    public void setActiveUsers(int activeUsers) {
        ActiveUsers = activeUsers;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
    //endregion
}
