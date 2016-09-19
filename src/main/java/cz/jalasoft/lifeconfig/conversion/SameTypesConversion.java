package cz.jalasoft.lifeconfig.conversion;

import cz.jalasoft.lifeconfig.converter.ConverterException;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * This conversion checks whether source and target
 * types are assignable, if so, source object is
 * returned as the target object.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-19.
 */
public final class SameTypesConversion implements Conversion {

    private final Conversion decorated;

    public SameTypesConversion(Conversion decorated) {
        this.decorated = decorated;
    }

    @Override
    public Optional<Object> convert(Object sourceValue, Method method) throws ConverterException {
        Class<?> sourceType = sourceValue.getClass();
        Class<?> targetType = method.getReturnType();

        if (targetType.isAssignableFrom(sourceType)) {
            return Optional.of(sourceValue);
        }

        return decorated.convert(sourceValue, method);
    }
}
