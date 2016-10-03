package cz.jalasoft.lifeconfig.collection;

import cz.jalasoft.lifeconfig.converter.Converter;
import cz.jalasoft.lifeconfig.converter.ConverterException;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-12.
 */
public final class PersonConverter implements Converter<String, Person> {

    @Override
    public Class<String> sourceType() {
        return String.class;
    }

    @Override
    public Class<Person> targetType() {
        return Person.class;
    }

    @Override
    public Person convert(String from) throws ConverterException {
        String[] names = from.split("\\s");

        if (names.length != 2) {
            throw new ConverterException(from, String.class, Person.class, "Unexpected format for person.");
        }

        String firstName = names[0];
        String lastName = names[1];

        return new Person(firstName, lastName);
    }
}
