import com.google.gson.JsonObject;

/**
 * User: Erik
 * Date: 6-6-13
 * Time: 16:08
 * Copyright (c) 2013 Erik Deijl
 */
public class PrivateMessage extends Thing {


    public PrivateMessage(Reddit reddit, JsonObject json) {
        super(json);
    }
}
