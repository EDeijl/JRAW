import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.sun.javafx.iio.ImageStorage;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.DateTime;

import javax.naming.AuthenticationException;
import java.io.IOException;
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
    private boolean NSFW;
    @SerializedName("public_description")
    private String PublicDescription;
    @SerializedName("subscribers")
    private int Subscribers;
    @SerializedName("accounts_active")
    private int ActiveUsers;
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

    protected Subreddit(Reddit reddit, JsonObject json) {
        super(json);
        Reddit = reddit;
        Type subredditType = new TypeToken<Subreddit>() {
        }.getType();
        Gson gson = new Gson();
        //TODO somthing converty
        Name = Url;
        if (Name.startsWith("/r/"))
            Name = Name.substring(3);
        if (Name.startsWith("r/"))
            Name = Name.substring(2);
        if (Name.endsWith("/"))
            Name = Name.substring(0, Name.length() - 1);
    }

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

    public Listing<Post> GetPosts() {
        if (Name == "/")
            return new Listing<Post>(Reddit, "/.json");
        return new Listing<Post>(Reddit, String.format(SubredditPostUrl, Name));
    }

    public Listing<Post> GetNew() {
        if (Name == "/")
            return new Listing<Post>(Reddit, "/new.json");
        return new Listing<Post>(Reddit, String.format(SubredditNewUrl, Name));
    }

    public Listing<VotableThing> GetModQueue() {
        return new Listing<VotableThing>(Reddit, String.format(ModqueueUrl, Name));
    }

    public Listing<Post> GetUnmoderatedLinks() {
        return new Listing<Post>(Reddit, String.format(UnmoderatedUrl, Name));
    }

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

    public void ClearFlairTemplates(FlairType flairType) {
        //TODO
    }

    public void AddFlairTemplate(String cssClass, FlairType flairType, String text, boolean userEditable) {
        //TODO
    }

    public void SetUserFlair(String user, String cssClass, String text) {
        //TODO
    }

    public void UploadHeaderImage(String name, ImageStorage.ImageType imageType, byte[] file) {
        //TODO
    }

    public SubredditStyle GetStyleSheet() {
        return null; //TODO
    }

    public void AcceptModeratorInvite() {
        //TODO
    }

    public void RemoveModerator(String id) {
        //TODO
    }

    @Override
    public String toString() {
        return "/r/" + DisplayName;
    }

    public Post SubmitTextPost(String title, String text) {
        return null;//TODO
    }

    public Post SubmitPost(String title, String url) {
        return null;//TODO
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
