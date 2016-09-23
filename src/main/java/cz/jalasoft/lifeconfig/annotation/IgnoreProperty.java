package cz.jalasoft.lifeconfig.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks a method to be omitted
 * from investigation and usage.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-06.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreProperty {

}
