package cz.jalasoft.lifeconfig.complex;

import cz.jalasoft.lifeconfig.annotation.Key;

import java.time.LocalDateTime;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-14.
 */
public interface Configuration {

    @Key("delayMinutes")
    float getDelayMinutes();

    byte deletionMask();

    LocalDateTime getExpirationDate();

    boolean isActual();

    PostgresConfig postgress();

    CassandraConfig getCassandra();
}
