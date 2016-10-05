package cz.jalasoft.lifeconfig.defaultmethods;

import cz.jalasoft.lifeconfig.annotation.Key;
import cz.jalasoft.lifeconfig.annotation.KeyPrefix;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-10-04.
 */
@KeyPrefix("page")
public interface PageAddress {

    String host();

    int port();

    String path();

    default URL url() {
        try {
            return new URL("http", host(), port(), path());
        } catch (MalformedURLException exc) {
            throw new RuntimeException(exc);
        }
    }

    @Key("first_anchor")
    Anchor firstAnchor();

}
