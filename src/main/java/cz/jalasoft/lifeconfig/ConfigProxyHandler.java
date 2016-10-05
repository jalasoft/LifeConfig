package cz.jalasoft.lifeconfig;


import cz.jalasoft.lifeconfig.annotation.IgnoreProperty;
import cz.jalasoft.lifeconfig.keyresolver.PropertyKeyResolver;
import cz.jalasoft.lifeconfig.reader.ConfigReader;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static cz.jalasoft.lifeconfig.keyresolver.PropertyKeyResolvers.prefixAnnotationBefore;
import static cz.jalasoft.lifeconfig.keyresolver.PropertyKeyResolvers.standardMethodKeyResolver;
import static cz.jalasoft.lifeconfig.keyresolver.PropertyKeyResolvers.staticPrefix;

/**
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-07-26.
 */
final class ConfigProxyHandler implements InvocationHandler {

    private final Class<?> pretenderType;
    private final PropertyKeyResolver keyResolver;
    private final ConfigReader configReader;
    private final boolean isLife;

    private final ConcurrentMap<Method, Object> nestedProxies;

    ConfigProxyHandler(ConfigProxyAssembler<?> assembler) {
        this.pretenderType = assembler.pretenderType;
        this.keyResolver = assembler.keyResolver;
        this.configReader = assembler.propertyReader;
        this.isLife = assembler.isLife;

        this.nestedProxies = new ConcurrentHashMap<>();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            return invokeSafely(proxy, method, args);
        } catch (UndeclaredThrowableException exc) {
            throw exc.getUndeclaredThrowable();
        }
    }

    private Object invokeSafely(Object proxy, Method method, Object[] args) throws Throwable {
        if (!isMethodOriginatingInPretender(method)) {
            //handle toString
            return null;
        }

        if (method.isAnnotationPresent(IgnoreProperty.class)) {
            throw new PropertyIgnoredException(method);
        }

        if (method.isDefault()) {
            return invokeDefaultMethodDirectly(proxy, method, args);
        }

        String key = keyResolver.resolveKey(method);
        Optional<Object> maybeValue = configReader.readProperty(key, method);

        if (maybeValue.isPresent()) {
            return maybeValue.get();
        }

        return nestedProxies.computeIfAbsent(method, m -> newProxy(m, key));
    }

    private Object invokeDefaultMethodDirectly(Object proxy, Method method, Object... args) throws Throwable {
        Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
        constructor.setAccessible(true);

        Class<?> declaringClass = method.getDeclaringClass();

        return constructor.newInstance(declaringClass, MethodHandles.Lookup.PRIVATE)
                        .unreflectSpecial(method, declaringClass)
                        .bindTo(proxy)
                        .invokeWithArguments(args);
    }

    private Object newProxy(Method method, String key) {
        Class<?> returnType = method.getReturnType();
        return ConfigProxyAssembler.forType(returnType)
                .life(isLife)
                .configReader(configReader)
                .keyResolver(staticPrefix(key, prefixAnnotationBefore(standardMethodKeyResolver())))
                .assemble();
    }

    private boolean isMethodOriginatingInPretender(Method method) {
        return method.getDeclaringClass().equals(pretenderType);
    }
}
