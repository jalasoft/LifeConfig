package cz.jalasoft.lifeconfig.collection;

import cz.jalasoft.lifeconfig.converter.Converter;
import cz.jalasoft.lifeconfig.converter.ConverterException;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-08.
 */
final class NotificationDefinitionConverter implements Converter<String, NotificationDefinition> {

    @Override
    public Class<String> sourceType() {
        return String.class;
    }

    @Override
    public Class<NotificationDefinition> targetType() {
        return NotificationDefinition.class;
    }

    @Override
    public NotificationDefinition convert(String from) throws ConverterException {
        String[] tokens = from.split(":");

        if (tokens.length != 2) {
            throw new ConverterException(from, String.class, NotificationDefinition.class, "Incorrect format.");
        }

        NotificationDefinition.Severity severity = NotificationDefinition.Severity.valueOf(tokens[0]);
        String text = tokens[1];

        return new NotificationDefinition(severity, text);
    }
}
