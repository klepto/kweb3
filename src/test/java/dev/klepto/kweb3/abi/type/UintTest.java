package dev.klepto.kweb3.abi.type;

import dev.klepto.kweb3.Web3Error;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class UintTest {

    @Test
    public void testUint() {
        assertEquals(new BigInteger("1"), new Uint(1).getValue());
        assertThrows(Web3Error.class, () -> new Uint(-1));

        val uint8MaxValue = new BigInteger("255");
        assertEquals(new BigInteger("0"), new Uint(0, 8).getMinValue());
        assertEquals(uint8MaxValue, new Uint(0, 8).getMaxValue());
        assertThrows(Web3Error.class, () -> new Uint(uint8MaxValue.add(BigInteger.ONE), 8));

        val uint32MaxValue = new BigInteger("4294967295");
        assertEquals(uint32MaxValue, new Uint(0, 32).getMaxValue());
        assertThrows(Web3Error.class, () -> new Int(uint32MaxValue.add(BigInteger.ONE), 32));
    }

}
