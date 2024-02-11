package dev.klepto.kweb3.abi;

import dev.klepto.kweb3.abi.descriptor.TypeDescriptor;
import dev.klepto.kweb3.type.EthType;
import org.jetbrains.annotations.NotNull;

/**
 * Represents ABI encoder for converting between string and ethereum data types.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface AbiEncoder {

    /**
     * Attempts to encode given {@link EthType} value into an ABI string by automatically detecting type information.
     * This will work most of the time except for cases where we are dealing with empty arrays or empty tuples where
     * inference isn't possible. Hence, why more delicate features of the API rely on
     * {@link #encode(EthType, TypeDescriptor)}.
     *
     * @param value the ethereum type value
     * @return the ABI encoded string containing the ethereum value
     */
    @NotNull
    default String encode(@NotNull EthType value) {
        return encode(value, TypeDescriptor.parse(value));
    }

    /**
     * Encodes given {@link EthType} value into an ABI string according to provided type description.
     *
     * @param value      the ethereum type value
     * @param descriptor the type description
     * @return the ABI encoded string containing the ethereum value
     */
    @NotNull
    String encode(@NotNull EthType value, @NotNull TypeDescriptor descriptor);

}
