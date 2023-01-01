package dev.klepto.kweb3.abi;

import dev.klepto.kweb3.abi.type.AbiType;
import dev.klepto.kweb3.abi.type.Struct;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface AbiDecoder {

    Struct decode(String abi, AbiType type);

}
