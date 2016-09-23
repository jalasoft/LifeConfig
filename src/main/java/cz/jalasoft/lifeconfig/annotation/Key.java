package cz.jalasoft.lifeconfig.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation that can be associated wiht a method
 * to descibe that for the method, the key that leads to
 * the value the method returns consists of a value of
 * the annotation.
 *
 * Created by Honza Lastovicka on 3.5.15.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Key {

    /**
     * key or part of composite key that leads to a value
     * returned by the method associated with this annotation.
     *
     * @return never null or empty
     */
    String value();
}
