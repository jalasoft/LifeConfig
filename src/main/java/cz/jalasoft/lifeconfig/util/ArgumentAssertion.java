package cz.jalasoft.lifeconfig.util;

/**
 * A utility class that allows checking of method parameters
 * and throwing {@link java.lang.IllegalArgumentException} in case
 * that an argument is not valid.
 *
 * Created by Honza Lastovicka on 19.4.15.
 */
public final class ArgumentAssertion {

    /**
     * Throws {@link IllegalArgumentException} if obj is null
     *
     * @param obj tested object
     * @param name a name of the object - used for description of potential exception.
     */
    public static void mustNotBeNull(Object obj, String name) {
        if (obj == null) {
            throw new IllegalArgumentException(name + " must notbe null.");
        }
    }

    /**
     * Throws {@link IllegalArgumentException} if value is null or empty
     *
     * @param value tested string
     * @param name a name or a description of the string, used in case the exception is thrown
     */
    public static void mustNotBeNullOrEmpty(String value, String name) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(name + " must not be null or empty.");
        }
    }

    /**
     * Throws {@link IllegalArgumentException} if boolean value is false.
     *
     * @param value a boolean value to test
     * @param name a name or description of the boolean, used in case the exception is thrown.
     */
    public static void mustBeTrue(boolean value, String name) {
        if (!value) {
            throw new IllegalArgumentException(name + " must be true");
        }
    }

    private ArgumentAssertion() {
        throw new RuntimeException();
    }
}
