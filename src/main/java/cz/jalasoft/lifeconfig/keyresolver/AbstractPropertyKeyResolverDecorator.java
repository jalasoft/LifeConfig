package cz.jalasoft.lifeconfig.keyresolver;

import java.lang.reflect.Method;

/**
 * A common parent for all decorators of {@link PropertyKeyResolver}
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-08-16.
 */
abstract class AbstractPropertyKeyResolverDecorator implements PropertyKeyResolver {

    private final PropertyKeyResolver decorated;

    AbstractPropertyKeyResolverDecorator(PropertyKeyResolver decorated) {
        this.decorated = decorated;
    }

    final String delegate(Method method) {
        return decorated.resolveKey(method);
    }
}
