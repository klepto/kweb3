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
public class IntTest {

    @Test
    public void testInt() {
        assertEquals(new BigInteger("1"), new Int(1).getValue());
        assertEquals(new BigInteger("-1"), new Int(-1).getValue());

        val int8MinValue = new BigInteger("128").negate();
        val int8MaxValue = new BigInteger("127");
        assertEquals(int8MinValue, new Int(0, 8).getMinValue());
        assertEquals(int8MaxValue, new Int(0, 8).getMaxValue());
        assertThrows(Web3Error.class, () -> new Int(int8MinValue.subtract(BigInteger.ONE), 8));
        assertThrows(Web3Error.class, () -> new Int(int8MaxValue.add(BigInteger.ONE), 8));

        val int32MinValue = new BigInteger("2147483648").negate();
        val int32MaxValue = new BigInteger("2147483647");
        assertEquals(int32MinValue, new Int(0, 32).getMinValue());
        assertEquals(int32MaxValue, new Int(0, 32).getMaxValue());
        assertThrows(Web3Error.class, () -> new Int(int32MinValue.subtract(BigInteger.ONE), 32));
        assertThrows(Web3Error.class, () -> new Int(int32MaxValue.add(BigInteger.ONE), 32));
    }

}
