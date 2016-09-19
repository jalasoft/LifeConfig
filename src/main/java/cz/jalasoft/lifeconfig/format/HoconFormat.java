package cz.jalasoft.lifeconfig.format;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import cz.jalasoft.lifeconfig.source.ConfigSource;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import static cz.jalasoft.lifeconfig.format.PropertyValue.*;

/**
 * An encapsulation of Hocon configuration format.
 *
 * @see {@link "https://github.com/typesafehub/config/blob/master/HOCON.md"}
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-07-27.
 */
public final class HoconFormat implements ConfigFormat {

    private Config config;

    public HoconFormat() {
        checkTypeSafeConfingOnClasspath();
    }

    private void checkTypeSafeConfingOnClasspath() {
        try {
            Class.forName("com.typesafe.config.Config");
        } catch (ClassNotFoundException exc) {
            throw new IllegalStateException("Cannot load TypeSafeConfig library as a source of hocon configuration infrastructure.");
        }
    }

    @Override
    public void reload(ConfigSource source) throws IOException {
        try (Reader input = source.load()) {
            config = ConfigFactory.parseReader(input);
        }
    }

    @Override
    public PropertyValue readProperty(String key) {
        checkConfigLoaded();

        if (!config.hasPath(key)) {
            return notFound();
        }

        Object value = config.getAnyRef(key);

        if (value instanceof Map) {
            return incompletePath();
        }

        return found(value);
    }

    private void checkConfigLoaded() {
        if (config == null) {
            throw new IllegalStateException("First call reload()");
        }
    }
}
