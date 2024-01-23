package dev.klepto.kweb3.abi;

import dev.klepto.kweb3.type.EthTuple;

/**
 * Represents ABI decoder for converting between string and ethereum data types.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface AbiDecoder {

    /**
     * Decodes given ABI string into ethereum values contained within {@link EthTuple}.
     *
     * @param abi            the ABI data string
     * @param typeDescriptor the type description of given string
     * @return a tuple containing decoded values
     */
    EthTuple decode(String abi, AbiTypeDescriptor typeDescriptor);

}
