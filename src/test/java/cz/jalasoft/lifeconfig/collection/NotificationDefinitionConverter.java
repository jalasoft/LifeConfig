package cz.jalasoft.lifeconfig.collection;

import cz.jalasoft.lifeconfig.converter.ConverterException;
import cz.jalasoft.lifeconfig.converter.TypesafeConverter;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-08.
 */
final class NotificationDefinitionConverter extends TypesafeConverter<String, NotificationDefinition> {

    public NotificationDefinitionConverter() {
        super(String.class, NotificationDefinition.class);
    }

    @Override
    public NotificationDefinition convertSafely(String from) throws ConverterException {
        String[] tokens = from.split(":");

        if (tokens.length != 2) {
            throw new ConverterException(from, String.class, NotificationDefinition.class, "Incorrect format.");
        }

        NotificationDefinition.Severity severity = NotificationDefinition.Severity.valueOf(tokens[0]);
        String text = tokens[1];

        return new NotificationDefinition(severity, text);
    }
}
