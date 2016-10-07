package cz.jalasoft.lifeconfig;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-10-07.
 */
public final class PropertyRetrievalException extends RuntimeException {

    public PropertyRetrievalException(Exception cause) {
        super(cause);
    }
}
