package cz.jalasoft.lifeconfig.reader;


import cz.jalasoft.lifeconfig.converterprovider.ConverterNotFoundException;
import cz.jalasoft.lifeconfig.converter.ConverterException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 *  An abstraction of a reader that provides an instance of a class according to
 *  to a return type of a method passed as an argument.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-08-31.
 */
public interface ConfigReader {

    /**
     * Gets an object representing a property in a format according to
     * a return type of the passed method.
     *
     * @param key a key of a resource
     * @param method a method that is being invoked by a proxy
     * @return never null
     * @throws IOException if an error occurred during retrieving a property.
     * @throws ConverterNotFoundException if there is no converter that could convert value
     * read from the source to the desired type described by the provided method return type.
     * @throws ConverterException if an error occurred during converting a value
     */
    Optional<Object> readProperty(String key, Method method) throws IOException, ConverterNotFoundException, ConverterException;

    /**
     * Reads all resources from a resource.
     * @throws IOException if an error occurred during refreshing properties in memory.
     */
    void reload() throws IOException;
}
