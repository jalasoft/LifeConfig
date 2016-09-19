package cz.jalasoft.lifeconfig.conversion;

import cz.jalasoft.lifeconfig.converter.ConverterException;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * Responsibility of this interface is to transform an object
 * to another type based on metadata contained on a provided
 * method (return type, annotation etc.).
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-15.
 */
public interface Conversion {


    /**
     * Gets a converted object (if it is possible), that can be handed over to the client.
     *
     * @param sourceValue a value to be converted
     * @param method a source of information for conversion
     * @return never null
     *
     * @throws ConversionException if an error occurred during conversion.
     */
    Optional<Object> convert(Object sourceValue, Method method) throws ConverterException;

}
