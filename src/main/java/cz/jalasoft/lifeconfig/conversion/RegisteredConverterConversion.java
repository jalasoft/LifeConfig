package cz.jalasoft.lifeconfig.conversion;

import cz.jalasoft.lifeconfig.converter.CollectionConverter;
import cz.jalasoft.lifeconfig.converter.Converter;
import cz.jalasoft.lifeconfig.converter.ConverterException;
import cz.jalasoft.lifeconfig.converter.ConverterRepository;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-19.
 */
public final class RegisteredConverterConversion implements Conversion {

    private final Conversion decorated;
    private final ConverterRepository converterRepository;

    public RegisteredConverterConversion(Conversion decorator, ConverterRepository converterRepository) {
        this.decorated = decorator;
        this.converterRepository = converterRepository;
    }

    @Override
    public Optional<Object> convert(Object sourceValue, Method method) throws ConverterException {

        Class<?> returnType = method.getReturnType();
        if (Collection.class.isAssignableFrom(returnType)) {
            return convertCollection(sourceValue, method);
        }

        return convertScalar(sourceValue, method);
    }

    private Optional<Object> convertScalar(Object sourceValue, Method method) throws ConverterException {
        Class<?> sourceType = sourceValue.getClass();
        Class<?> targetType = method.getReturnType();

        Optional<Converter<Object, Object>> maybeConverter = converterRepository.converter(sourceType, targetType);
        if (!maybeConverter.isPresent()) {
            return decorated.convert(sourceValue, method);
        }

        Converter<Object, Object> converter = maybeConverter.get();

        Object result = converter.convert(sourceValue);
        return Optional.of(result);
    }

    private Optional<Object> convertCollection(Object sourceValue, Method method) throws ConverterException {
        if (!(sourceValue instanceof Collection)) {
            throw new RuntimeException("Cannot convert property value of type " + sourceValue.getClass() + " to a collection.");
        }

        Class<?> sourceType = sourceValue.getClass();

        Optional<Class<?>> maybeTargetType = extractGenericType(method.getGenericReturnType());

        if (maybeTargetType.isPresent()) {
            //try get first item and find out its class
            Optional<Class<?>> maybeSourceType = tryExtractGenericType((Collection) sourceValue);

            if (!maybeSourceType.isPresent()) {
                //the source collection must be empty
                CollectionConverter converter = new CollectionConverter(null, sourceType);
                Object result = converter.convert((Collection)sourceValue);
                return Optional.of(result);
            }

            Optional<Converter<Object, Object>> maybeItemConverter = converterRepository.converter(maybeSourceType.get(), maybeTargetType.get());
            if (!maybeItemConverter.isPresent()) {
                return decorated.convert(sourceValue, method);
            }

            CollectionConverter collectionConverter = new CollectionConverter(maybeItemConverter.get(), method.getReturnType());
            Object result = collectionConverter.convert((Collection) sourceValue);
            return Optional.of(result);
        }

        return Optional.of(sourceValue);
    }

    private Optional<Class<?>> extractGenericType(Type type) {
        //Type type = method.getGenericReturnType();

        if (!(type instanceof ParameterizedType)) {
            return Optional.empty();
        }

        ParameterizedType parameterizedType = (ParameterizedType) type;

        Type[] args = parameterizedType.getActualTypeArguments();
        Type typeObj = args[0];

        try {
            Class<?> genericType = Class.forName(typeObj.getTypeName());
            return Optional.of(genericType);
        } catch (ClassNotFoundException exc) {
            throw new RuntimeException(exc);
        }
    }

    private Optional<Class<?>> tryExtractGenericType(Collection collection) {
        if (collection.isEmpty()) {
            return Optional.empty();
        }

        Object item = collection.iterator().next();
        return Optional.of(item.getClass());
    }
}
