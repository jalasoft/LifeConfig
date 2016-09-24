package cz.jalasoft.lifeconfig;

import java.lang.reflect.Method;

/**
 * An exception thrown when a method designated by annotation
 * {@link cz.jalasoft.lifeconfig.annotation.IgnoreProperty} and
 * the method is invoked by the client.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-06.
 */
public final class PropertyIgnoredException extends RuntimeException {

    private final Method method;

    public PropertyIgnoredException(Method method) {
        this.method = method;
    }

    @Override
    public String getMessage() {
        return "Method " + method.getName() + " is designated as ignored. You cannot invoke it to obtain a property.";
    }
}
