package cz.jalasoft.lifeconfig.nested;

import cz.jalasoft.lifeconfig.annotation.Key;
import cz.jalasoft.lifeconfig.annotation.KeyPrefix;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-08-17.
 */
@KeyPrefix("nesty")
public interface NestedConfiguration {

    Heroe heroeProperties();

    @KeyPrefix("myHero")
    interface Heroe {

        int strength();

        String villingness();

        @Key("hisMaterial")
        Material material();
    }

    enum Material {

        IRON, METAL;
    }
}
