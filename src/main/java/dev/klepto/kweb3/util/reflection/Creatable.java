package dev.klepto.kweb3.util.reflection;

import com.google.common.reflect.TypeToken;
import lombok.SneakyThrows;
import lombok.val;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface Creatable<T> {

    @SneakyThrows
    default T create(Object... args) {
        val type = new TypeToken<T>(getClass()) {}.getRawType();
        return (T) Reflection.create(type, args);
    }

}