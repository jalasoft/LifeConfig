package cz.jalasoft.lifeconfig.keyresolver;


import cz.jalasoft.lifeconfig.annotation.KeyPrefix;

import java.lang.reflect.Method;

/**
 * This key resolver looks for an annotation {@link KeyPrefix}
 * of on a type that owns a method for which a key is being resolved.
 * A value inside the annotation is appended before a key resolved
 * by another (decorated) key resolver.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-08-16.
 */
final class PrefixAnnotationPropertyKeyResolver extends AbstractPropertyKeyResolverDecorator {

    PrefixAnnotationPropertyKeyResolver(PropertyKeyResolver decorated) {
        super(decorated);
    }

    @Override
    public String resolveKey(Method method) {
        String rawKey = delegate(method);
        String prefix = prefix(method);

        return prefix + (prefix.isEmpty() ? "" : ".") + rawKey;
    }

    private String prefix(Method method) {
        Class<?> type = method.getDeclaringClass();

        if (!type.isAnnotationPresent(KeyPrefix.class)) {
            return "";
        }

        KeyPrefix prefixAnnotation = type.getAnnotation(KeyPrefix.class);
        String prefix = prefixAnnotation.value();
        return prefix;
    }
}
