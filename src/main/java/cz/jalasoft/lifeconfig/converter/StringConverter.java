package cz.jalasoft.lifeconfig.converter;

/**
 * A base class for all converters from String to any type.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-19.
 */
public abstract class StringConverter implements Converter {

    @Override
    public abstract Object convert(Object from) throws ConverterException;

    @Override
    public final Class<String> sourceType() {
        return String.class;
    }

    @Override
    public abstract Class<?> targetType();
}
