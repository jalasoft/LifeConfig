package cz.jalasoft.lifeconfig.converterprovider;

import cz.jalasoft.lifeconfig.converter.Converter;

import java.lang.reflect.Method;

/**
 * This simple conversion silly returns "I did not find any converter to process the conversion"
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-19.
 */
public final class ConverterNotFoundThrowingConverterProvider implements ConverterProvider {

    @Override
    public Converter converter(Object sourceValue, Method method) throws ConverterNotFoundException {
        throw new ConverterNotFoundException(sourceValue.getClass(), method.getReturnType().getClass());
    }
}
