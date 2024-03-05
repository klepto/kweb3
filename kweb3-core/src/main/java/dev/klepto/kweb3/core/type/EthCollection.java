package dev.klepto.kweb3.core.type;

import java.util.List;
import java.util.stream.Stream;

public interface EthCollection<T extends EthType> extends EthType {

    List<T> values();

    default boolean isEmpty() {
        return values().isEmpty();
    }

    default int size() {
        return values().size();
    }

    default T get(int index) {
        return values().get(index);
    }

    default Stream<T> stream() {
        return values().stream();
    }

}
