package cz.jalasoft.lifeconfig.keyresolver;

import cz.jalasoft.lifeconfig.annotation.Key;

import java.lang.reflect.Method;

/**
 * This key resolved looks at the provided method and
 * checks whether it has an annotation {@link Key}.
 * If it has, then it is used for resolution of a
 * key that leads t a value that is returned directly
 * or indirectly by the method.
 * Otherwise it delegates to another key resolver.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-08-16.
 */
final class KeyAnnotationPropertyKeyResolver extends AbstractPropertyKeyResolverDecorator implements PropertyKeyResolver {

    KeyAnnotationPropertyKeyResolver(PropertyKeyResolver decorated) {
        super(decorated);
    }

    @Override
    public String resolveKey(Method method) {
        if (!method.isAnnotationPresent(Key.class)) {
            return delegate(method);
        }

        Key keyAnnotation = method.getAnnotation(Key.class);
        String key = keyAnnotation.value();
        return key;
    }
}
