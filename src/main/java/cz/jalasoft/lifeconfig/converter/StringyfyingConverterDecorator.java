package cz.jalasoft.lifeconfig.converter;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-10-06.
 */
public final class StringyfyingConverterDecorator implements Converter {

    private final Converter decorated;

    public StringyfyingConverterDecorator(Converter decorated) {
        this.decorated = decorated;
    }

    @Override
    public Object convert(Object from) throws ConverterException {
        if (from == null) {
            throw new IllegalArgumentException("Object to be converted must not be null.");
        }
        return decorated.convert(from.toString());
    }

    @Override
    public Class<?> sourceType() {
        return Object.class;
    }

    @Override
    public Class<?> targetType() {
        return String.class;
    }
}
