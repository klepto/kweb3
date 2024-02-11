package dev.klepto.kweb3.type;

import com.google.common.collect.ImmutableList;
import lombok.experimental.Delegate;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

/**
 * Represents ethereum <code>tuple</code> data type. All blockchain functions return this type and all solidity structs
 * are encoded as tuples.
 *
 * @param values a list of values contained within this tuple
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public record EthTuple(@Delegate ImmutableList<EthType> values) implements EthType {

    @Override
    public String toString() {
        val children = values().stream().map(Object::toString).collect(Collectors.joining(","));
        return "tuple(" + children + ")";
    }

    /* Solidity style tuple initializers */
    @NotNull
    public static EthTuple tuple(@NotNull EthType... values) {
        return new EthTuple(ImmutableList.copyOf(values));
    }

    @NotNull
    public static EthTuple tuple(@NotNull Iterable<EthType> values) {
        return new EthTuple(ImmutableList.copyOf(values));
    }

}
