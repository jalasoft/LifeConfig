package cz.jalasoft.lifeconfig.collection;


import cz.jalasoft.lifeconfig.annotation.Converter;
import cz.jalasoft.lifeconfig.annotation.Key;
import cz.jalasoft.lifeconfig.annotation.KeyPrefix;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-08.
 */
@KeyPrefix("config")
public interface MyCoolConfig {

    Collection<String> names();

    @Key("notification.defs")
    List<NotificationDefinition> notifications();


    @Converter(PersonConverter.class)
    Set<Person> people();

    @Key("addresses")
    @Converter(AddressConverter.class)
    Collection addresses();

    @Key("addresses")
    Collection<Address> addressesToo();
}


