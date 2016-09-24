package cz.jalasoft.lifeconfig.validation;


import cz.jalasoft.lifeconfig.annotation.Converter;
import cz.jalasoft.lifeconfig.annotation.IgnoreProperty;
import cz.jalasoft.lifeconfig.annotation.Key;
import cz.jalasoft.lifeconfig.annotation.KeyPrefix;
import cz.jalasoft.lifeconfig.converter.ConverterRepository;
import cz.jalasoft.lifeconfig.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

/**
 * A validator of a configuration interface and all other interfaces
 * as return types of methods.
 *
 * <ul>
 *     <li>Provided configuration type must be an interface</li>
 *     <li>If an annotation {@link KeyPrefix} is present, then its value must not be null or empty</li>
 *     <li>each method defined on the interface must have no parameter</li>
 *     <li>each method that has the annotation {@link Key} must have not null or empty value</li>
 *     <li>each method that has an annotation {@link Converter} the its value - class - must have constructor with no arguments</li>
 * </ul>
 *
 * @author Honza Lastovicka (lastovicka@avast.com)
 * @since 2016-07-26.
 */
public final class ProxyInterfaceValidator {

    private final ConverterRepository converterRegistry;

    public ProxyInterfaceValidator(ConverterRepository converterRegistry) {
        this.converterRegistry = converterRegistry;
    }

    public void validate(Class<?> type) {
        validateTypeIsInterface(type);
        validateKeyPrefixIsNotEmptyIfPresent(type);

        validateDeclaredMethods(type);

        returnTypeInterfacesForRecursiveValidation(type).forEach(this::validate);
    }

    private void validateTypeIsInterface(Class<?> type) {
        if (!type.isInterface()) {
            throw new ProxyInterfaceValidationException("Type %s is not an interface.", type);
        }
    }

    private void validateKeyPrefixIsNotEmptyIfPresent(Class<?> type) {
        if (!type.isAnnotationPresent(KeyPrefix.class)) {
            return;
        }

        KeyPrefix prefixAnnotation = type.getAnnotation(KeyPrefix.class);
        String prefix = prefixAnnotation.value();

        if (prefix.isEmpty()) {
            throw new ProxyInterfaceValidationException("Key prefix on type %s must not be empty.", type);
        }
    }

    private void validateDeclaredMethods(Class<?> type) {
        Method[] declaredMethods = type.getDeclaredMethods();

        stream(declaredMethods)
                .filter(m -> !m.isAnnotationPresent(IgnoreProperty.class))
                .forEach(this::validateDeclaredMethod);
    }

    private void validateDeclaredMethod(Method method) {
        validateZeroArguments(method);
        validateKeyAnnotationNotEmptyIfPresent(method);
        validateConverterHasParameterlessConstructorIfPresent(method);
    }

    private void validateZeroArguments(Method method) {
        int paramsCount = method.getParameterCount();

        if (paramsCount == 0) {
            return;
        }
        if (paramsCount > 0) {
            throw new ProxyInterfaceValidationException("Number of arguments on %s is %d. Must be zero.", method, paramsCount);
        }
    }

    private void validateKeyAnnotationNotEmptyIfPresent(Method method) {
        if (!method.isAnnotationPresent(Key.class)) {
            return;
        }

        Key annotation = method.getAnnotation(Key.class);
        String key = annotation.value();
        if (key.isEmpty()) {
            throw new ProxyInterfaceValidationException("Method %s has annotation @Key whose value is an empty string. Provide non empty resolveKey.", method);
        }
    }

    private void validateConverterHasParameterlessConstructorIfPresent(Method method) {
        if (!method.isAnnotationPresent(Converter.class)) {
            return;
        }

        Class<?> converterType = method.getAnnotation(Converter.class).value();
        Constructor<?>[] constructors = converterType.getConstructors();

        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                return;
            }
        }

        throw new ProxyInterfaceValidationException("Converter %s must have parameterless constructor.", converterType);
    }

    private Collection<Class<?>> returnTypeInterfacesForRecursiveValidation(Class<?> type) {
        Collection<Class<?>> suspiciousReturnTypes = stream(type.getDeclaredMethods())
                .map(this::returnTypeForRecursiveValidation)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return suspiciousReturnTypes;
    }

    private Optional<Class<?>> returnTypeForRecursiveValidation(Method method) {
        Class<?> returnType = method.getReturnType();

        if (ReflectionUtils.isPrimitiveOrWrapper(returnType)) {
            return Optional.empty();
        }

        if (Collection.class.isAssignableFrom(returnType)) {
            return Optional.empty();
        }

        if (method.isAnnotationPresent(Converter.class)) {
            return Optional.empty();
        }

        if (returnType.isInterface()) {
            return Optional.of(returnType);
        }

        return Optional.empty();
    }
}
