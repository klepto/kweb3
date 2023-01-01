package dev.klepto.kweb3.abi.type;

import dev.klepto.kweb3.Web3Error;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class BytesTest {

    @Test
    public void testBytes() {
        assertEquals(0, new Bytes(new byte[0]).getSize());
        assertThrows(Web3Error.class, () -> new Bytes(new byte[0], -1));
        assertThrows(Web3Error.class, () -> new Bytes(new byte[33], 33));
        assertThrows(Web3Error.class, () -> new Bytes(new byte[33], 32));
        assertEquals(32, new Bytes(new byte[32], 32).getSize());
    }

}
