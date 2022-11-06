package dev.klepto.kweb3.util.reflection;

import com.google.common.collect.Iterables;
import com.google.gson.internal.Primitives;
import lombok.SneakyThrows;
import lombok.val;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collection;

import static dev.klepto.kweb3.Web3Error.require;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Reflection {

    private Reflection() {
    }

    @SneakyThrows
    public static <T> T create(String className, Object... values) {
        val type = Reflection.class.getClassLoader().loadClass(className);
        return (T) create(type, values);
    }

    @SneakyThrows
    public static <T> T create(Class<T> type, Iterable<Object> values) {
        return create(type, Iterables.toArray(values, Object.class));
    }

    @SneakyThrows
    public static <T> T create(Class<T> type, Object... values) {
        val constructor = findConstructor(type, values);
        require(constructor != null, "Couldn't resolve {} constructor.", type);
        return constructor.newInstance(values);
    }

    public static boolean isMatchingTypes(Class<?>[] typesA, Class<?>[] typesB) {
        require(typesA.length == typesB.length, "Types length mismatch.");
        for (var i = 0; i < typesA.length; i++) {
            val typeA = Primitives.wrap(typesA[i]);
            val typeB = Primitives.wrap(typesB[i]);
            if (!typeA.isAssignableFrom(typeB)) {
                return false;
            }
        }
        return true;
    }

    public static <T> Constructor<T> findConstructor(Class<T> type, Object... values) {
        val valueTypes = Arrays.stream(values).map(Object::getClass).toArray(Class[]::new);
        val constructors = type.getDeclaredConstructors();
        val constructor = Arrays.stream(constructors)
                .filter(c -> c.getParameterCount() == values.length)
                .filter(c -> isMatchingTypes(c.getParameterTypes(), valueTypes))
                .findAny()
                .orElse(null);
        if (constructor == null) {
            return null;
        }

        constructor.setAccessible(true);
        return (Constructor<T>) constructor;
    }

}
