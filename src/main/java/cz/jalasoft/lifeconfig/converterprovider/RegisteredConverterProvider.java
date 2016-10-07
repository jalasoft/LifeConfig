package cz.jalasoft.lifeconfig.converterprovider;

import cz.jalasoft.lifeconfig.converter.*;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;

/**
 * This provider simply tries to find any converter registered by the client.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-19.
 */
public final class RegisteredConverterProvider implements ConverterProvider {

    private final ConverterProvider decorated;
    private final ConverterRepository converterRepository;

    public RegisteredConverterProvider(ConverterProvider decorator, ConverterRepository converterRepository) {
        this.decorated = decorator;
        this.converterRepository = converterRepository;
    }

    @Override
    public Converter converter(Object sourceValue, Method method) throws ConverterNotFoundException {

        Class<?> returnType = method.getReturnType();
        if (Collection.class.isAssignableFrom(returnType)) {
            return collectionConverter(sourceValue, method);
        }

        return convertScalar(sourceValue, method);
    }

    private Converter convertScalar(Object sourceValue, Method method) throws ConverterNotFoundException {
        Class<?> sourceType = sourceValue.getClass();
        Class<?> targetType = method.getReturnType();

        Optional<Converter> maybeConverter = converterRepository.converter(sourceType, targetType);
        if (!maybeConverter.isPresent()) {
            return decorated.converter(sourceValue, method);
        }

        Converter converter = maybeConverter.get();
        return converter;
    }

    private Converter collectionConverter(Object sourceValue, Method method) throws ConverterNotFoundException {
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
                return converter;
            }

            Optional<Converter> maybeItemConverter = converterRepository.converter(maybeSourceType.get(), maybeTargetType.get());
            if (!maybeItemConverter.isPresent()) {
                return decorated.converter(sourceValue, method);
            }

            CollectionConverter collectionConverter = new CollectionConverter(maybeItemConverter.get(), method.getReturnType());
            return collectionConverter;
        }

        return Converters.identity(sourceType);
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
