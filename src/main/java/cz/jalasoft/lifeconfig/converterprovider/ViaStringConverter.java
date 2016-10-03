package cz.jalasoft.lifeconfig.converterprovider;

import cz.jalasoft.lifeconfig.converter.Converter;
import cz.jalasoft.lifeconfig.converter.ConverterException;
import cz.jalasoft.lifeconfig.converter.ConverterRepository;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * This provider solves slight impedance mismatches
 * between source and target types by two steps conversion.
 * At first, property value is converted to string via its
 * toString() method. Then a converter from string is used
 * to try to convert the string representation to the target
 * type.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-19.
 */
public final class ViaStringConverter implements ConverterProvider {

    private final ConverterProvider decorated;
    private final ConverterRepository converterRepository;

    public ViaStringConverter(ConverterProvider decorated, ConverterRepository converterRepository) {
        this.decorated = decorated;
        this.converterRepository = converterRepository;
    }

    @Override
    public Converter converter(Object sourceValue, Method method) throws ConverterNotFoundException {
        Class<?> targetType = method.getReturnType();

        Optional<Converter<String, Object>> maybeConverter = converterRepository.fromStringTo(targetType);
        if (!maybeConverter.isPresent()) {
            return decorated.converter(sourceValue, method);
        }

        Converter<String, Object> converter = maybeConverter.get();


        return new Converter<Object, Object>() {
            @Override
            public Object convert(Object from) throws ConverterException {
                return converter.convert(sourceValue.toString());
            }

            @Override
            public Class<Object> sourceType() {
                return Object.class;
            }

            @Override
            public Class<Object> targetType() {
                return (Class<Object>) targetType;
            }
        };
    }
}
