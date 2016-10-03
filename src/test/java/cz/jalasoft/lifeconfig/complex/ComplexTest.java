package cz.jalasoft.lifeconfig.complex;

import cz.jalasoft.lifeconfig.LifeConfig;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.testng.Assert.assertEquals;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-14.
 */
public class ComplexTest {

    @Test
    public void yamlComplexTest() {
        Configuration config = LifeConfig.pretending(Configuration.class)
                .addConverter(new CredentialsConverter())
                .yaml()
                .fromClasspath(getClass(), "yaml_config.yml")
                .load();

        assertEquals(config.getDelayMinutes(), 23.345f);
        assertEquals(config.deletionMask(), (byte) 122);

        LocalDateTime expectedExpiration = LocalDateTime.parse("2016-11-11T10:15:30", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        assertEquals(config.getExpirationDate(), expectedExpiration);
        assertEquals(config.isActual(), true);


    }
}
