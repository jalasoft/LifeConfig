package cz.jalasoft.lifeconfig.reader;

import cz.jalasoft.lifeconfig.conversion.Conversion;
import cz.jalasoft.lifeconfig.format.ConfigFormat;
import cz.jalasoft.lifeconfig.format.PropertyValue;
import cz.jalasoft.lifeconfig.source.ConfigSource;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-19.
 */
public final class ConvertingConfigReader implements ConfigReader {

    private final ConfigSource configSource;
    private final ConfigFormat configFormat;
    private final Conversion conversion;

    public ConvertingConfigReader(ConfigSource configSource, ConfigFormat configFormat, Conversion conversion) {
        this.configSource = configSource;
        this.configFormat = configFormat;
        this.conversion = conversion;
    }

    @Override
    public Optional<Object> readProperty(String key, Method method) {
        PropertyValue value = configFormat.readProperty(key);

        if (value.isNotFound()) {
            throw new PropertyNotFoundException(key);
        }

        if (value.isIncomplete()) {
            return Optional.empty();
        }

        try {
            Object targetObject = conversion.convert(value.value(), method);
            return Optional.of(targetObject);
        } catch (ConverterException exc) {
            throw new RuntimeException(exc);
        }
    }

    @Override
    public void reload() throws IOException {
        configFormat.reload(configSource);
    }
}
