package cz.jalasoft.lifeconfig.reader;

import cz.jalasoft.lifeconfig.converterprovider.ConverterNotFoundException;
import cz.jalasoft.lifeconfig.converter.ConverterException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-20.
 */
public final class ReloadingConfigReader implements ConfigReader {

    private final ConfigReader decorated;

    private final LastModifiedMillisProvider lastModifiedMillisSuplier;
    private final boolean isReloading;

    private Long actualLastUpdateMillis;

    public ReloadingConfigReader(ConfigReader decorated, LastModifiedMillisProvider lasUpdateMillisSupplier, boolean isReloading) {
        this.decorated = decorated;
        this.lastModifiedMillisSuplier = lasUpdateMillisSupplier;
        this.isReloading = isReloading;
    }

    @Override
    public Optional<Object> readProperty(String key, Method method) throws IOException, ConverterNotFoundException, ConverterException {
        reloadSourceIfChanged();
        return reader().readProperty(key, method);
    }

    private ConfigReader reader() {
        return decorated;
    }

    private void reloadSourceIfChanged() throws IOException {
        if (!isReloading) {
            if (actualLastUpdateMillis == null) {
                actualLastUpdateMillis = 0l;
                reload();
            }
            return;
        }

        long lastModifiedMillis = lastModifiedMillisSuplier.get();

        if (actualLastUpdateMillis == null) {
            actualLastUpdateMillis = lastModifiedMillis;
            reload();
            return;
        }


        if (lastModifiedMillis > actualLastUpdateMillis) {
            actualLastUpdateMillis = lastModifiedMillis;
            reload();
        }
    }

    @Override
    public void reload() throws IOException {
        decorated.reload();
    }

    public interface LastModifiedMillisProvider {
        long get() throws IOException;
    }
}
