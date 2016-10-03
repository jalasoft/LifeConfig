package cz.jalasoft.lifeconfig.collection;


import cz.jalasoft.lifeconfig.format.ConfigFormat;
import cz.jalasoft.lifeconfig.format.HoconFormat;
import cz.jalasoft.lifeconfig.format.YamlFormat;
import org.testng.annotations.Test;
import org.unitils.reflectionassert.ReflectionAssert;

import java.util.*;

import static cz.jalasoft.lifeconfig.LifeConfig.pretending;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-08.
 */
public class CollectionTest {

    @Test
    public void hoconConfigInterfaceConvertsListOfStringsPropertiesToJavaCollection() {
        configInterfaceConvertsListOfStringsPropertiesToJavaCollection(new HoconFormat(), "hocon_config.conf");
    }

    /*    @Test
        public void javaConfigInterfaceConvertsListOfStringsPropertiesToJavaCollection() {
            configInterfaceConvertsListOfStringsPropertiesToJavaCollection(new JavaPropertyFormat(), "java_config.conf");
        }
    */
    @Test
    public void ymlConfigInterfaceConvertsListOfStringsPropertiesToJavaCollection() {
        configInterfaceConvertsListOfStringsPropertiesToJavaCollection(new YamlFormat(), "yaml_config.yml");
    }

    private void configInterfaceConvertsListOfStringsPropertiesToJavaCollection(ConfigFormat format, String resourceName) {

        MyCoolConfig config = pretending(MyCoolConfig.class)
                .fromClasspath(getClass(), resourceName)
                .format(format)
                .load();

        Collection<String> names = config.names();

        ArrayList<String> expectation = new ArrayList();
        expectation.add("Honza");
        expectation.add("Michaela");
        expectation.add("Jirka");

        ReflectionAssert.assertLenientEquals(expectation, names);
    }

    @Test
    public void hoconConfigInterfaceConvertsListOfStringToListOfObjectViaConverter() {
        MyCoolConfig config = pretending(MyCoolConfig.class)
                .fromClasspath(getClass(), "hocon_config.conf")
                .hocon()
                .addConverter(new NotificationDefinitionConverter())
                .load();

        List<NotificationDefinition> actual = config.notifications();


        List<NotificationDefinition> expected = new ArrayList<>();
        expected.add(new NotificationDefinition(NotificationDefinition.Severity.WARN, "this is somethig that does not fail"));
        expected.add(new NotificationDefinition(NotificationDefinition.Severity.ERROR, "the world is being collapsed"));

        ReflectionAssert.assertLenientEquals(expected, actual);
    }

    /*
    @Test
    public void javaConfigInterfaceConvertsListOfStringToListOfObjectViaConverter() {
        MyCoolConfig config = pretending(MyCoolConfig.class)
                .fromClasspath(getClass(), "java_config.conf")
                .javaProperties("%")
                .addConverter(new NotificationDefinitionConverter())
                .load();

        List<NotificationDefinition> actual = config.notifications();

        List<NotificationDefinition> expected = new ArrayList<>();
        expected.add(new NotificationDefinition(NotificationDefinition.Severity.WARN, "this is somethig that does not fail"));
        expected.add(new NotificationDefinition(NotificationDefinition.Severity.ERROR, "the world is being collapsed"));

        ReflectionAssert.assertLenientEquals(expected, actual);
    }*/

    @Test
    public void yamlConfigInterfaceConvertsListOfStringToListOfObjectViaConverter() {
        MyCoolConfig config = pretending(MyCoolConfig.class)
                .fromClasspath(getClass(), "yaml_config.yml")
                .yaml()
                .addConverter(new NotificationDefinitionConverter())
                .load();

        List<NotificationDefinition> actual = config.notifications();


        List<NotificationDefinition> expected = new ArrayList<>();
        expected.add(new NotificationDefinition(NotificationDefinition.Severity.WARN, "this is somethig that does not fail"));
        expected.add(new NotificationDefinition(NotificationDefinition.Severity.ERROR, "the world is being collapsed"));

        ReflectionAssert.assertLenientEquals(expected, actual);
    }

    @Test
    public void hoconConfigInterfaceConvertsListOfStringIntoListOfObjectViaConverterAnnotation() {
        configInterfaceConvertsListOfStringIntoListOfObjectViaConverterAnnotation(new HoconFormat(), "hocon_config.conf");
    }

    @Test
    public void yamlConfigInterfaceConvertsListOfStringIntoListOfObjectViaConverterAnnotation() {
        configInterfaceConvertsListOfStringIntoListOfObjectViaConverterAnnotation(new YamlFormat(), "yaml_config.yml");
    }

    private void configInterfaceConvertsListOfStringIntoListOfObjectViaConverterAnnotation(ConfigFormat format, String resourceName) {
        MyCoolConfig config = pretending(MyCoolConfig.class)
                .fromClasspath(getClass(), resourceName)
                .format(format)
                .load();

        Set<Person> actual = config.people();

        Set<Person> expected = new HashSet<>();
        expected.add(new Person("Honza", "Lastovicka"));
        expected.add(new Person("Jiri", "Karasek"));
        expected.add(new Person("Misa", "Cerna"));

        ReflectionAssert.assertLenientEquals(expected, actual);
    }

    @Test
    public void hoconConfigInterfaceConvertsToListOfObjectsBasedOnConverterAndCollectionWithoutGenericType() {
        MyCoolConfig config = pretending(MyCoolConfig.class)
                .fromClasspath(getClass(), "hocon_config.conf")
                .hocon()
                .load();

        Collection actual = config.addresses();

        List<Address> expected = new ArrayList<>();
        expected.add(new Address("Kolin", "czech_republic", "Delnicka", "804"));
        expected.add(new Address("Kolin", "czech_republic", "AntoninaDvoraka", "1111"));

        ReflectionAssert.assertLenientEquals(expected, actual);
    }

    /*
    @Test
    public void javaConfigInterfaceConvertsToListOfObjectsBasedOnConverterAndCollectionWithoutGenericType() {
        MyCoolConfig config = pretending(MyCoolConfig.class)
                .fromClasspath(getClass(), "java_config.conf")
                .javaProperties("%")
                .load();

        Collection actual = config.addresses();

        List<Address> expected = new ArrayList<>();
        expected.add(new Address("Kolin", "czech_republic", "Delnicka", "804"));
        expected.add(new Address("Kolin", "czech_republic", "AntoninaDvoraka", "1111"));

        ReflectionAssert.assertLenientEquals(expected, actual);
    }*/

    @Test
    public void yamlConfigInterfaceConvertsToListOfObjectsBasedOnConverterAndCollectionWithoutGenericType() {
        MyCoolConfig config = pretending(MyCoolConfig.class)
                .fromClasspath(getClass(), "yaml_config.yml")
                .yaml()
                .load();

        Collection actual = config.addresses();

        List<Address> expected = new ArrayList<>();
        expected.add(new Address("Kolin", "czech_republic", "Delnicka", "804"));
        expected.add(new Address("Kolin", "czech_republic", "AntoninaDvoraka", "1111"));

        ReflectionAssert.assertLenientEquals(expected, actual);
    }

    @Test
    public void hoconConfigInterfaceConvertsToListOfObjectBasedOnRegisteredConverter() {
        MyCoolConfig config = pretending(MyCoolConfig.class)
                .fromClasspath(getClass(), "hocon_config.conf")
                .hocon()
                .addConverter(new AddressConverter())
                .load();

        Collection actual = config.addressesToo();

        List<Address> expected = new ArrayList<>();
        expected.add(new Address("Kolin", "czech_republic", "Delnicka", "804"));
        expected.add(new Address("Kolin", "czech_republic", "AntoninaDvoraka", "1111"));

        ReflectionAssert.assertLenientEquals(expected, actual);
    }

    /*
    @Test
    public void javaConfigInterfaceConvertsToListOfObjectBasedOnRegisteredConverter() {
        MyCoolConfig config = pretending(MyCoolConfig.class)
                .fromClasspath(getClass(), "java_config.conf")
                .javaProperties("%")
                .addConverter(new AddressConverter())
                .load();

        Collection actual = config.addressesToo();

        List<Address> expected = new ArrayList<>();
        expected.add(new Address("Kolin", "czech_republic", "Delnicka", "804"));
        expected.add(new Address("Kolin", "czech_republic", "AntoninaDvoraka", "1111"));

        ReflectionAssert.assertLenientEquals(expected, actual);
    }*/

    @Test
    public void yamlConfigInterfaceConvertsToListOfObjectBasedOnRegisteredConverter() {
        MyCoolConfig config = pretending(MyCoolConfig.class)
                .fromClasspath(getClass(), "yaml_config.yml")
                .yaml()
                .addConverter(new AddressConverter())
                .load();

        Collection actual = config.addressesToo();

        List<Address> expected = new ArrayList<>();
        expected.add(new Address("Kolin", "czech_republic", "Delnicka", "804"));
        expected.add(new Address("Kolin", "czech_republic", "AntoninaDvoraka", "1111"));

        ReflectionAssert.assertLenientEquals(expected, actual);
    }
}

