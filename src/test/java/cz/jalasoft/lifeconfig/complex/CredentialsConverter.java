package cz.jalasoft.lifeconfig.complex;

import cz.jalasoft.lifeconfig.converter.ConverterException;
import cz.jalasoft.lifeconfig.converter.StringConverter;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-14.
 */
public final class CredentialsConverter extends StringConverter<Credentials> {

    @Override
    public Class<Credentials> targetType() {
        return Credentials.class;
    }

    @Override
    public Credentials convert(String from) throws ConverterException {
        String[] segments = from.split(":");

        if (segments.length != 2) {
            throw new ConverterException(from, String.class, Credentials.class, "Unexpected number of segemtns separated by ';' :" + segments.length);
        }

        return new Credentials(segments[0], segments[1]);
    }
}
