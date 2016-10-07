package cz.jalasoft.lifeconfig.converter;

/**
 * A functional interface that does a conversion from
 * generic type F to generic type T.
 *
 * Created by Honza Lastovicka on 18.4.15.
 */
public interface Converter {

    /**
     * Converts from F to T
     * @param from must not be null
     * @return never null
     * @throws ConverterException if a problem occurred during the conversion
     * @throws java.lang.IllegalArgumentException if from is null
     */
    Object convert(Object from) throws ConverterException;

    /**
     * A type of a source object.
     * @return never null
     */
    Class<?> sourceType();

    /**
     * A type of a target object.
     * @return never null
     */
    Class<?> targetType();
}
