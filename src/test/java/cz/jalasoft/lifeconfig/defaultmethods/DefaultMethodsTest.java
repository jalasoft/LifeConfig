package cz.jalasoft.lifeconfig.defaultmethods;

import cz.jalasoft.lifeconfig.LifeConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-10-04.
 */
public class DefaultMethodsTest {

    @Test
    public void defaultMethodInNameOfOtherConfigMethodProvidesExcpectedObject() {

        PageAddress address = LifeConfig.pretending(PageAddress.class)
                .fromClasspath(PageAddress.class, "address.cfg")
                .hocon()
                .load();

        URL actualURL = address.url();
        URL expectedURL = expectedURL();

        Assert.assertEquals(actualURL, expectedURL);
    }

    private URL expectedURL() {
        try {
            return new URL("http", "m.cd.cz", 80, "mobile");
        } catch (MalformedURLException exc) {
            throw new RuntimeException(exc);
        }
    }

    @Test
    public void nestedDefaultMethodProvidesObjectAsCompositionOfConfigPropertiesInTheInterface() {

        PageAddress address = LifeConfig.pretending(PageAddress.class)
                .fromClasspath(PageAddress.class, "address.cfg")
                .hocon()
                .load();

        Anchor.Coordinate actual = address.firstAnchor().asCoordinate();
        Anchor.Coordinate expected = new Anchor.Coordinate(34, 22);

        Assert.assertEquals(actual, expected);
    }
}
