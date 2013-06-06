import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.beanutils.BeanUtils;
import org.joda.time.DateTime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

/**
 * User: Erik
 * Date: 5-6-13
 * Time: 13:59
 * Copyright (c) 2013 Erik Deijl
 */
public class CreatedThing extends Thing {

    private Reddit Reddit;


    @SerializedName("created")
    private DateTime Created;

    /**
     * Constructor for a CreatedThing
     *
     * @param reddit
     * @param json
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public CreatedThing(Reddit reddit, JsonObject json) throws InvocationTargetException, IllegalAccessException {
        super(json);
        Reddit = reddit;
        //TODO populate this with json data
        Type createdThingType = new TypeToken<CreatedThing>() {
        }.getType();
        Gson gson = new GsonBuilder().setExclusionStrategies(new MyExlusionStrategy(JsonIgnore.class)).setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).serializeNulls().create();
        CreatedThing createdThing = gson.fromJson(json, createdThingType);
        BeanUtils.copyProperties(this, createdThing);
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
    //endregion
}
