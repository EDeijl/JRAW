import com.google.gson.JsonObject;

/**
 * User: Erik
 * Date: 6-6-13
 * Time: 16:07
 * Copyright (c) 2013 Erik Deijl
 */
public class Post extends Thing {


    public Post(Reddit reddit, JsonObject json) {
        super(json);
    }
}
