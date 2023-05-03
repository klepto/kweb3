package dev.klepto.kweb3.abi;

import dev.klepto.kweb3.abi.type.AbiType;
import dev.klepto.kweb3.abi.type.Tuple;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface AbiDecoder {

    Tuple decode(String abi, AbiType type);

}
