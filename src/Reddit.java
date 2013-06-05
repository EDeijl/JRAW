import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

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

    //TODO all things reddit does
    public AuthenticatedUser Login() {
        return null; //TODO
    }

    public RedditUser GetUser(String name) {
        return null; //TODO
    }

    public AuthenticatedUser GetMe() {
        return null; //TODO
    }

    public SubReddit GetSubreddit(String name) {
        return null; //TODO
    }

    public void ComposePrivateMessage(String subject, String body, String to) {
        //TODO
    }

    private static DateTime lastRequest = new DateTime(Long.MIN_VALUE);

    protected HttpRequestBase CreateRequest(String url, String method, boolean prependDomain) {
        HttpGet httpGet;
        HttpPost httpPost;
        while (EnableRateLimit && (DateTime.now().minusSeconds(2).isBefore(lastRequest))) //Rate Limiting
        {
        }
        lastRequest = DateTime.now();
        HttpClient httpClient = new DefaultHttpClient();

        if (prependDomain) {
            if (method.contains("GET")) {
                httpGet = new HttpGet(String.format("http://%1$s%2$s", RootDomain, url));
            } else if (method.contains("POST")) {
                httpPost = new HttpPost(String.format("http://%1$s%2$s", RootDomain, url));
            }
        } else {
            if (method.contains("GET")) {
                httpGet = new HttpGet(url);
            } else if (method.contains("POST")) {
                httpPost = new HttpPost(url);
            }
        }

        return null; //TODO
    }

    protected HttpGet CreateGet(String url, boolean prependDomain) {
        while (EnableRateLimit && (DateTime.now().minusSeconds(2).isBefore(lastRequest))) //Rate Limiting
        {
        }
        lastRequest = DateTime.now();
        HttpGet httpGet;
        if (prependDomain)
            httpGet = new HttpGet(String.format("http://%1$s%2$s", RootDomain, url));
        else
            httpGet = new HttpGet(url);

        return httpGet;
    }

    protected HttpPost CreatePost(String url, boolean prependDomain) {
        while (EnableRateLimit && (DateTime.now().minusSeconds(2).isBefore(lastRequest))) {

        }
        lastRequest = DateTime.now();
        HttpPost httpPost;
        if (prependDomain)
            httpPost = new HttpPost(String.format("http://%1$s%2$s", RootDomain, url));
        else
            httpPost = new HttpPost(url);
        return httpPost;
    }

    protected String GetResponseString() {
        return null; //TODO
    }

    protected Thing GetThing(String url, boolean prependDomain) {
        return null; //TODO
    }

    protected void WritePostBody() {
        //TODO
    }

    protected static DateTime UnixTimeStampToDate(int unixTimeStamp) {
        //Unix timestamp is seconds past epoch
        LocalDateTime dateTime = new LocalDateTime(1970, 1, 1, 0, 0, 0, 0);
        DateTime dtDateTime = dateTime.plusSeconds(unixTimeStamp).toDateTime();
        return dtDateTime;
    }


}
