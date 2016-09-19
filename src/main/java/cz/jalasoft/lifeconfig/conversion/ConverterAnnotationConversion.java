package cz.jalasoft.lifeconfig.conversion;

import cz.jalasoft.lifeconfig.annotation.Converter;
import cz.jalasoft.lifeconfig.converter.CollectionConverter;
import cz.jalasoft.lifeconfig.converter.ConverterException;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;

/**
 * This conversion looks for the annotation {@link Converter}
 * on a method to perform the conversion by a class defines
 * in the annotation. Otherwise it delegats to another
 * conversion.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-19.
 */
public final class ConverterAnnotationConversion implements Conversion {

    private final Conversion decorated;

    public ConverterAnnotationConversion(Conversion decorated) {
        this.decorated = decorated;
    }

    @Override
    public Optional<Object> convert(Object sourceValue, Method method) throws ConverterException {
        if (!method.isAnnotationPresent(Converter.class)) {
            return decorated.convert(sourceValue, method);
        }

        Converter annotation = method.getAnnotation(Converter.class);
        cz.jalasoft.lifeconfig.converter.Converter<Object, Object> converter = converterFromAnnotation(annotation);

        Class<?> returnType = method.getReturnType();
        if (Collection.class.isAssignableFrom(returnType)) {
            return convertCollection(sourceValue, converter, returnType);
        }

        Object result = converter.convert(sourceValue);
        return Optional.of(result);
    }

    private cz.jalasoft.lifeconfig.converter.Converter<Object,Object> converterFromAnnotation(Converter annotation) {
        Class<? extends cz.jalasoft.lifeconfig.converter.Converter<? extends Object, ? extends Object>> converterType = annotation.value();
        try {
            return (cz.jalasoft.lifeconfig.converter.Converter<Object,Object>) converterType.newInstance();
        } catch (ReflectiveOperationException exc) {
            throw new RuntimeException("Cannot instantiate a converter " + converterType + ": " + exc.getMessage(), exc);
        }
    }

    private Optional<Object> convertCollection(Object sourceValue, cz.jalasoft.lifeconfig.converter.Converter<Object,Object> itemConverter, Class<?> returnType) throws ConverterException {
        if (!(sourceValue instanceof Collection)) {
            throw new RuntimeException("Cannot convert property value of type " + sourceValue.getClass() + " to a collection.");
        }

        CollectionConverter collectionConverter = new CollectionConverter(itemConverter, returnType);

        Object result = collectionConverter.convert((Collection) sourceValue);
        return Optional.of(result);
    }
}
