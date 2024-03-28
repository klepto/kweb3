package dev.klepto.kweb3.core.contract.type;

import dev.klepto.kweb3.core.abi.HeadlongCodec;
import dev.klepto.kweb3.core.contract.ContractCodec;
import dev.klepto.kweb3.core.type.EthBytes;
import dev.klepto.kweb3.core.type.EthValue;
import lombok.val;

import static dev.klepto.kweb3.core.type.EthBytes.bytes;

/**
 * Marks any JVM class as a tuple container. A tuple container is a class that <b>only</b> consists of fields dedicated
 * to decoding ethereum contract return values. Suggested patterns is implementing this interface in a <i>java
 * record</i>, lombok's <i>@Value</i> class or kotlin's <i>data  class</i>.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthTupleContainer extends EthValue {

    /**
     * Converts this tuple container into <code>ethereum bytes</code>.
     *
     * @return ethereum bytes representation of this tuple container
     */
    default EthBytes toBytes() {
        return bytes(toHex());
    }

    /**
     * Encodes this tuple container into a hexadecimal string.
     *
     * @return hexadecimal string representation of this tuple container
     */
    default String toHex() {
        val codec = new HeadlongCodec();
        val tuple = ContractCodec.encodeTupleContainerValue(this);
        return "0x" + codec.encode(tuple);
    }

}
