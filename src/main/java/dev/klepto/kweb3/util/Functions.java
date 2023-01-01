package dev.klepto.kweb3.util;

import java.util.function.Function;
import java.util.stream.IntStream;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Functions {

    public static IntStream until(int limit) {
        return until(0, limit);
    }

    public static IntStream until(int start, int limit) {
        return IntStream.range(start, limit);
    }

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
