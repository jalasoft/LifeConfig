package cz.jalasoft.lifeconfig.converterprovider;

import cz.jalasoft.lifeconfig.converter.Converter;
import cz.jalasoft.lifeconfig.converter.Converters;

import java.lang.reflect.Method;

/**
 * This provider checks whether the source and
 * the target types are assignable, if so, then
 * a converter is provided that returns the source
 * value. Othrwise it delegates to another provider.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-19.
 */
public final class SameTypesConverterProvider implements ConverterProvider {

    private final ConverterProvider decorated;

    public SameTypesConverterProvider(ConverterProvider decorated) {
        this.decorated = decorated;
    }

    @Override
    public Converter converter(Object sourceValue, Method method) throws ConverterNotFoundException {
        Class<?> sourceType = sourceValue.getClass();
        Class<?> targetType = method.getReturnType();

        if (targetType.isAssignableFrom(sourceType)) {
            return Converters.identity(sourceType);
        }

        return decorated.converter(sourceValue, method);
    }
}
