package cz.jalasoft.lifeconfig.converter;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-10-07.
 */
public abstract class TypesafeConverter<S, T> implements Converter {

    private final Class<S> sourceType;
    private final Class<T> targetType;

    public TypesafeConverter(Class<S> sourceType, Class<T> targetType) {
        this.sourceType = sourceType;
        this.targetType = targetType;
    }

    @Override
    public final Class<S> sourceType() {
        return sourceType;
    }

    @Override
    public final Class<T> targetType() {
        return targetType;
    }

    @Override
    public final Object convert(Object from) throws ConverterException {
        if (!sourceType.isInstance(from)) {
            throw new ConverterException(from, sourceType, targetType, "Object to be converted must be of type " + sourceType + ", actual type is " + from.getClass());
        }

        Object result = convertSafely(sourceType.cast(from));

       // if (!targetType.isInstance(result)) {
       //     throw new ConverterException(from, sourceType, targetType, "Converted object must be of type " + targetType + ", actual type is " + result.getClass());
       // }

        return result;
    }

    protected abstract T convertSafely(S from) throws ConverterException;
}
