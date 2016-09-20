package cz.jalasoft.lifeconfig.reader;

import static cz.jalasoft.lifeconfig.util.ArgumentAssertion.*;
/**
 * A runtime exception that identifies a situation when there
 * is no value for a given resolveKey in a configuration file.
 *
 * @author Honza Lastovicka
 */
public final class PropertyNotFoundException extends RuntimeException {

    private final String key;

    public PropertyNotFoundException(String key) {
        mustNotBeNullOrEmpty(key, "Property resolveKey must not be null or empty");

        this.key = key;
    }

    public PropertyNotFoundException(String key, String reason) {
        mustNotBeNullOrEmpty(key, "Property resolveKey must not be null or empty");
        this.key = key;
    }

    @Override
    public String getMessage() {
        return "No property found for key " + key;
    }
}
