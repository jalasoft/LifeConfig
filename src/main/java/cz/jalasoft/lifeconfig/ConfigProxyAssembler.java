package cz.jalasoft.lifeconfig;

import cz.jalasoft.lifeconfig.keyresolver.PropertyKeyResolver;
import cz.jalasoft.lifeconfig.reader.ConfigReader;

import java.lang.reflect.Proxy;

/**
 * This class constructs a final dynamic proxy used by the client.
 *
 * It provides builder methods for setting all mandatory
 * values in a human readable way.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-08-19.
 */
final class ConfigProxyAssembler<T> {

    static <T> ConfigProxyAssembler<T> forType(Class<T> type) {
        return new ConfigProxyAssembler(type);
    }

    //----------------------------------------------------------
    //INSTANCE SCOPE
    //----------------------------------------------------------

    final Class<?> pretenderType;
    PropertyKeyResolver keyResolver;
    ConfigReader propertyReader;
    boolean isLife;


    private ConfigProxyAssembler(Class<?> interfaceType) {
        this.pretenderType = interfaceType;
    }

    ConfigProxyAssembler<T> keyResolver(PropertyKeyResolver keyResolver) {
        this.keyResolver = keyResolver;
        return this;
    }

    ConfigProxyAssembler<T> configReader(ConfigReader reader) {
        this.propertyReader = reader;
        return this;
    }

    ConfigProxyAssembler<T> life(boolean isLife) {
        this.isLife = isLife;
        return this;
    }

    /**
     * Constructs a dynamic proxy.
     *
     * @return never null
     */
    T assemble() {
        ConfigProxyHandler handler = new ConfigProxyHandler(this);
        return (T) Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[]{pretenderType}, handler);
    }
}
