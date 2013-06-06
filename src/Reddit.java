import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import javax.naming.AuthenticationException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

/**
 * User: Erik
 * Date: 5-6-13
 * Time: 13:05
 * Copyright (c) 2013 Erik Deijl
 */
public class Reddit {

    //region Constants
    private final String SslLoginUrl = "https://ssl.reddit.com/api/login";
    private final String LoginUrl = "/api/login/username";
    private final String UserInfoUrl = "/user/%s/about.json";
    private final String MeUrl = "/api/me.json";
    private final String SubredditAboutUrl = "/r/%s/about.json";
    private final String ComposeMessageUrl = "/api/compose";
    //endregion

    //region Static Fields
    static {
        UserAgent = "";
        EnableRateLimit = true;
        RootDomain = "www.reddit.com";
    }

    private static String UserAgent;
    private static boolean EnableRateLimit;
    private static String RootDomain;


    //endregion

    public Reddit() {

    }

    //region Fields
    public AuthenticatedUser User;
    public CookieStore Cookies;
    public String AuthCookie;


    //endregion

    /**
     * Log in to an account.
     *
     * @param username a username
     * @param password the user's password
     * @param useSsl   a boolean yes of no
     * @return AuthenticatedUser returns an AuthenticatedUser Object
     * @throws IOException
     * @throws IllegalAccessException
     * @throws AuthenticationException
     */
    public AuthenticatedUser Login(final String username, final String password, boolean useSsl) throws IOException, IllegalAccessException, AuthenticationException {
        Cookies = new BasicCookieStore();
        HttpUriRequest request;
        HttpResponse response;
        if (useSsl) {
            request = CreatePost(SslLoginUrl, false);
        } else {
            request = CreatePost(LoginUrl, true);
        }
        if (useSsl) {

            response = WritePostBody(request, new Object() {
                String user = username;
                String passwd = password;
                String api_type = "json";
            });

        } else {

            response = WritePostBody(request, new Object() {
                String user = username;
                String passwd = password;
                String api_type = "json";
                String op = "login";
            });

        }
        String result = GetResponseString(response);
        Gson gson = new Gson();
        String data = GetResponseString(response);
        JsonElement jsonElement = new JsonParser().parse(data);
        Type userType = new TypeToken<AuthenticatedUser>() {
        }.getType();
        AuthenticatedUser authenticatedUser = gson.fromJson(jsonElement, userType);
        if (data.contains("errors")) {
            throw new AuthenticationException("Incorret login.");
        }
        GetMe();
        return User;
    }

    /**
     * Get a specific user.
     *
     * @param name a username
     * @return RedditUser
     * @throws IOException
     */
    public RedditUser GetUser(String name) throws IOException {
        HttpUriRequest request = CreateGet(String.format(UserInfoUrl, name), true);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        String result = GetResponseString(response);
        Gson gson = new Gson();
        JsonObject json = (JsonObject) new JsonParser().parse(result);
        return new RedditUser(this, json);
    }

    /**
     * Gets info about the currently authenticated user.
     *
     * @return An user with modhash, karma and new mail status.
     * @throws IOException
     */
    public AuthenticatedUser GetMe() throws IOException {
        HttpUriRequest request = CreateGet(MeUrl, true);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = client.execute(request);
        String result = GetResponseString(response);
        JsonElement json = new JsonParser().parse(result);
        User = new AuthenticatedUser(this, json);
        return User;

    }

    /**
     * Gets a specific subreddit.
     *
     * @param name a subreddit name.
     * @return The requested subreddit.
     * @throws IOException
     */
    public Subreddit GetSubreddit(String name) throws IOException, InvocationTargetException, IllegalAccessException {
        if (name.startsWith("r/")) {
            name = name.substring(2);
        }
        if (name.startsWith("/r/")) {
            name = name.substring(3);
        }
        return (Subreddit) GetThing(String.format(SubredditAboutUrl, name), true);
    }

    /**
     * Handles message composition
     *
     * @param subject message subject
     * @param body    raw markdown text
     * @param to      the name of an existing user
     * @throws IOException
     * @throws IllegalAccessException
     */
    public void ComposePrivateMessage(final String subject, final String body, final String to) throws IOException, IllegalAccessException {
        HttpUriRequest request = CreatePost(ComposeMessageUrl, true);
        HttpResponse response = WritePostBody(request, new Object() {
            String api_type = "json";
            String subject;
            String text = body;
            String to;
            String uh = User.getModHash();
        });
        String result = GetResponseString(response);
        //TODO: Error

    }

    //region Helpers
    private static DateTime lastRequest = new DateTime(Long.MIN_VALUE);

    /**
     * Creates a HttpRequest to the specified endpoint
     *
     * @param url           destination url, this can be with or without rood domain
     * @param method        Http method only "GET" and "POST" are supported
     * @param prependDomain true if http://reddit.com still has to be added to the url, false if not.
     * @return an HttpRequest
     */
    protected HttpUriRequest CreateRequest(String url, String method, boolean prependDomain) {

        HttpUriRequest request = null;


        while (EnableRateLimit && (DateTime.now().minusSeconds(2).isBefore(lastRequest))) //Rate Limiting
        {
        }
        lastRequest = DateTime.now();
        if (method.equalsIgnoreCase("GET")) {
            if (prependDomain) {
                request = new HttpGet(String.format("http://%1$s%2$s", RootDomain, url));
            } else {
                request = new HttpGet(url);
            }
        } else if (method.equalsIgnoreCase("POST")) {
            if (prependDomain) {
                request = new HttpPost(String.format("http://%1$s%2$s", RootDomain, url));
            } else {
                request = new HttpPost(url);
            }
        }
        return request;


    }

    /**
     * Creates a GET request.
     *
     * @param url           the destination endpoint.
     * @param prependDomain true if http://reddit.com still has to be added to the url, false if not.
     * @return
     */
    protected HttpUriRequest CreateGet(String url, boolean prependDomain) {
        return CreateRequest(url, "GET", prependDomain);

    }

    /**
     * Creates a POST request.
     *
     * @param url           the destination endpoint.
     * @param prependDomain true if http://reddit.com still has to be added to the url, false if not.
     * @return
     */
    protected HttpUriRequest CreatePost(String url, boolean prependDomain) {
        HttpUriRequest request = CreateRequest(url, "POST", prependDomain);
        request.addHeader(new BasicHeader("Content-Type", "application/x-www-form-urlencoded"));
        return request;

    }

    /**
     * Gets the response's body as a string
     *
     * @param response a HttpResponse
     * @return The response body as a string.
     * @throws UnknownHostException
     * @throws IOException
     */
    protected String GetResponseString(HttpResponse response) throws UnknownHostException, IOException {

        return EntityUtils.toString(response.getEntity());
    }

    /**
     * Get a reddit Thing.
     *
     * @param url           the destination endpoint.
     * @param prependDomain true if http://reddit.com still has to be added to the url, false if not.
     * @return
     * @throws IOException
     */
    protected Thing GetThing(String url, boolean prependDomain) throws IOException, InvocationTargetException, IllegalAccessException {
        HttpClient client = new DefaultHttpClient();
        HttpUriRequest request = CreateGet(url, prependDomain);
        HttpResponse response = client.execute(request);
        String data = GetResponseString(response);
        JsonObject jsonElement = (JsonObject) new JsonParser().parse(data);
        return Thing.Parse(this, jsonElement);
    }

    static Hashtable HTML_SPECIALCHARS_TABLE = new Hashtable();

    static {
        HTML_SPECIALCHARS_TABLE.put(";", "%3B");
        HTML_SPECIALCHARS_TABLE.put("&", "%26");
    }

    /**
     * Replaces special characters with their HTML value
     *
     * @param s String which needs replacing.
     * @return The string with replacements.
     */
    static String HtmlSpecialChars_Decode_ENT_NOQUOTES(String s) {
        Enumeration enumeration = HTML_SPECIALCHARS_TABLE.keys();
        while (enumeration.hasMoreElements()) {
            String key = (String) enumeration.nextElement();
            String val = (String) HTML_SPECIALCHARS_TABLE.get(key);
            s = s.replace(key, val);
        }
        return s;
    }

    /**
     * Writes a PostBody
     *
     * @param request the HttpRequest
     * @param data    The object to send.
     * @return A HttpResponse
     * @throws IllegalAccessException
     * @throws IOException
     */
    protected HttpResponse WritePostBody(HttpUriRequest request, Object data) throws IllegalAccessException, IOException {
        Class<?> type = data.getClass();
        String value = "";
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        for (Field field : type.getFields()) {
            String entry = String.valueOf(field.get(data));
            nameValuePairs.add(new BasicNameValuePair(field.getName(), HtmlSpecialChars_Decode_ENT_NOQUOTES(entry)));
        }
        value = value.substring(0, value.length() - 1);
        byte[] raw = value.getBytes("UTF-8");
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = (HttpPost) request;
        HttpResponse response;
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
        try {
            response = httpClient.execute(httpPost);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return response;
    }

    /**
     * Convert unix time to local time.
     *
     * @param unixTimeStamp a unix timestamp
     * @return a Joda DateTime value
     */
    protected static DateTime UnixTimeStampToDate(int unixTimeStamp) {
        //Unix timestamp is seconds past epoch
        LocalDateTime dateTime = new LocalDateTime(1970, 1, 1, 0, 0, 0, 0);
        DateTime dtDateTime = dateTime.plusSeconds(unixTimeStamp).toDateTime();
        return dtDateTime;
    }
    //endregion

    //region Getters and Setters
    public AuthenticatedUser getUser() {
        return User;
    }

    public void setUser(AuthenticatedUser user) {
        User = user;
    }

    public CookieStore getCookies() {
        return Cookies;
    }

    public void setCookies(CookieStore cookies) {
        Cookies = cookies;
    }

    public String getAuthCookie() {
        return AuthCookie;
    }

    public void setAuthCookie(String authCookie) {
        AuthCookie = authCookie;
    }

    public static String getUserAgent() {
        return UserAgent;
    }

    public static void setUserAgent(String userAgent) {
        UserAgent = userAgent;
    }
    //endregion
}
