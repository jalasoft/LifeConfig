package cz.jalasoft.lifeconfig.complex;

import cz.jalasoft.lifeconfig.annotation.Converter;
import cz.jalasoft.lifeconfig.annotation.Key;

import java.util.Set;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-14.
 */
public interface CassandraConfig {

    Set<Node> nodes();

    @Key("nodes")
    Set<String> nodeUrls();

    @Key("credential")
    @Converter(CredentialsConverter.class)
    Credentials credentials();

}
