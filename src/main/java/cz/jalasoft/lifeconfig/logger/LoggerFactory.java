package cz.jalasoft.lifeconfig.logger;

/**
 * Own internal logger factory.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-07.
 */
public final class LoggerFactory {

    public static Logger getLogger(Class<?> type) {
        if (isSlf4jOnClasspath()) {
            return new Slf4jLoggerWrapper(type);
        }

        return new PrintStreamLogger(System.out, type);
    }

    private static boolean isSlf4jOnClasspath() {
        try {
            Class.forName("org.slf4j.Logger");
            return true;
        } catch (ClassNotFoundException exc) {
            return false;
        }
    }

    private LoggerFactory() {
        throw new RuntimeException();
    }
}
