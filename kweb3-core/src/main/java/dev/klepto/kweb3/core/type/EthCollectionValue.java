package dev.klepto.kweb3.core.type;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * Represents <code>ethereum</code> value that contains multiple values.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 * @see EthArray
 * @see EthTuple
 */
public interface EthCollectionValue<T extends EthValue> extends EthValue, Iterable<T> {

    /**
     * Returns the values contained within this collection.
     *
     * @return the values contained within this collection
     */
    List<T> values();

    /**
     * Returns the number of elements in this collection.
     *
     * @return the number of elements in this collection
     */
    default int size() {
        return values().size();
    }

    /**
     * Returns true if this collection contains no elements.
     *
     * @return true if this collection contains no elements
     */
    default boolean isEmpty() {
        return values().isEmpty();
    }

    /**
     * Returns the element at the specified position in this collection.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this collection
     */
    default T get(int index) {
        return values().get(index);
    }

    /**
     * Returns the first element in this collection.
     *
     * @return the first element in this collection
     */
    default T first() {
        return values().get(0);
    }

    /**
     * Returns the last element in this collection.
     *
     * @return the last element in this collection
     */
    default T last() {
        return values().get(size() - 1);
    }

    /**
     * Returns a sequential {@link Stream} with this collection as its source.
     *
     * @return a sequential {@link Stream} with this collection as its source
     */
    default Stream<T> stream() {
        return values().stream();
    }

    /**
     * Returns the {@link Iterator} associated with this collection.
     *
     * @return the iterator associated with this collection
     */
    @NotNull
    @Override
    default Iterator<T> iterator() {
        return values().iterator();
    }

}
