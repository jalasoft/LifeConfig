package cz.jalasoft.lifeconfig.converterprovider;

import cz.jalasoft.lifeconfig.converter.Converter;
import cz.jalasoft.lifeconfig.converter.Converters;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Responsibility of this provider is to provide a converter between
 * primitive types and their boxed equivalents.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-19.
 */
public final class PrimitiveAndBoxedTypesConverterProvider implements ConverterProvider {

    private static final Map<Class<?>, Class<?>> equivalentPrimitiveAndWrappers = new HashMap<>();

    static {
        equivalentPrimitiveAndWrappers.put(Byte.class, byte.class);
        equivalentPrimitiveAndWrappers.put(Short.class, short.class);
        equivalentPrimitiveAndWrappers.put(Integer.class, int.class);
        equivalentPrimitiveAndWrappers.put(Long.class, long.class);
        equivalentPrimitiveAndWrappers.put(Float.class, float.class);
        equivalentPrimitiveAndWrappers.put(Double.class, double.class);
        equivalentPrimitiveAndWrappers.put(Boolean.class, boolean.class);
        equivalentPrimitiveAndWrappers.put(byte.class, Byte.class);
        equivalentPrimitiveAndWrappers.put(short.class, Short.class);
        equivalentPrimitiveAndWrappers.put(int.class, Integer.class);
        equivalentPrimitiveAndWrappers.put(long.class, Long.class);
        equivalentPrimitiveAndWrappers.put(float.class, Float.class);
        equivalentPrimitiveAndWrappers.put(double.class, Double.class);
        equivalentPrimitiveAndWrappers.put(boolean.class, Boolean.class);
    }

    //-------------------------------------------------------------------
    //INSTANCE SCOPE
    //-------------------------------------------------------------------

    private final ConverterProvider decorated;

    public PrimitiveAndBoxedTypesConverterProvider(ConverterProvider decorated) {
        this.decorated = decorated;
    }

    @Override
    public Converter converter(Object sourceValue, Method method) throws ConverterNotFoundException {
        Class<?> sourceType = sourceValue.getClass();
        Class<?> targetType = method.getReturnType();

        if (areSourceAndTargetTypeSameWrapperAndPrimite(sourceType, targetType)) {
            return Converters.identity(sourceType);
        }

        return decorated.converter(sourceValue, method);
    }


    private boolean areSourceAndTargetTypeSameWrapperAndPrimite(Class<?> sourceType, Class<?> targetType) {
        if (!equivalentPrimitiveAndWrappers.containsKey(sourceType)) {
            return false;
        }

        Class<?> targetWrapperOrPrimitive = equivalentPrimitiveAndWrappers.get(sourceType);
        return targetType.equals(targetWrapperOrPrimitive);
    }
}
