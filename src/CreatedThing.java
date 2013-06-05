import com.google.gson.stream.JsonToken;

/**
 * User: Erik
 * Date: 5-6-13
 * Time: 13:59
 * Copyright (c) 2013 Erik Deijl
 */
public class CreatedThing extends Thing {

    private Reddit Reddit;

    public CreatedThing(Reddit reddit, JsonToken jsonToken) {
        super(jsonToken);
        Reddit = reddit;
        //TODO populate this with json data


    }
}
