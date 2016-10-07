package cz.jalasoft.lifeconfig.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-19.
 */
public final class ConverterRepository {

    private static List<Converter> stringConverters = new ArrayList<>();
    static {
        stringConverters.add(StringConverters.toByte());
        stringConverters.add(StringConverters.toShort());
        stringConverters.add(StringConverters.toInteger());
        stringConverters.add(StringConverters.toLong());
        stringConverters.add(StringConverters.toFloat());
        stringConverters.add(StringConverters.toDouble());
        stringConverters.add(StringConverters.toBoolean());
        stringConverters.add(StringConverters.toBytePrimitive());
        stringConverters.add(StringConverters.toShortPrimitive());
        stringConverters.add(StringConverters.toIntPrimitive());
        stringConverters.add(StringConverters.toLongPrimitive());
        stringConverters.add(StringConverters.toFloatPrimitive());
        stringConverters.add(StringConverters.toDoublePrimitive());
        stringConverters.add(StringConverters.toBooleanPrimitive());
        stringConverters.add(StringConverters.toISOLocalDateTime());
    }

    private final List<Converter> converters;

    public ConverterRepository() {
        converters = new ArrayList<>();
    }

    public Optional<Converter> fromStringTo(Class<?> targetType) {
        return stringConverters.stream()
                .filter(c -> c.targetType().isAssignableFrom(targetType))
                .findFirst();
    }

    public void registerConverter(Converter converter) {
        converters.add(converter);
    }

    public Optional<Converter> converter(Class<?> sourceType, Class<?> targetType) {
        return converters.stream()
                .filter(c -> c.sourceType().isAssignableFrom(sourceType))
                .filter(c -> c.targetType().isAssignableFrom(targetType))
                .findFirst();
    }
}
