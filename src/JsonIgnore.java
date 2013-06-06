import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * User: Erik
 * Date: 6-6-13
 * Time: 16:44
 * Copyright (c) 2013 Erik Deijl
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface JsonIgnore {
}
