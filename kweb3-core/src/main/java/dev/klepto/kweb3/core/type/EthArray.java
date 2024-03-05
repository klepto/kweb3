package dev.klepto.kweb3.core.type;

import com.google.common.collect.ImmutableList;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

import static dev.klepto.kweb3.core.util.Conditions.require;

/**
 * Represents ethereum array data type. Used in place of JVM arrays in-order to enable immutability and support proper
 * type-safety for kweb3 API.
 *
 * @param capacity the size of the array, -1 indicates dynamic array, any other value indicates fixed size array
 * @param values   the elements of the array
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public record EthArray<T extends EthType>(int capacity,
                                          ImmutableList<T> values) implements EthType, EthCollection<T> {

    public EthArray {
        require(
                capacity == -1 || capacity == values.size(),
                "Array of capacity {} cannot fit {} values",
                capacity, values.size()
        );
    }

    /**
     * Returns the ethereum data type of this array.
     *
     * @return the class representing the type of elements contained in this array
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public Class<T> getComponentType() {
        if (values.isEmpty()) {
            // Unable to infer types on empty generic arrays.
            return (Class<T>) EthType.class;
        }
        return (Class<T>) values.get(0).getClass();
    }

    @Override
    public String toString() {
        if (values.isEmpty()) {
            return "unknown[]";
        }

        val solidityName = EthType.getSolidityName(values.get(0).getClass());
        val size = capacity >= 0 ? "[" + capacity + "]" : "";
        val children = values().stream().map(Object::toString).collect(Collectors.joining(","));
        return solidityName + size + "[" + children + "]";
    }

    /* Solidity style array initializers */

    /**
     * Creates a fixed-size ethereum array with given elements.
     *
     * @param size   the fixed array size
     * @param values the array element values
     * @return a fixed-sized ethereum array representation
     */
    @NotNull
    @SafeVarargs
    public static <T extends EthType> EthArray<T> array(int size, @NotNull T... values) {
        return new EthArray<>(size, ImmutableList.copyOf(values));
    }

    /**
     * Creates a fixed-size ethereum array by copying elements from given iterable.
     *
     * @param size   the fixed array size
     * @param values the iterable containing elements values
     * @return a fixed-sized ethereum array representation
     */
    @NotNull
    public static <T extends EthType> EthArray<T> array(int size, @NotNull Iterable<T> values) {
        return new EthArray<>(size, ImmutableList.copyOf(values));
    }

    /**
     * Creates a dynamic-size ethereum array with given elements.
     *
     * @param values the array element values
     * @return a dynamic-size ethereum array representation
     */
    @NotNull
    @SafeVarargs
    public static <T extends EthType> EthArray<T> array(@NotNull T... values) {
        return array(-1, values);
    }

    /**
     * Creates a dynamic-size ethereum array by copying elements from given iterable.
     *
     * @param values the array element values
     * @return a dynamic-size ethereum array representation
     */
    @NotNull
    public static <T extends EthType> EthArray<T> array(@NotNull Iterable<T> values) {
        return array(-1, values);
    }

}
