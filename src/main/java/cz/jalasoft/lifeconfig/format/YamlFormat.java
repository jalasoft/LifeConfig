package cz.jalasoft.lifeconfig.format;

import cz.jalasoft.lifeconfig.source.ConfigSource;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cz.jalasoft.lifeconfig.format.PropertyValue.*;

/**
 * An encapsulation of Yaml format.
 *
 * @see <a href="http://yaml.org/">yaml.org</a>
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-07-27.
 */
public final class YamlFormat implements ConfigFormat {

    private Map<String, Object> data;

    public YamlFormat() {
        checkSnakeYamlOnClasspath();
    }

    private void checkSnakeYamlOnClasspath() {
        try {
            Class.forName("org.yaml.snakeyaml.Yaml");
        } catch (ClassNotFoundException exc) {
            throw new IllegalStateException("SnakeYaml is not on classpath.");
        }
    }

    @Override
    public void reload(ConfigSource source) throws IOException {
        try (Reader reader = source.load()) {
            data = (Map<String, Object>) new Yaml().load(reader);
        }
    }

    @Override
    public PropertyValue readProperty(String key) {
        Object value = readObject(key);

        if (value == null) {
            return notFound();
        }

        if (value instanceof Map) {
            return incompletePath();
        }

        return found(value);
    }

    private Object readObject(String key) {
        String[] tokens = decomposeKey(key);
        List<String> tokensList = Arrays.stream(tokens).collect(Collectors.toList());
        try {
            return readObject(tokensList, this.data);
        } catch (UnexpectedYamlObject exc) {
            return null;
        }
    }

    private String[] decomposeKey(String key) {
        String[] tokens = key.split("\\.");
        return tokens;
    }

    private Object readObject(List<String> tokens, Map<String, Object> data) throws UnexpectedYamlObject {
        String token = tokens.remove(0);
        Object value = data.get(token);

        if (tokens.isEmpty()) {
            return value;
        }

        return readObject(tokens, (Map<String, Object>) value);
    }

    private static final class UnexpectedYamlObject extends Exception {

        private final Object value;

        public UnexpectedYamlObject(Object value) {
            this.value = value;
        }

        Object object() {
            return value;
        }
    }
}
