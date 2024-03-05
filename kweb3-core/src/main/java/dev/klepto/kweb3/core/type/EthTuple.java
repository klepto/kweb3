package dev.klepto.kweb3.core.type;

import com.google.common.collect.ImmutableList;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Container for <code>ethereum tuple</code> value. Used to represent smart contract return values and <code>ethereum
 * structs</code>.
 *
 * @param values a list of values contained within this tuple
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public record EthTuple(ImmutableList<EthValue> values) implements EthValue, EthCollectionValue<EthValue> {

    /**
     * Returns string representation of this <code>ethereum tuple</code>.
     *
     * @return the string representation of this <code>ethereum tuple</code>.
     */
    @Override
    public String toString() {
        val children = values().stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
        return "tuple(" + children + ")";
    }

    /**
     * Returns hash code of this <code>ethereum tuple</code>.
     *
     * @return hash code of this <code>ethereum tuple</code>
     */
    @Override
    public int hashCode() {
        return values.hashCode();
    }

    /**
     * Compares this <code>ethereum tuple</code> to the specified object.
     *
     * @param object the object to compare with
     * @return true if the objects are the same; false otherwise
     */
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object == this) {
            return true;
        }
        if (object instanceof EthTuple other) {
            return values.equals(other.values);
        }
        return false;
    }

    /* Solidity style static initializers */
    @NotNull
    public static EthTuple tuple(@NotNull EthValue... values) {
        return new EthTuple(ImmutableList.copyOf(values));
    }

    @NotNull
    public static EthTuple tuple(@NotNull Collection<EthValue> values) {
        return new EthTuple(ImmutableList.copyOf(values));
    }

}
