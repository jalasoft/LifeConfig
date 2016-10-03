package cz.jalasoft.lifeconfig;

import cz.jalasoft.lifeconfig.converterprovider.*;
import cz.jalasoft.lifeconfig.converter.Converter;
import cz.jalasoft.lifeconfig.converter.ConverterRepository;
import cz.jalasoft.lifeconfig.format.ConfigFormat;
import cz.jalasoft.lifeconfig.format.HoconFormat;
import cz.jalasoft.lifeconfig.format.JavaPropertyFormat;
import cz.jalasoft.lifeconfig.format.YamlFormat;
import cz.jalasoft.lifeconfig.keyresolver.PropertyKeyResolver;
import cz.jalasoft.lifeconfig.logger.Logger;
import cz.jalasoft.lifeconfig.logger.LoggerFactory;
import cz.jalasoft.lifeconfig.reader.ConfigReader;
import cz.jalasoft.lifeconfig.reader.ConvertingConfigReader;
import cz.jalasoft.lifeconfig.reader.ReloadingConfigReader;
import cz.jalasoft.lifeconfig.source.ConfigSource;
import cz.jalasoft.lifeconfig.source.FileConfigSource;
import cz.jalasoft.lifeconfig.validation.ConfigInterfaceValidator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static cz.jalasoft.lifeconfig.util.ArgumentAssertion.mustNotBeNull;
import static cz.jalasoft.lifeconfig.util.ArgumentAssertion.mustNotBeNullOrEmpty;
import static cz.jalasoft.lifeconfig.source.ClassPathConfigSource.*;

import static cz.jalasoft.lifeconfig.keyresolver.PropertyKeyResolvers.*;

/**
 * The main entry point of the LifeConfig library. It allows
 * configuring and creating a dynamic proxy of a provided
 * interface whose methods implements dynamically reading from
 * a property file of various formats (java config, hocon) and
 * sources (file, classpath resource) and return values for
 * given keys.
 *
 * <br/>
 *
 * One of the advantage of this library is the ability to read
 * fresh properties. If a content of a property file changes
 * during lifetime of an application, LifeConfig detects
 * such changes and reflects them in values returned by methods
 * of the configuration interface (dynamic proxy).
 *
 * <br/>
 *
 * <h3>Format</h3>
 *
 * At this time LifeConfig supports following formats:
 * <ul>
 *     <li>java properties</li>
 *     <li>hocon</li>
 * </ul>
 *
 * It is possible to customize the format via method {@link #format(ConfigFormat)}
 * and implementing an interface {@link ConfigFormat}.
 *
 * <h3>Source</h3>
 *
 * At this time LifeConfig supports following resources:
 *
 * <ul>
 *     <li>file on a filesystem</li>
 *     <li>file on classpath (i.e. inside JAR etc.)</li>
 * </ul>
 *
 * It is possible to specify custom source of configuration by
 * implementing {@link ConfigSource} interface and setting in via
 * method {@link #from(ConfigSource)}.
 *
 *
 * Basic usage:
 * {@code
 *  MyConfig config = LifeConfig.pretending(MyConfig.class)
 *      .hocon()
 *      .fromFile("/home/user/myconfig.conf")
 *      .live()
 *      .load();
 *
 *  String name = config.getName();
 *  int age = config.getAge();
 * }
 *
 *  @see "https://github.com/jalasoft/LifeConfig"
 *
 * @author Honza Lastovicka
 */
public final class LifeConfig<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LifeConfig.class);

    public static <T> LifeConfig<T> pretending(Class<T> type) {
        mustNotBeNull(type, "Configuration interface");

        return new LifeConfig(type);
    }

    //-------------------------------------------------------------------------------------
    //INSTANCE SCOPE
    //-------------------------------------------------------------------------------------

    private ConfigSource source;
    private ConfigFormat format;
    private boolean isLife;

    private final Class<T> type;
    private final ConverterRepository converterRepository;

    private LifeConfig(Class<T> type) {
        this.type = type;
        this.converterRepository = new ConverterRepository();

        LOGGER.debug("-----------------------------");
        LOGGER.debug("Initialization of LifeConfig.");
        LOGGER.debug("-----------------------------");
    }

    /**
     * Registers custom simpleConverter from a string property to a data type.
     *
     * @param converter a simpleConverter from string, must not be null
     * @throws java.lang.IllegalArgumentException if type or simpleConverter is null
     */
    public <C> LifeConfig<T> addConverter(Converter<? extends Object, ? extends Object> converter) {
        mustNotBeNull(converter, "Converter to be registered must not be null.");

        converterRepository.registerConverter(converter);

        LOGGER.debug("Converter of type " + converter.getClass() + " registered.");
        return this;
    }

    /**
     * Defines a file as a source of the configuration.
     * @param filePath must be a regular file path.
     * @return the same LifeConfig
     * @throws IllegalArgumentException if filePath is null, empty or not a regular file.
     */
    public LifeConfig<T> fromFile(String filePath) {
        mustNotBeNullOrEmpty(filePath, "File path");

        return fromFile(Paths.get(filePath));
    }

    /**
     * Defines a file as a source of the configuration.
     * @param path must be a regular file path.
     * @return the same LifeConfig
     * @throws IllegalArgumentException if filePath is null, or not a regular file.
     */
    public LifeConfig<T> fromFile(Path path) {
        mustNotBeNull(path, "Resource file");

        if (!Files.isRegularFile(path)) {
            throw new IllegalArgumentException("File path '" + path + "' must lead to a regular file.");
        }

        LOGGER.debug("File resource specified: " + path);
        return from(new FileConfigSource(path));
    }

    /**
     *
     * @param resourceName
     * @return
     */
    public LifeConfig<T> fromClasspath(String resourceName) {
        mustNotBeNullOrEmpty(resourceName, "Resource name");

        LOGGER.debug("Classpath resource specified: " + resourceName);
        return from(defaultClassloader(resourceName));
    }

    public LifeConfig<T> fromClasspath(ClassLoader classloader, String resourceName) {
        mustNotBeNull(classloader, "Classloader");
        mustNotBeNullOrEmpty(resourceName, "Resource name");

        LOGGER.debug("Classpath resource specified: " + resourceName);
        return from(classLoader(classloader, resourceName));
    }

    public LifeConfig<T> fromClasspath(Class<?> type, String resourceName) {
        mustNotBeNull(type, "Class");
        mustNotBeNullOrEmpty(resourceName, "Resource name");

        LOGGER.debug("Classpath resource specified: " + resourceName);
        return from(clazz(type, resourceName));
    }

    public LifeConfig<T> from(ConfigSource source) {
        mustNotBeNull(source, "Config source");

        this.source = source;

        LOGGER.debug("ConfigSource inserted of type " + source.getClass());
        return this;
    }

    /**
     * Marks the configuration being created as live which
     * means that before every accessing a property, a test
     * will be performed to check whether the content of the
     * source has changed or not, and will be reloaded in case
     * it has changed.
     *
     * @return the same LifeConfig
     */
    public LifeConfig<T> live() {
        this.isLife = true;

        LOGGER.debug("Live configuration specified.");
        return this;
    }

    /**
     * Sets java property as a format of configuration. Separator
     * of items is comma.
     *
     * @return the same LifeConfig
     */
    public LifeConfig<T> javaProperties() {
        return format(new JavaPropertyFormat());
    }

    /**
     * Sets java property file as a format of configuration.
     *
     * @param listItemsSeparatorRegex a regular expression of a
     * items separator must not be null or empty.
     *
     * @return the same LifeConfig instance
     */
    public LifeConfig<T> javaProperties(String listItemsSeparatorRegex) {
        mustNotBeNullOrEmpty(listItemsSeparatorRegex, "List item separator");

        return format(new JavaPropertyFormat(listItemsSeparatorRegex));
    }

    /**
     * Sets HOCON as a format of configuration.
     *
     * @return the same LifeConfig instance
     *
     * @see <a href="https://github.com/typesafehub/config/blob/master/HOCON.md">hocon</a>
     */
    public LifeConfig<T> hocon() {
        return format(new HoconFormat());
    }

    /**
     * Sets Yaml as a format of configuration.
     *
     * @return the same LifeConfig instance
     *
     * @see <a href="http://www.yaml.org/">yaml</a>
     */
    public LifeConfig<T> yaml() {
        return format(new YamlFormat());
    }

    /**
     * Sets custom format of configuration.
     *
     * @param format must not be null.
     *
     * @return the same LifeConfig instance
     */
    public LifeConfig<T> format(ConfigFormat format) {
        mustNotBeNull(format, "Config format");

        this.format = format;
        LOGGER.debug("Configuration format specified: " + format.getClass());
        return this;
    }

    /**
     * Prepares a new implementation of configuration interface (dynamically).
     *
     * @return never null
     */
    public T load() {
        LOGGER.debug("----------------------------");
        LOGGER.debug("Creating configuration proxy");
        LOGGER.debug("----------------------------");

        checkAllInserted();

        new ConfigInterfaceValidator(converterRepository).validate(type);

        LOGGER.debug("Validation of type " + type + " was successful");

        PropertyKeyResolver keyResolver = prefixAnnotationBefore(standardMethodKeyResolver());
        ConfigReader reader = reader(converterRepository);

        return ConfigProxyAssembler.forType(type)
                .configReader(reader)
                .keyResolver(keyResolver)
                .life(isLife)
                .assemble();
    }

    private void checkAllInserted() {
        if (source == null) {
            throw new IllegalStateException("Configuration source has not been inserted.");
        }
        if (format == null) {
            throw new IllegalStateException("Format of a configuration has not been inserted. Use format()/yaml()/hocon()/javaConfig");
        }
    }

    private ConfigReader reader(ConverterRepository converterRepository) {
        ConverterProvider conversion = conversion(converterRepository);
        ConfigReader convertingReader = new ConvertingConfigReader(source, format, conversion);
        return new ReloadingConfigReader(convertingReader, () -> source.lastModifiedMillis(), isLife);
    }

    private ConverterProvider conversion(ConverterRepository converterRepository) {
        ConverterProvider throwingExceptionConversion = new ConverterNotFoundThrowingConverterProvider();
        ConverterProvider defaultConversion = new ViaStringConverter(throwingExceptionConversion, converterRepository);
        ConverterProvider primitiveTypesConversion = new PrimitiveAndBoxedTypesConverterProvider(defaultConversion);
        ConverterProvider sameTypeConversion = new SameTypesConverterProvider(primitiveTypesConversion);
        ConverterProvider customConverterConversion = new RegisteredConverterProvider(sameTypeConversion, converterRepository);
        ConverterProvider annotationConversion = new ConverterAnnotationProvider(customConverterConversion);

        return annotationConversion;
    }
}
