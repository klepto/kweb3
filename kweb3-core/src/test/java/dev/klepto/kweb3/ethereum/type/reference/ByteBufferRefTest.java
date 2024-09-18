package dev.klepto.kweb3.ethereum.type.reference;

import dev.klepto.kweb3.core.ethereum.type.reference.ByteBufferRef;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;

import static org.junit.jupiter.api.Assertions.*;

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
        return new ByteBufferRef(false, buffer);
    }

    @Test
    public void testBoolean() {
        val refTrue = createRef(1);
        val refFalse = createRef(0);
        assertTrue(refTrue.toBoolean());
        assertFalse(refFalse.toBoolean());
    }

    @Test
    public void testByte() {
        val refA = createRef(Byte.MAX_VALUE);
        val refB = createRef(Byte.MAX_VALUE + 1);
        assertEquals(Byte.MAX_VALUE, refA.toByte());
        assertEquals(0, refB.toByte());
        assertEquals(new BigInteger("128"), refB.toBigInteger());
    }

    @Test
    public void testShort() {
        val refA = createRef(Short.MAX_VALUE);
        val refB = createRef(Short.MAX_VALUE + 1);
        assertEquals(Short.MAX_VALUE, refA.toShort());
        assertEquals(Byte.MAX_VALUE + 1, refB.toShort());
        assertEquals(new BigInteger("32768"), refB.toBigInteger());
    }

    @Test
    public void testInt() {
        val refA = createRef(Integer.MAX_VALUE);
        val refB = createRef(Integer.MAX_VALUE + 1L);
        assertEquals(Integer.MAX_VALUE, refA.toInt());
        assertEquals(8388608, refB.toInt());
        assertEquals(new BigInteger("2147483648"), refB.toBigInteger());
    }

    @Test
    public void testLong() {
        val refA = createRef(Long.MAX_VALUE);
        val refB = createRef(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE));
        assertEquals(Long.MAX_VALUE, refA.toLong());
        assertEquals(36028797018963968L, refB.toLong());
        assertEquals(new BigInteger("9223372036854775808"), refB.toBigInteger());
    }

    @Test
    public void testFloat() {
        val ref = createRef(Float.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, ref.toFloat());
    }

    @Test
    public void testDouble() {
        val ref = createRef(Double.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, ref.toDouble());
    }

    @Test
    public void testHex() {
        val ref = createRef(new BigInteger("1208925819614629174706175"));
        assertEquals("0x00ffffffffffffffffffff", ref.toHex());
    }

    @Test
    public void testBigInteger() {
        val value = new BigInteger("1208925819614629174706175");
        val ref = createRef(value);
        assertEquals(value, ref.toBigInteger());
    }

    @Test
    public void testBigDecimal() {
        val value = new BigInteger("1208925819614629174706175");
        val ref = createRef(value);
        assertEquals(new BigDecimal(value), ref.toBigDecimal());
    }

    @Test
    public void testByteArray() {
        val array = new byte[]{0, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        val ref = createRef(new BigInteger("1208925819614629174706175"));
        assertArrayEquals(array, ref.toByteArray());
    }

    @Test
    public void testPlainString() {
        val ref = createRef(new BigInteger("1208925819614629174706175"));
        assertEquals("1208925819614629174706175", ref.toPlainString());
    }

    @Test
    public void testEmpty() {
        val ref = new ByteBufferRef(false, ByteBuffer.allocate(0));
        assertEquals(0, ref.toByte());
        assertEquals(0, ref.toShort());
        assertEquals(0, ref.toInt());
        assertEquals(0, ref.toLong());
        assertEquals(0, ref.toFloat());
        assertEquals(0, ref.toDouble());
        assertEquals(BigInteger.ZERO, ref.toBigInteger());
        assertEquals(BigDecimal.ZERO, ref.toBigDecimal());
        assertEquals("0x0", ref.toHex());
        assertArrayEquals(new byte[0], ref.toByteArray());
        assertEquals("0", ref.toPlainString());
    }

}
