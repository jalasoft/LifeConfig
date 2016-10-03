package cz.jalasoft.lifeconfig.nested;


import cz.jalasoft.lifeconfig.LifeConfig;
import cz.jalasoft.lifeconfig.converter.Converter;
import cz.jalasoft.lifeconfig.converter.ConverterException;
import cz.jalasoft.lifeconfig.format.ConfigFormat;
import cz.jalasoft.lifeconfig.format.HoconFormat;
import cz.jalasoft.lifeconfig.format.JavaPropertyFormat;
import cz.jalasoft.lifeconfig.format.YamlFormat;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertSame;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-08-18.
 */
public class NestedConfigurationTest {


    private NestedConfiguration configuration(ConfigFormat provider, String resourceName) {
        return LifeConfig
                .pretending(NestedConfiguration.class)
                .format(provider)
                .fromClasspath(getClass(), resourceName)
                .addConverter(new Converter<String, NestedConfiguration.Material>() {
                    @Override
                    public Class<NestedConfiguration.Material> targetType() {
                        return NestedConfiguration.Material.class;
                    }

                    @Override
                    public Class<String> sourceType() {
                        return String.class;
                    }

                    @Override
                    public NestedConfiguration.Material convert(String from) throws ConverterException {
                        if (from.equalsIgnoreCase("metal")) {
                            return NestedConfiguration.Material.METAL;
                        }

                        if (from.equalsIgnoreCase("iron")) {
                            return NestedConfiguration.Material.IRON;
                        }

                        throw new ConverterException(from, String.class, NestedConfiguration.Material.class);
                    }
                })
                .load();
    }

    @Test
    public void javaNotRegisteredConverterForOnePropertyReturnValueThrowsException() {
        notRegisteredConverterForOnePropertyReturnValueThrowsException(new JavaPropertyFormat(), "java_config.conf");
    }

    @Test
    public void hoconNotRegisteredConverterForOnePropertyReturnValueThrowsException() {
        notRegisteredConverterForOnePropertyReturnValueThrowsException(new HoconFormat(), "hocon_config.conf");
    }

    @Test
    public void yamlNotRegisteredConverterForOnePropertyReturnValueThrowsException() {
        notRegisteredConverterForOnePropertyReturnValueThrowsException(new YamlFormat(), "yaml_config.yml");
    }

    private void notRegisteredConverterForOnePropertyReturnValueThrowsException(ConfigFormat provider, String resourceName) {

        NestedConfiguration config = configuration(provider, resourceName);
        NestedConfiguration.Heroe heroe = config.heroeProperties();

        assertEquals(heroe.strength(), 34);
    }

    @Test
    public void javaNestedProxyRetrievedSeveralTimesIsJustTheSameInstance() {
        nestedProxyRetrievedSeveralTimesIsJustTheSameInstance(new JavaPropertyFormat(), "java_config.conf");
    }

    @Test
    public void hoconNestedProxyRetrievedSeveralTimesIsJustTheSameInstance() {
        nestedProxyRetrievedSeveralTimesIsJustTheSameInstance(new HoconFormat(), "hocon_config.conf");
    }

    @Test
    public void yamlNestedProxyRetrievedSeveralTimesIsJustTheSameInstance() {
        nestedProxyRetrievedSeveralTimesIsJustTheSameInstance(new YamlFormat(), "yaml_config.yml");
    }

    private void nestedProxyRetrievedSeveralTimesIsJustTheSameInstance(ConfigFormat provider, String resourceName) {

        NestedConfiguration config = configuration(provider, resourceName);

        NestedConfiguration.Heroe heroe1 = config.heroeProperties();
        NestedConfiguration.Heroe heroe2 = config.heroeProperties();

        assertNotNull(heroe1);
        assertNotNull(heroe2);
        assertSame(heroe1, heroe2);
    }
}
