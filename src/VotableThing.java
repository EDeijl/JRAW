import com.google.gson.JsonObject;

import java.lang.reflect.InvocationTargetException;

/**
 * User: Erik
 * Date: 5-6-13
 * Time: 13:59
 * Copyright (c) 2013 Erik Deijl
 */
public class VotableThing extends CreatedThing {
    public VotableThing(Reddit reddit, JsonObject json) throws InvocationTargetException, IllegalAccessException {
        super(reddit, json);
    }
    //TODO
}
