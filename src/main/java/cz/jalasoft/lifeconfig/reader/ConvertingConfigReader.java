package cz.jalasoft.lifeconfig.reader;

import cz.jalasoft.lifeconfig.converterprovider.ConverterNotFoundException;
import cz.jalasoft.lifeconfig.converterprovider.ConverterProvider;
import cz.jalasoft.lifeconfig.converter.Converter;
import cz.jalasoft.lifeconfig.converter.ConverterException;
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
    private final ConverterProvider converterProvider;

    public ConvertingConfigReader(ConfigSource configSource, ConfigFormat configFormat, ConverterProvider conversion) {
        this.configSource = configSource;
        this.configFormat = configFormat;
        this.converterProvider = conversion;
    }

    @Override
    public Optional<Object> readProperty(String key, Method method) throws IOException, ConverterNotFoundException, ConverterException {
        PropertyValue value = configFormat.readProperty(key);

        if (value.isNotFound()) {
            throw new PropertyNotFoundException(key);
        }

        if (value.isIncomplete()) {
            return Optional.empty();
        }

        Converter<Object, Object> converter = converterProvider.converter(value.value(), method);
        Object result = converter.convert(value.value());
        return Optional.of(result);
    }

    @Override
    public void reload() throws IOException {
        configFormat.reload(configSource);
    }
}
