package cz.jalasoft.lifeconfig.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A method annotation that contributes to the
 * value retrieval by providing a simpleConverter from
 * string representation to a composite type
 *
 * Created by Honza Lastovicka on 12.5.15.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Converter {

    Class<? extends cz.jalasoft.lifeconfig.converter.Converter> value();
}
