package cz.jalasoft.lifeconfig.converter;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-20.
 */
public class Converters {

    public static Converter identity(Class<?> type) {
        return new Converter() {
            @Override
            public Object convert(Object from) throws ConverterException {
                return from;
            }

            @Override
            public Class sourceType() {
                return type;
            }

            @Override
            public Class targetType() {
                return type;
            }
        };
    }
}
