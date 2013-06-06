import com.google.gson.JsonObject;

/**
 * User: Erik
 * Date: 5-6-13
 * Time: 13:59
 * Copyright (c) 2013 Erik Deijl
 */
public class CreatedThing extends Thing {

    private Reddit Reddit;

    public CreatedThing(Reddit reddit, JsonObject json) {
        super(json);
        Reddit = reddit;
        //TODO populate this with json data


    }
}
