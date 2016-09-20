package cz.jalasoft.lifeconfig.keyresolver;

import java.lang.reflect.Method;

/**
 * A responsibility of the key resolver is to
 * provide a string key derived from a provided method.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-08-16.
 */
public interface PropertyKeyResolver {

    /**
     * Gets a key for a configuration format to get a value.
     * @param method never null
     * @return must not be null
     */
    String resolveKey(Method method);
}
