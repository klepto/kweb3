package dev.klepto.kweb3.core.ethereum.type.primitive;

import com.google.common.collect.ImmutableList;
import dev.klepto.kweb3.core.ethereum.type.EthCollectionValue;
import dev.klepto.kweb3.core.ethereum.type.EthValue;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Container for <code>ethereum tuple</code> value. Used to represent smart contract return values and <code>ethereum
 * structs</code>.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class EthTuple implements EthValue, EthCollectionValue<EthValue> {

    @NotNull
    private final ImmutableList<EthValue> values;

    /**
     * Constructs new <code>ethereum tuple</code> with the specified values.
     *
     * @param values the collection containing elements of the tuple
     */
    public EthTuple(@NotNull Collection<EthValue> values) {
        this.values = ImmutableList.copyOf(values);
    }

    /**
     * Returns the values contained within this <code>ethereum tuple</code>.
     *
     * @return the values contained within this <code>ethereum tuple</code>
     */
    @NotNull
    public ImmutableList<EthValue> values() {
        return values;
    }

    /**
     * Returns string representation of this <code>ethereum tuple</code>.
     *
     * @return the string representation of this <code>ethereum tuple</code>.
     */
    @Override
    @NotNull
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
    public boolean equals(@Nullable Object object) {
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
