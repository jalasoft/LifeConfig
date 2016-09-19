package cz.jalasoft.lifeconfig.conversion;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * This simple conversion silly returns "I did not find any converter to process the conversion"
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-19.
 */
public final class EmptyObjectReturningConversion implements Conversion {

    @Override
    public Optional<Object> convert(Object sourceValue, Method method) {
        return Optional.empty();
    }
}
