package cz.jalasoft.lifeconfig.complex;

import cz.jalasoft.lifeconfig.annotation.Key;
import cz.jalasoft.lifeconfig.annotation.KeyPrefix;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-14.
 */
@KeyPrefix("config")
public interface PostgresConfig {

    String getHost();

    Integer getPort();

    String getDbName();

    @Key("cred")
    Credentials getCredentials();
}
