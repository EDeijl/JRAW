import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class MyExlusionStrategy implements ExclusionStrategy {
    private final Class<?> typeToSkip;

    public MyExlusionStrategy(Class<?> typeToSkip) {
        this.typeToSkip = typeToSkip;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return (clazz == typeToSkip);
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(JsonIgnore.class) != null;
    }


}
