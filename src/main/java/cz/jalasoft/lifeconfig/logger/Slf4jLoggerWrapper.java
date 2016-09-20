package cz.jalasoft.lifeconfig.logger;

/**
 * An implemntation of a logger that delegates
 * to an external standard logging library -
 * Slf4j.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-07.
 */
final class Slf4jLoggerWrapper implements Logger {

    private final org.slf4j.Logger logger;

    Slf4jLoggerWrapper(Class<?> type) {
        this.logger = org.slf4j.LoggerFactory.getLogger(type);
    }

    @Override
    public void debug(String message) {
        logger.debug(message);
    }
}
