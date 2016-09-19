package cz.jalasoft.lifeconfig.source;



import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.MissingResourceException;
import java.util.function.Supplier;

import static cz.jalasoft.lifeconfig.util.ArgumentAssertion.*;

/**
 * A configuration provider that reads files from
 * classpath.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-07-27.
 */
public final class ClassPathConfigSource implements ConfigSource {

    public static ClassPathConfigSource fromDefaultClassloader(String resourceName) {
        mustNotBeNullOrEmpty(resourceName, "Name of a  classpath resource.");

        URL resourceUrl = ClassPathConfigSource.class.getClassLoader().getResource(resourceName);

        if (resourceUrl == null) {
            throw new MissingResourceException("Cannot load classpath resource: " + resourceName, null, null);
        }

        Supplier<InputStream> inputStreamProvider = () -> ClassPathConfigSource.class.getClassLoader().getResourceAsStream(resourceName);

        return new ClassPathConfigSource(resourceUrl, inputStreamProvider);
    }

    public static ClassPathConfigSource fromClassLoader(ClassLoader loader, String resourceName) {
        mustNotBeNull(loader, "Loader");
        mustNotBeNullOrEmpty(resourceName, "Name of a  classpath resource.");

        URL resourceUrl = loader.getResource(resourceName);

        if (resourceUrl == null) {
            throw new MissingResourceException("Cannot load classpath resource by classloader. Resource name: " + resourceName, null, null);
        }

        Supplier<InputStream> inputStreamProvider = () -> loader.getResourceAsStream(resourceName);

        return new ClassPathConfigSource(resourceUrl, inputStreamProvider);
    }

    public static ClassPathConfigSource fromClass(Class<?> loader, String resourceName) {
        mustNotBeNull(loader, "Loader");
        mustNotBeNullOrEmpty(resourceName, "Name of a  classpath resource.");

        URL resourceUrl = loader.getResource(resourceName);

        if (resourceUrl == null) {
            throw new MissingResourceException("Cannot load classpath resource by class. Resource name: " + resourceName, null, null);
        }

        Supplier<InputStream> inputStreamProvider = () -> loader.getResourceAsStream(resourceName);

        return new ClassPathConfigSource(resourceUrl, inputStreamProvider);
    }

    //------------------------------------------------------------------
    //INSTANCE SCOPE
    //-------------------------------------------------------------------

    private final URL resourceUrl;
    private final Supplier<InputStream> inputStreamProvider;

    private ClassPathConfigSource(URL resourceUrl, Supplier<InputStream> inputStreamProvider) {
        this.resourceUrl = resourceUrl;
        this.inputStreamProvider = inputStreamProvider;
    }

    @Override
    public Reader load() throws IOException {
        InputStream input = inputStreamProvider.get();

        return new InputStreamReader(input);
    }

    @Override
    public String name() {
        return resourceUrl.getFile();
    }

    @Override
    public long lastModifiedMillis() throws IOException {
        try {
            Path file = Paths.get(resourceUrl.toURI());
            return Files.getLastModifiedTime(file).toMillis();
        } catch (URISyntaxException exc) {
            throw new RuntimeException(exc);
        }
    }
}
