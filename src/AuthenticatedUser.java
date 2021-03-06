import com.google.gson.JsonElement;

/**
 * User: Erik
 * Date: 5-6-13
 * Time: 13:21
 * Copyright (c) 2013 Erik Deijl
 */
public class AuthenticatedUser {
    private final String ModeratorUrl = "/reddits/mine/moderator.json";
    private final String UnreadMessagesUrl = "/message/unread.json?mark=true&limit=25";
    private final String ModQueueUrl = "/r/mod/about/modqueue.json";
    private final String UnmoderatedUrl = "/r/mod/about/unmoderated.json";
    private final String ModMailUrl = "/message/moderators.json";

    private String ModHash;

    public AuthenticatedUser(Reddit reddit, JsonElement json) {

    }

    public String getModHash() {
        return ModHash;
    }

    public void setModHash(String modHash) {
        ModHash = modHash;
    }


    //TODO things Users do
}
