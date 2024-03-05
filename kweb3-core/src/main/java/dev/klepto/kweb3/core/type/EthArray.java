package dev.klepto.kweb3.core.type;

import com.google.common.collect.ImmutableList;
import lombok.With;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.stream.Collectors;

import static dev.klepto.kweb3.core.util.Conditions.require;

/**
 * Container for <code>ethereum array</code> value. Used in favor of JVM arrays in-order to enable immutability and API
 * type-safety.
 *
 * @param capacity the size of the array, <code>-1</code> indicates dynamic array, any other positive value indicates a
 *                 fixed size array
 * @param values   the elements of the array
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public record EthArray<T extends EthValue>(int capacity,
                                           ImmutableList<T> values) implements EthValue, EthCollectionValue<T> {

    /**
     * Constant indicating dynamic array size.
     */
    public static final int DYNAMIC_SIZE = -1;

    public EthArray {
        require(
                capacity == DYNAMIC_SIZE || capacity == values.size(),
                "Array of capacity {} cannot fit {} values",
                capacity, values.size()
        );
    }

    /**
     * Returns the {@link EthValue} type of this array.
     *
     * @return the class representing the type of elements contained in this array
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public Class<T> getComponentType() {
        if (values.isEmpty()) {
            // Unable to infer types on empty generic arrays.
            return (Class<T>) EthValue.class;
        }
        return (Class<T>) values.get(0).getClass();
    }

    /**
     * Returns string representation of this <code>ethereum array</code>.
     *
     * @return the string representation of this <code>ethereum array</code>.
     */
    @Override
    public String toString() {
        if (values.isEmpty()) {
            return "unknown[]";
        }

        val solidityName = EthValue.getSolidityName(values.get(0).getClass());
        val size = capacity >= 0 ? "[" + capacity + "]" : "";
        val children = values().stream().map(Object::toString).collect(Collectors.joining(","));
        return solidityName + size + "[" + children + "]";
    }

    /**
     * Returns hash code of this <code>ethereum bool</code>.
     *
     * @return hash code of this <code>ethereum bool</code>
     */
    @Override
    public int hashCode() {
        return values.hashCode();
    }

    /**
     * Compares this <code>ethereum bool</code> to the specified object.
     *
     * @param object the object to compare with
     * @return true if the objects are the same; false otherwise
     */
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!(object instanceof EthArray<?> other)) {
            return false;
        }
        return values.equals(other.values);
    }

    /* Solidity style static initializers */

    /**
     * Creates a fixed-size  <code>ethereum array</code> with given elements.
     *
     * @param size   the fixed array size
     * @param values the array values
     * @return a new fixed-sized <code>ethereum array</code> container
     */
    @NotNull
    @SafeVarargs
    public static <T extends EthValue> EthArray<T> array(int size, @NotNull T... values) {
        return new EthArray<>(size, ImmutableList.copyOf(values));
    }

    /**
     * Creates a fixed-size  <code>ethereum array</code> with given {@link Collection} <code>values</code>.
     *
     * @param size   the fixed array size
     * @param values the collection containing values
     * @return a new fixed-sized <code>ethereum array</code> container
     */
    @NotNull
    public static <T extends EthValue> EthArray<T> array(int size, @NotNull Collection<T> values) {
        return new EthArray<>(size, ImmutableList.copyOf(values));
    }

    /**
     * Creates a dynamic-size  <code>ethereum array</code> with given elements.
     *
     * @param values the array values
     * @return a new dynamic-size <code>ethereum array</code> container
     */
    @NotNull
    @SafeVarargs
    public static <T extends EthValue> EthArray<T> array(@NotNull T... values) {
        return array(DYNAMIC_SIZE, values);
    }

    /**
     * Creates a dynamic-size  <code>ethereum array</code> with given {@link Collection} <code>values</code>.
     *
     * @param values the collection containing values
     * @return a new dynamic-size <code>ethereum array</code> container
     */
    @NotNull
    public static <T extends EthValue> EthArray<T> array(@NotNull Collection<T> values) {
        return array(DYNAMIC_SIZE, values);
    }

}
