import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.InvocationTargetException;

/**
 * User: Erik
 * Date: 5-6-13
 * Time: 14:00
 * Copyright (c) 2013 Erik Deijl
 */
public class Thing {
    private String FullName;
    private String Id;
    private String Kind;
    private String Shortlink;

    public static Thing Parse(Reddit reddit, JsonObject json) throws InvocationTargetException, IllegalAccessException {
        String kind = json.get("kind").getAsString();
        switch (kind) {
            case "t1":
                return new Comment(reddit, json);
            case "t2":
                return new RedditUser(reddit, json);
            case "t3":
                return new Post(reddit, json);
            case "t4":
                return new PrivateMessage(reddit, json);
            case "t5":
                return new Subreddit(reddit, json);
            default:
                return null;
        }

    }

    Thing(JsonObject json) {
        if (json == null) {
            return;
        }
        JsonElement data = json.get("data").getAsJsonArray();
        FullName = data.getAsJsonObject().get("name").getAsString();
        Id = data.getAsJsonObject().get("id").getAsString();
        Kind = data.getAsJsonObject().get("kind").getAsString();

    }


    //region Getters and Setters
    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getKind() {
        return Kind;
    }

    public void setKind(String kind) {
        Kind = kind;
    }

    public String getShortlink() {
        return String.format("http://redd.it/%s", Id);
    }


    //endregion
}


