package dev.klepto.kweb3.ethereum.type.reference;

import dev.klepto.kweb3.core.ethereum.type.reference.ByteBufferRef;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link ByteBufferRef}.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class ByteBufferRefTest {

    private static ByteBufferRef createRef(Number value) {
        var bigInteger = BigInteger.valueOf(value.longValue());
        if (value instanceof BigInteger) {
            bigInteger = (BigInteger) value;
        }
        val buffer = ByteBuffer.wrap(bigInteger.toByteArray());
        return new ByteBufferRef(buffer);
    }

    @Test
    public void testByteMaxValue() {
        val refA = createRef(Byte.MAX_VALUE);
        val refB = createRef(Byte.MAX_VALUE + 1);
        assertEquals(Byte.MAX_VALUE, refA.toByte());
        assertEquals(0, refB.toByte());
        assertEquals(new BigInteger("128"), refB.toBigInteger());
    }

    @Test
    public void testShortMaxValue() {
        val refA = createRef(Short.MAX_VALUE);
        val refB = createRef(Short.MAX_VALUE + 1);
        assertEquals(Short.MAX_VALUE, refA.toShort());
        assertEquals(Byte.MAX_VALUE + 1, refB.toShort());
        assertEquals(new BigInteger("32768"), refB.toBigInteger());
    }

    @Test
    public void testIntegerMaxValue() {
        val refA = createRef(Integer.MAX_VALUE);
        val refB = createRef(Integer.MAX_VALUE + 1L);
        assertEquals(Integer.MAX_VALUE, refA.toInt());
        assertEquals(8388608, refB.toInt());
        assertEquals(new BigInteger("2147483648"), refB.toBigInteger());
    }

    @Test
    public void testLongMaxValue() {
        val refA = createRef(Long.MAX_VALUE);
        val refB = createRef(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE));
        assertEquals(Long.MAX_VALUE, refA.toLong());
        assertEquals(36028797018963968L, refB.toLong());
        assertEquals(new BigInteger("9223372036854775808"), refB.toBigInteger());
    }

}
