package cz.jalasoft.lifeconfig.converterprovider;

import cz.jalasoft.lifeconfig.annotation.Converter;
import cz.jalasoft.lifeconfig.converter.CollectionConverter;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * This provider looks for the annotation {@link Converter}
 * on a method to provide an instance of a converter by a
 * class defined in the annotation. Otherwise it delegates
 * to another provider.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-19.
 */
public final class ConverterAnnotationProvider implements ConverterProvider {

    private final ConverterProvider decorated;

    public ConverterAnnotationProvider(ConverterProvider decorated) {
        this.decorated = decorated;
    }

    @Override
    public cz.jalasoft.lifeconfig.converter.Converter converter(Object sourceValue, Method method) throws ConverterNotFoundException {
        if (!method.isAnnotationPresent(Converter.class)) {
            return decorated.converter(sourceValue, method);
        }

        Converter annotation = method.getAnnotation(Converter.class);
        cz.jalasoft.lifeconfig.converter.Converter<Object, Object> converter = converterFromAnnotation(annotation);

        Class<?> returnType = method.getReturnType();
        if (Collection.class.isAssignableFrom(returnType)) {
            return collectionConverter(sourceValue, converter, returnType);
        }

        return converter;
    }

    private cz.jalasoft.lifeconfig.converter.Converter<Object,Object> converterFromAnnotation(Converter annotation) {
        Class<? extends cz.jalasoft.lifeconfig.converter.Converter<? extends Object, ? extends Object>> converterType = annotation.value();
        try {
            return (cz.jalasoft.lifeconfig.converter.Converter<Object,Object>) converterType.newInstance();
        } catch (ReflectiveOperationException exc) {
            throw new RuntimeException("Cannot instantiate a converter " + converterType + ": " + exc.getMessage(), exc);
        }
    }

    private cz.jalasoft.lifeconfig.converter.Converter collectionConverter(Object sourceValue, cz.jalasoft.lifeconfig.converter.Converter<Object,Object> itemConverter, Class<?> returnType) {
        if (!(sourceValue instanceof Collection)) {
            throw new RuntimeException("Cannot convert property value of type " + sourceValue.getClass() + " to a collection.");
        }

        CollectionConverter collectionConverter = new CollectionConverter(itemConverter, returnType);
        return collectionConverter;
    }
}
