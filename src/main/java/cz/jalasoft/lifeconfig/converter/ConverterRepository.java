package cz.jalasoft.lifeconfig.converter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-19.
 */
public final class ConverterRepository {

    private static List<Converter<String, ?>> stringConverters = new ArrayList<>();
    static {
        stringConverters.add(StringConverters.identity());
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

    private final List<Converter<Object, Object>> converters;

    public ConverterRepository() {
        converters = new ArrayList<>();
    }

    public Optional<Converter<String, Object>> fromStringTo(Class<?> targetType) {
        return stringConverters.stream()
                .map(c -> (Converter<String, Object>) c)
                .filter(c -> c.targetType().isAssignableFrom(targetType))
                .findFirst();
    }

    public void registerConverter(Converter<?, ?> converter) {
        Converter<Object, Object> deCocotyzed = (Converter<Object, Object>) converter;
        converters.add(deCocotyzed);
    }

    public Optional<Converter<Object, Object>> converter(Class<?> sourceType, Class<?> targetType) {
        return converters.stream()
                .filter(c -> c.sourceType().isAssignableFrom(sourceType))
                .filter(c -> c.targetType().isAssignableFrom(targetType))
                .findFirst();
    }
}
