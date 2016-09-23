package cz.jalasoft.lifeconfig.validation;

/**
 * A validator of configuration interface that investigates its methods and
 * checks supported annotations.
 *
 * These are the rules of validation:
 *
 * //TODO
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-23.
 */
public final class ProxyInterfaceValidationException extends RuntimeException {

    public ProxyInterfaceValidationException(String cause, Object... params) {
        super(String.format(cause, params));
    }
}
