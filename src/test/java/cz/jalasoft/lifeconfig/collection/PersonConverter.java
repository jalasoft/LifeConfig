package cz.jalasoft.lifeconfig.collection;

import cz.jalasoft.lifeconfig.converter.ConverterException;
import cz.jalasoft.lifeconfig.converter.TypesafeConverter;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-12.
 */
public final class PersonConverter extends TypesafeConverter<String, Person> {

    public PersonConverter() {
        super(String.class, Person.class);
    }

    @Override
    public Person convertSafely(String from) throws ConverterException {
        String[] names = from.split("\\s");

        if (names.length != 2) {
            throw new ConverterException(from, String.class, Person.class, "Unexpected format for person.");
        }

        String firstName = names[0];
        String lastName = names[1];

        return new Person(firstName, lastName);
    }
}
