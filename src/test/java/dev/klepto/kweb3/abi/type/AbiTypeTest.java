package dev.klepto.kweb3.abi.type;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class AbiTypeTest {

    @Test
    public void testAbiType() {
        val addressType = new AbiType(Address.class);
        val bytesType = new AbiType(Bytes.class);
        val bytes32Type = new AbiType(Bytes.class, List.of(), false, 32, 0);
        val intType = new AbiType(Int.class, List.of(), false, 256, 0);
        val uintType = new AbiType(Uint.class, List.of(), false, 256, 0);
        val stringType = new AbiType(String.class);
        val booleanType = new AbiType(boolean.class);

        assertEquals("address", addressType.toString());
        assertEquals("bytes", bytesType.toString());
        assertEquals("bytes32", bytes32Type.toString());
        assertEquals("int256", intType.toString());
        assertEquals("uint256", uintType.toString());
        assertEquals("string", stringType.toString());
        assertEquals("bool", booleanType.toString());

        val addressStructType = addressType.wrapTuple();
        assertEquals(Tuple.class, addressStructType.getType());
        assertEquals("(address)", addressStructType.toString());

        val structArrayType = addressStructType.withArray(true);
        assertEquals("(address)[]", structArrayType.toString());
        assertEquals("(address)[10]", structArrayType.withArraySize(10).toString());
    }

}
