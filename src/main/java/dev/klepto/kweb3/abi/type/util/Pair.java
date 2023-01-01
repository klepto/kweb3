package dev.klepto.kweb3.abi.type.util;

import lombok.Value;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Value
public class Pair<V1, V2> {

    V1 first;
    V2 second;

    public static <V1, V2> Pair<V1, V2> of(V1 first, V2 second) {
        return new Pair<>(first, second);
    }

    public static <V1, V2> Pair<V1, V2> pair(V1 first, V2 second) {
        return of(first, second);
    }

}
