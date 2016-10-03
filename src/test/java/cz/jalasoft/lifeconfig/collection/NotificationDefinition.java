package cz.jalasoft.lifeconfig.collection;


/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-08.
 */
public final class NotificationDefinition {

    public enum Severity {
        WARN, ERROR
    }

    private final Severity severity;
    private final String text;

    public NotificationDefinition(Severity severity, String text) {
        this.severity = severity;
        this.text = text;
    }

    public Severity severity() {
        return severity;
    }

    public String text() {
        return text;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof NotificationDefinition)) {
            return false;
        }

        NotificationDefinition that = (NotificationDefinition) obj;

        if (!this.severity.equals(that.severity)) {
            return false;
        }

        return this.text.equals(that.text);
    }

    @Override
    public int hashCode() {
        int result = 17;

        result = result * 37 + severity.hashCode();
        result = result * 37 + text.hashCode();

        return result;
    }
}
