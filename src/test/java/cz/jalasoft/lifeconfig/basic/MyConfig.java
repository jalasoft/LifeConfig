package cz.jalasoft.lifeconfig.basic;

import cz.jalasoft.lifeconfig.annotation.Converter;
import cz.jalasoft.lifeconfig.annotation.IgnoreProperty;
import cz.jalasoft.lifeconfig.annotation.Key;
import cz.jalasoft.lifeconfig.annotation.KeyPrefix;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-06.
 */
@KeyPrefix("test")
public interface MyConfig {

    String getName();

    @Key("myage")
    int getAge();

    @Key("my_good_day")
    @Converter(MyDayConverter.class)
    MyDay getMyDay();

    @Key("my_bad_day")
    @Converter(MyDayConverter.class)
    MyDay getMyAnotherDay();

    @Key("lucky")
    double getMyLuckyDouble();

    String notAnnotated();

    int isAdult();

    @IgnoreProperty
    Long pleaseIgnoreMe();
}
