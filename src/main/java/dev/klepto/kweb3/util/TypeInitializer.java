package dev.klepto.kweb3.util;

import com.google.common.reflect.TypeToken;
import lombok.SneakyThrows;
import lombok.val;

import java.util.Arrays;

/**
 * Reflective initializer utility to enable class initialization without constructor look-up.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface TypeInitializer<T> {

    @SneakyThrows
    default T create(Object... args) {
        val type = new TypeToken<T>(getClass()) {
        }.getRawType();
        val argsTypes = Arrays.stream(args).map(Object::getClass).toArray(Class[]::new);
        val constructor = type.getDeclaredConstructor(argsTypes);
        constructor.setAccessible(true);
        return (T) constructor.newInstance(args);
    }

}
