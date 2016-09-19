package cz.jalasoft.lifeconfig.conversion;

import cz.jalasoft.lifeconfig.converter.Converter;
import cz.jalasoft.lifeconfig.converter.ConverterException;
import cz.jalasoft.lifeconfig.converter.ConverterRepository;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * This conversion solves slight impedance mismatchees
 * between source and target types by two steps conversion.
 * At first, property value is converted to string via its
 * toString() method. Then a converter from string is used
 * to try to convert the string representation to the target
 * type.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-19.
 */
public final class ViaStringConversion implements Conversion {

    private final Conversion decorated;
    private final ConverterRepository converterRepository;

    public ViaStringConversion(Conversion decorated, ConverterRepository converterRepository) {
        this.decorated = decorated;
        this.converterRepository = converterRepository;
    }

    @Override
    public Optional<Object> convert(Object sourceValue, Method method) throws ConverterException {
        Class<?> targetType = method.getReturnType();

        Optional<Converter<String, Object>> maybeConverter = converterRepository.fromStringTo(targetType);
        if (!maybeConverter.isPresent()) {
            Object result = decorated.convert(sourceValue, method);
            return Optional.of(result);
        }

        Converter<String, Object> converter = maybeConverter.get();
        Object result = converter.convert(sourceValue.toString());

        return Optional.of(result);
    }
}
