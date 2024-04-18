package dev.klepto.kweb3.core.ethereum.abi;

import dev.klepto.kweb3.core.ethereum.abi.descriptor.TypeDescriptor;
import dev.klepto.kweb3.core.ethereum.type.EthValue;
import org.jetbrains.annotations.NotNull;

/**
 * Represents ABI encoder for converting between string and ethereum data types.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface AbiEncoder {

    /**
     * Attempts to encode given {@link EthValue} value into an ABI string by automatically detecting type information.
     * This will work most of the time except for cases where we are dealing with empty arrays or empty tuples where
     * inference isn't possible. Hence, why more delicate features of the API rely on
     * {@link #encode(EthValue, TypeDescriptor)}.
     *
     * @param value the ethereum type value
     * @return the ABI encoded string containing the ethereum value
     */
    @NotNull
    default String encode(@NotNull EthValue value) {
        return encode(value, TypeDescriptor.parse(value));
    }

    /**
     * Encodes given {@link EthValue} value into an ABI string according to provided type description.
     *
     * @param value      the ethereum type value
     * @param descriptor the type description
     * @return the ABI encoded string containing the ethereum value
     */
    @NotNull
    String encode(@NotNull EthValue value, @NotNull TypeDescriptor descriptor);

}
