package cz.jalasoft.lifeconfig.converter;

import cz.jalasoft.lifeconfig.LifeConfig;
import cz.jalasoft.lifeconfig.format.ConfigFormat;
import cz.jalasoft.lifeconfig.format.HoconFormat;
import cz.jalasoft.lifeconfig.format.JavaPropertyFormat;
import cz.jalasoft.lifeconfig.format.YamlFormat;
import org.testng.annotations.Test;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-08-20.
 */
public class ConverterCompatibilityTest {

    @Test(expectedExceptions = ClassCastException.class)
    public void hoconConverterInAnnotationIsNotCompatibleWithReturnTypeOfTheInterfaceMethod() {
        converterInAnnotationIsNotCompatibleWithReturnTypeOfTheInterfaceMethod(new HoconFormat(), "hocon_config.conf");
    }

    @Test(expectedExceptions = ClassCastException.class)
    public void javaConverterInAnnotationIsNotCompatibleWithReturnTypeOfTheInterfaceMethod() {
        converterInAnnotationIsNotCompatibleWithReturnTypeOfTheInterfaceMethod(new JavaPropertyFormat(), "java_config.conf");
    }

    @Test(expectedExceptions = ClassCastException.class)
    public void yamlConverterInAnnotationIsNotCompatibleWithReturnTypeOfTheInterfaceMethod() {
        converterInAnnotationIsNotCompatibleWithReturnTypeOfTheInterfaceMethod(new YamlFormat(), "yaml_config.yml");
    }

    public void converterInAnnotationIsNotCompatibleWithReturnTypeOfTheInterfaceMethod(ConfigFormat provider, String resourceName) {

        Configuration config = LifeConfig.pretending(Configuration.class)
                .fromClasspath(getClass(), resourceName)
                .format(provider)
                .load();

        config.age();
    }

    //--------------------------------------------------
    //CONFIGURATION INTERFACE
    //--------------------------------------------------

    public interface Configuration {

        @cz.jalasoft.lifeconfig.annotation.Converter(AgeConverter.class)
        String age();
    }

    //--------------------------------------------------
    //CONVERTER
    //--------------------------------------------------
    public static final class AgeConverter implements cz.jalasoft.lifeconfig.converter.Converter<String, Integer> {

        @Override
        public Class<Integer> targetType() {
            return Integer.class;
        }

        @Override
        public Class<String> sourceType() {
            return String.class;
        }

        @Override
        public Integer convert(String from) throws ConverterException {
            return Integer.valueOf(from);
        }
    }
}
