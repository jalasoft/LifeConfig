package cz.jalasoft.lifeconfig.format;

import cz.jalasoft.lifeconfig.source.ConfigSource;

import java.io.IOException;
import java.io.Reader;
import java.util.Properties;
import java.util.Set;

import static cz.jalasoft.lifeconfig.format.PropertyValue.*;

/**
 * An encapsulation of java property format.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-07-27.
 */
public final class JavaPropertyFormat implements ConfigFormat {

    private static final String DEFAULT_LIST_ITEM_SEPARATOR = "\\,";

    private final String separatorRegex;
    private Properties properties;

    public JavaPropertyFormat() {
        this(DEFAULT_LIST_ITEM_SEPARATOR);
    }

    public JavaPropertyFormat(String separatorRegex) {
        this.separatorRegex = separatorRegex;
    }

    @Override
    public void reload(ConfigSource source) throws IOException {
        properties = new Properties();
        try (Reader reader = source.load()) {
            properties.load(reader);
        }
    }

    @Override
    public PropertyValue readProperty(String key) {
        checkConfigLoaded();

        Object value = properties.get(key);

        if (value == null) {
            return notFoundOrIncompletePath(key);
        }

        return found(value);
    }

    private void checkConfigLoaded() {
        if (properties == null) {
            throw new IllegalStateException("Call reload() first before readProperty().");
        }
    }

    private PropertyValue notFoundOrIncompletePath(String keyPrefix) {
        Set<Object> keys = properties.keySet();

        for(Object key : keys) {
            if (key.toString().startsWith(keyPrefix + ".")) {
                return incompletePath();
            }
        }

        return notFound();
    }
}
