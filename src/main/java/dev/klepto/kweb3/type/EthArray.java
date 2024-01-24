package dev.klepto.kweb3.type;

import com.google.common.collect.ImmutableList;
import lombok.experimental.Delegate;
import lombok.val;

import java.util.stream.Collectors;

import static dev.klepto.kweb3.error.Conditions.require;

/**
 * Represents ethereum array data type. Used in place of JVM arrays in-order to enable immutability and support proper
 * type-safety for kweb3 API.
 *
 * @param capacity the size of the array, -1 indicates dynamic array, any other value indicates fixed size array
 * @param values   the elements of the array
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public record EthArray<T extends EthType>(int capacity, @Delegate ImmutableList<T> values) implements EthType {

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
    public Class<T> getComponentType() {
        if (isEmpty()) {
            // Unable to infer types on empty generic arrays.
            return (Class<T>) EthType.class;
        }
        return (Class<T>) get(0).getClass();
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "unknown[]";
        }

        val solidityName = EthType.getSolidityName(get(0).getClass());
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
    public static <T extends EthType> EthArray<T> array(int size, T... values) {
        return new EthArray<>(size, ImmutableList.copyOf(values));
    }

    /**
     * Creates a dynamic-size ethereum array with given elements.
     *
     * @param values the array element values
     * @return a dynamic-size ethereum array representation
     */
    public static <T extends EthType> EthArray<T> array(T... values) {
        return array(-1, values);
    }

}
