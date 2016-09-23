package cz.jalasoft.lifeconfig.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation can be associated with only a type.
 * The purpose is to specify that all methods on the type
 * specifying a part or whole key (via annotation) that
 * leads to a value must be prefixed by a string defined
 * in this annotation.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-07-31.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface KeyPrefix {

    /**
     * prefix of all keys associated with all the method on the type.
     * @return never null or empty.
     */
    String value();
}
