package dev.klepto.kweb3.abi;

import dev.klepto.kweb3.abi.descriptor.TypeDescriptor;
import dev.klepto.kweb3.type.EthType;

/**
 * Represents ABI encoder for converting between string and ethereum data types.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface AbiEncoder {

    /**
     * Attempts to encode given {@link EthType} value into an ABI string by automatically detecting type information.
     * This will work most of the time except some edge cases where we are dealing with empty arrays etc. Hence why more
     * delicate features of the API rely on {@link #encode(EthType, TypeDescriptor)}.
     *
     * @param value the ethereum type value
     * @return the ABI encoded string containing the ethereum value
     */
    String encode(EthType value);

    /**
     * Attempts to encode given {@link EthType} value into an ABI string according to provided type description.
     *
     * @param value          the ethereum type value
     * @param typeDescriptor the type description
     * @return the ABI encoded string containing the ethereum value
     */
    String encode(EthType value, TypeDescriptor typeDescriptor);

}
