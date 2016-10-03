package cz.jalasoft.lifeconfig.keyresolver;

/**
 * This class provides factory methods for retrieving various key resolver.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-08-16.
 */
public final class PropertyKeyResolvers {

    /**
     * Gets a resolver that derives a key from a method name.
     * @return never null
     */
    public static PropertyKeyResolver methodName() {
        return m -> m.getName();
    }

    /**
     * Gets a decorated existing key resolver with another resolver recognizing getter and
     * fall backing to the other resolver.
     *
     * @param decorated another resolver to be used for composition of final key
     * @return never null
     */
    public static PropertyKeyResolver getterExtractingOr(PropertyKeyResolver decorated) {
        return new GetterRecognizingKeyResolver(decorated);
    }

    /**
     * Gets a decorated existing key resolver with a resolver that checks an annotation
     * {@link cz.jalasoft.lifeconfig.annotation.Key} to resolve a key, otherwise it
     * delegates the existing (decorated) resolver.
     *
     * @param decorated another resolver to be used for composition of final key.
     * @return never null
     */
    public static PropertyKeyResolver keyAnnotationValueOr(PropertyKeyResolver decorated) {
        return new KeyAnnotationPropertyKeyResolver(decorated);
    }

    /**
     * Gets a decorated resolver with a resolver that looks for an annotation {@link cz.jalasoft.lifeconfig.annotation.KeyPrefix}
     * to prepend its value to a key resolved by the decorated resolver.
     *
     * @param decorated another resolver to be used for composition of final key
     * @return never null
     */
    public static PropertyKeyResolver prefixAnnotationBefore(PropertyKeyResolver decorated) {
        return new PrefixAnnotationPropertyKeyResolver(decorated);
    }

    /**
     * Gets an existing resolver decorated by a resolver that simply prepends a key resolved by
     * the decorated resolver with a constant string.
     *
     * @param prefix a prefix to be appended before key derived by the second argument
     * @param decorated another resolver to be used for composition of finalv key
     * @return never null
     */
    public static PropertyKeyResolver staticPrefix(String prefix, PropertyKeyResolver decorated) {
        return m -> prefix + (prefix.isEmpty() ? "" : ".") + decorated.resolveKey(m);
    }

    /**
     * Gets a chain of resolvers that resolve keys in this order:
     * <ul>
     *     <li>looks at an annotation {@link cz.jalasoft.lifeconfig.annotation.Key}</li>
     *     <li>or recognizes a getter name</li>
     *     <li>or gets a name of a method as a key</li>
     * </ul>
     *
     * @return never null
     */
    public static PropertyKeyResolver standardMethodKeyResolver() {
        return keyAnnotationValueOr(getterExtractingOr(methodName()));
    }

    private PropertyKeyResolvers() {
        throw new RuntimeException();
    }
}
