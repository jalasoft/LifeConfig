package cz.jalasoft.lifeconfig.converterprovider;

import cz.jalasoft.lifeconfig.converter.Converter;

import java.lang.reflect.Method;

/**
 * Responsibility of this interface is to provide an object
 * that can convert the a source value to another type based
 * on metadata contained on a provided method (return type, annotation etc.).
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-15.
 */
public interface ConverterProvider {


    /**
     * Gets a converter (if it is possible), that can be handed over to the client.
     *
     * @param sourceValue a value to be converted
     * @param method a source of information for conversion
     * @return never null
     * @throws ConverterNotFoundException if no converter has been found for conversion.
     */
    Converter converter(Object sourceValue, Method method) throws ConverterNotFoundException;

}
