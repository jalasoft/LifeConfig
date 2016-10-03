package cz.jalasoft.lifeconfig.collection;

import cz.jalasoft.lifeconfig.converter.ConverterException;
import cz.jalasoft.lifeconfig.converter.StringConverter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-12.
 */
public final class AddressConverter extends StringConverter<Address> {

    private static final Pattern ADDRESS_PATTERN = Pattern.compile("(\\w{3,20}) (\\d{1,5}), (\\w{2,20}), country: (\\w{2,20})");

    @Override
    public Class<Address> targetType() {
        return Address.class;
    }

    @Override
    public Address convert(String from) throws ConverterException {
        Matcher m = ADDRESS_PATTERN.matcher(from);
        if (!m.matches()) {
            throw new ConverterException(from, String.class, Address.class, "Input string does not match pattern.");
        }

//        m.find();

        String street = m.group(1);
        String number = m.group(2);
        String town = m.group(3);
        String country = m.group(4);

        return new Address(town, country, street, number);
    }
}
