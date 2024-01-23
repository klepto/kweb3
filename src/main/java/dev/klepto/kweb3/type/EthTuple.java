package dev.klepto.kweb3.type;

import com.google.common.collect.ImmutableList;
import lombok.experimental.Delegate;

/**
 * Represents ethereum <code>tuple</code> data type. All blockchain functions return this type and all solidity structs
 * are encoded as tuples.
 *
 * @param values a list of values contained within this tuple
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public record EthTuple(@Delegate ImmutableList<EthType> values) implements EthType {

    /* Solidity style tuple initializers */
    public static EthTuple tuple(EthType... values) {
        return new EthTuple(ImmutableList.copyOf(values));
    }

}
