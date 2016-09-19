package cz.jalasoft.lifeconfig.converter;

/**
 * A base class for all converters from String to any type.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-19.
 */
public abstract class StringConverter<T> implements Converter<String, T> {

    @Override
    public abstract T convert(String from) throws ConverterException;

    @Override
    public final Class<String> sourceType() {
        return String.class;
    }

    @Override
    public abstract Class<T> targetType();
}
