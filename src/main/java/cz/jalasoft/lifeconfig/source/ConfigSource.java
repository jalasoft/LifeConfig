package cz.jalasoft.lifeconfig.source;

import java.io.IOException;
import java.io.Reader;

/**
 * An abstraction of a configuration source.
 *
 * <p>
 * This class is a basic component that allows
 * reading configuration in any format.
 * </p>
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-07-27.
 */
public interface ConfigSource {

    /**
     * Provides a reader that allows reading configuration
     * regardless of a format.
     *
     * @return never null
     * @throws IOException in case an error occurred.
     */
    Reader load() throws IOException;

    /**
     * Provides a name of the source. This can by anything that
     * identifies the source.
     *
     * @return never null
     */
    String name();

    /**
     * Provides UTC epoch time in millis of the last modification.
     * @return millis
     *
     * @throws IOException in case that an error occurred during
     * investigating the modification time
     */
    long lastModifiedMillis() throws IOException;
}
