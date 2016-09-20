package cz.jalasoft.lifeconfig.converterprovider;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-15.
 */
public final class ConverterNotFoundException extends Exception {

    private final Class<?> sourceType;
    private final Class<?> targetType;

    public ConverterNotFoundException(Class<?> sourceType, Class<?> targetType) {
        this.sourceType = sourceType;
        this.targetType = targetType;
    }

    @Override
    public String getMessage() {
        return "No converter from " + sourceType + " to " + targetType + " has been found.";
    }
}
