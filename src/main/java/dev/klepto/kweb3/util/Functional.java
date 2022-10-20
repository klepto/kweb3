package dev.klepto.kweb3.util;

import java.util.function.Function;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Functional {

    public static <T, R> Function<T, R> tryOrElse(Function<? super T, ? extends R> mapper, R result) {
        return (value) -> {
            try {
                return mapper.apply(value);
            } catch (Throwable t) {
                return result;
            }
        };
    }

}