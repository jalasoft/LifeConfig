package cz.jalasoft.lifeconfig.format;


import cz.jalasoft.lifeconfig.source.ConfigSource;

import java.io.IOException;

/**
 * An abstraction of a configuration format.
 *
 * <p>
 * Responsibility of this interface is to abstract various
 * formats of configurations. This interface is independent
 * of any source a configuration is coming.
 * </p>
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-07-26.
 */
public interface ConfigFormat {

    /**
     * Reads and caches configuration.
     *
     * @param source a source of configuration to be provided, as a reader
     * that should be closed inside this method. Source is never null.
     *
     * @throws IOException if an error during retrieving of configuration occurred.
     */
    void reload(ConfigSource source) throws IOException;

    /**
     * Reads one string value associated with a key.
     * @param key never null or empty.
     * @return never null
     */
    PropertyValue readProperty(String key);
}
