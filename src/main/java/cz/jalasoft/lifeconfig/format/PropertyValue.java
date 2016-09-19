package cz.jalasoft.lifeconfig.format;

/**
 * An encapsulation of a result of a configuration
 * property reading.
 *
 * <p>
 * Basically after reading a configuration property
 * by {@link ConfigFormat} a result might be one of
 * the following values:
 *
 * <ul>
 *     <li>value of the configuration property, any value (string, number etc.)</li>
 *     <li>no value - there is no configuration value for given key</li>
 *     <li>partial key - there will be a property value, but the provided key is not complete.</li>
 * </ul>
 *
 * The reason why there is the third point is that we can haven nested configuration property
 * that might be accessed from parent configuration property. A method on the parent
 * is associated with a key that leads to a value on its children. This is the case when
 * we want to retrieve a property by invoking a method returning another configuration interface
 * that will later lead to a regular value.
 *
 * </p>
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-09-15.
 */
public final class PropertyValue {

    /**
     * There is no property for given key.
     *
     * @return never null
     */
    public static PropertyValue notFound() {
        return new PropertyValue(Type.NOT_FOUND, null);
    }

    /**
     * There is a value for given key, and it is available via method {@link #value()}
     *
     * @param value the value
     * @return never null
     */
    public static PropertyValue found(Object value) {
        return new PropertyValue(Type.FOUND, value);
    }

    /**
     * There will be a value for given key, but not now.
     *
     * @return never null
     */
    public static PropertyValue incompletePath() {
        return new PropertyValue(Type.INCOMPLETE_PATH, null);
    }

    //---------------------------------------------------------------
    //INSTANCE SCOPE
    //---------------------------------------------------------------

    private enum Type {
        NOT_FOUND, FOUND, INCOMPLETE_PATH;
    }

    private final Type type;
    private final Object value;

    private PropertyValue(Type type, Object value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Gets a value of a property, but not now.
     *
     * @return never null
     * @throws IllegalStateException if there is no value because this instance has
     * not been created by method {@link #found(Object)}
     */
    public Object value() {
        if (value == null) {
            throw new IllegalStateException("No value, first check status of PropertyValue before retrieving value.");
        }
        return value;
    }

    /**
     * Gets whether this instance has value, that is, it has been created by
     * method {@link #found(Object)}
     *
     * @return
     */
    public boolean isFound() {
        return type == Type.FOUND;
    }

    /**
     * Gets whether this instance has no value because it does not exists, that
     * is, it has been created via method {@link #notFound()}
     *
     * @return
     */
    public boolean isNotFound() {
        return type == Type.NOT_FOUND;
    }

    /**
     * Gets whether this instance has been created without value because the key
     * is not complete. It means that an instance has been create by method
     * {@link #incompletePath()}
     *
     * @return
     */
    public boolean isIncomplete() {
        return type == Type.INCOMPLETE_PATH;
    }

    @Override
    public String toString() {
        return "PropertyValue[" + type + (isFound() ? ": " + value() : "") + "]";
    }
}
