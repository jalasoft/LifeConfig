package cz.jalasoft.lifeconfig.logger;

import java.io.PrintStream;

/**
 * An implementation of a logger that sends its
 * output to a print stream.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-07.
 */
final class PrintStreamLogger implements Logger {

    private static final String MESSAGE_PATTERN = "%s - %s: %s";

    private final PrintStream output;
    private final Class<?> type;

    PrintStreamLogger(PrintStream output, Class<?> type) {
        this.output = output;
        this.type = type;
    }

    @Override
    public void debug(String message) {
        String formattedMessage = String.format(MESSAGE_PATTERN, "DEBUG", type.toString(), message);
        output.println(formattedMessage);
    }
}
