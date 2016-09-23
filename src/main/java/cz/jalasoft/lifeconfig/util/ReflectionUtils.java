package cz.jalasoft.lifeconfig.util;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-23.
 */
public final class ReflectionUtils {

    public static boolean isPrimitiveOrWrapper(Class<?> type) {
        if (type.isPrimitive()) {
            return true;
        }

        if (type.isAssignableFrom(Integer.class)) {
            return true;
        }

        if (type.isAssignableFrom(Float.class)) {
            return true;
        }

        if (type.isAssignableFrom(Short.class)) {
            return true;
        }

        if (type.isAssignableFrom(Byte.class)) {
            return true;
        }

        if (type.isAssignableFrom(Long.class)) {
            return true;
        }

        if (type.isAssignableFrom(Float.class)) {
            return true;
        }

        if (type.isAssignableFrom(Double.class)) {
            return true;
        }

        if (type.isAssignableFrom(Boolean.class)) {
            return true;
        }

        return false;
    }

    private ReflectionUtils() {}
}
