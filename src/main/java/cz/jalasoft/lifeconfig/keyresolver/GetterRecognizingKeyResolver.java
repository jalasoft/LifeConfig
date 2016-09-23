package cz.jalasoft.lifeconfig.keyresolver;

import java.lang.reflect.Method;

/**
 * A key resolver that analyzes a provided method to derive a key by
 * checking whether the name of the method is getter (starts with get or is),
 * if so, then it returns the name of the method without the prefix, otherwise
 * it delegates to another key resolver.
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-08-16.
 */
final class GetterRecognizingKeyResolver extends AbstractPropertyKeyResolverDecorator implements PropertyKeyResolver {

    GetterRecognizingKeyResolver(PropertyKeyResolver decorated) {
        super(decorated);
    }

    @Override
    public String resolveKey(Method method) {
        String methodName = method.getName();

        if (methodName.startsWith("get")) {
            return resolveGetterKey(methodName, "get");
        }

        if (methodName.startsWith("is")) {
            return resolveGetterKey(methodName, "is");
        }

        return delegate(method);
    }

    private String resolveGetterKey(String methodName, String prefix) {
        String methodNAmeWithoutGet = methodName.replace(prefix, "");
        String firstCharInLowerCase = String.valueOf(methodNAmeWithoutGet.charAt(0)).toLowerCase();

        String key = firstCharInLowerCase + methodNAmeWithoutGet.substring(1, methodNAmeWithoutGet.length());
        return key;
    }
}
