package cz.jalasoft.lifeconfig.basic;


import cz.jalasoft.lifeconfig.LifeConfig;
import cz.jalasoft.lifeconfig.PropertyIgnoredException;
import cz.jalasoft.lifeconfig.format.ConfigFormat;
import cz.jalasoft.lifeconfig.format.HoconFormat;
import cz.jalasoft.lifeconfig.format.JavaPropertyFormat;
import cz.jalasoft.lifeconfig.format.YamlFormat;
import cz.jalasoft.lifeconfig.reader.PropertyNotFoundException;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

/**
 * Created by honzales on 3.5.15.
 */
public class BasicConfigurationTest {

    @Test
    public void javaConfigurationProvidesExpectedValues() {
        configurationProvidesExpectedValues(new JavaPropertyFormat(), "java_config.conf");
    }

    @Test
    public void hoconConfigurationProvidesExpectedValues() {
        configurationProvidesExpectedValues(new HoconFormat(), "hocon_config.conf");
    }

    @Test
    public void yamlConfigurationProvidesExpectedValues() {
        configurationProvidesExpectedValues(new YamlFormat(), "yaml_config.yml");
    }

    public void configurationProvidesExpectedValues(ConfigFormat provider, String resourceName) {
        MyConfig config = LifeConfig.pretending(MyConfig.class)
                .format(provider)
                .fromClasspath(getClass(), resourceName)
                .load();

        assertEquals(config.getName(), "Honzales");
        assertEquals(config.getAge(), 32);

        MyDay myDay = config.getMyDay();
        assertEquals(myDay.getDay(), 25);
        assertEquals(myDay.getMonth(), MyDay.Month.FEBRUARY);
    }

    @Test(expectedExceptions = PropertyNotFoundException.class)
    public void javaNotExistingPropertyIsIndicatedAsRuntimeException() {
        notExistingPropertyIsIndicatedAsRuntimeException(new JavaPropertyFormat(), "java_config.conf");
    }

    @Test(expectedExceptions = PropertyNotFoundException.class)
    public void hoconNotExistingPropertyIsIndicatedAsRuntimeException() {
        notExistingPropertyIsIndicatedAsRuntimeException(new HoconFormat(), "hocon_config.conf");
    }

    @Test(expectedExceptions = PropertyNotFoundException.class)
    public void yamlNotExistingPropertyIsIndicatedAsRuntimeException() {
        notExistingPropertyIsIndicatedAsRuntimeException(new YamlFormat(), "yaml_config.yml");
    }

    private void notExistingPropertyIsIndicatedAsRuntimeException(ConfigFormat provider, String resourceName) {
        MyConfig config = LifeConfig.pretending(MyConfig.class)
                .format(provider)
                .fromClasspath(getClass(), resourceName)
                .load();

        config.isAdult();
    }

    @Test(expectedExceptions = PropertyIgnoredException.class)
    public void javaIgnoredPropertyMethodThrowsExceptionWhenInvoked() {
        ignoredPropertyMethodThrowsExceptionWhenInvoked(new JavaPropertyFormat(), "java_config.conf");
    }

    @Test(expectedExceptions = PropertyIgnoredException.class)
    public void hoconIgnoredPropertyMethodThrowsExceptionWhenInvoked() {
        ignoredPropertyMethodThrowsExceptionWhenInvoked(new JavaPropertyFormat(), "hocon_config.conf");
    }

    @Test(expectedExceptions = PropertyIgnoredException.class)
    public void yamlIgnoredPropertyMethodThrowsExceptionWhenInvoked() {
        ignoredPropertyMethodThrowsExceptionWhenInvoked(new YamlFormat(), "yaml_config.yml");
    }

    private void ignoredPropertyMethodThrowsExceptionWhenInvoked(ConfigFormat provider, String resourceName) {
        MyConfig config = LifeConfig.pretending(MyConfig.class)
                .format(provider)
                .fromClasspath(getClass(), resourceName)
                .load();

        config.pleaseIgnoreMe();
    }
}
