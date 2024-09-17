package dev.klepto.kweb3.ethereum.type.reference;

import dev.klepto.kweb3.core.ethereum.type.reference.ValueRef;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class HexRefTest {

    @Test
    public void testBoolean() {
        val refTrue = ValueRef.of("0x1");
        val refFalse = ValueRef.of("0x0");
        assertTrue(refTrue.toBoolean());
        assertFalse(refFalse.toBoolean());
    }

    @Test
    public void testByte() {
        val refA = ValueRef.of("0xff");
        val refB = ValueRef.of("0x100");
        assertEquals(-1, refA.toByte());
        assertEquals(0, refB.toByte());
        assertEquals(new BigInteger("256"), refB.toBigInteger());
    }

    @Test
    public void testShort() {
        val refA = ValueRef.of("0xffff");
        val refB = ValueRef.of("0x10000");
        assertEquals(-1, refA.toShort());
        assertEquals(0, refB.toShort());
        assertEquals(new BigInteger("65536"), refB.toBigInteger());
    }

    @Test
    public void testInt() {
        val refA = ValueRef.of("0xffffffff");
        val refB = ValueRef.of("0x100000000");
        assertEquals(-1, refA.toInt());
        assertEquals(0, refB.toInt());
        assertEquals(new BigInteger("4294967296"), refB.toBigInteger());
    }

    @Test
    public void testLong() {
        val refA = ValueRef.of("0xffffffffffffffff");
        val refB = ValueRef.of("0x10000000000000000");
        assertEquals(-1, refA.toLong());
        assertEquals(0, refB.toLong());
        assertEquals(new BigInteger("18446744073709551616"), refB.toBigInteger());
    }

    @Test
    public void testFloat() {
        val ref = ValueRef.of("0xffffffff");
        assertEquals(Integer.MAX_VALUE, ref.toFloat() / 2);
    }

    @Test
    public void testDouble() {
        val ref = ValueRef.of("0xffffffffffffffff");
        assertEquals(Long.MAX_VALUE, ref.toDouble() / 2);
    }

    @Test
    public void testHex() {
        val ref = ValueRef.of("0xffffffffffffffffffff");
        assertEquals("0xffffffffffffffffffff", ref.toHex());
    }

    @Test
    public void testBigInteger() {
        val ref = ValueRef.of("0xffffffffffffffffffff");
        assertEquals(new BigInteger("1208925819614629174706175"), ref.toBigInteger());
    }

    @Test
    public void testBigDecimal() {
        val ref = ValueRef.of("0xffffffffffffffffffff");
        assertEquals(new BigDecimal(new BigInteger("1208925819614629174706175")), ref.toBigDecimal());
    }

    @Test
    public void testByteArray() {
        val array = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        val ref = ValueRef.of("0xffffffffffffffffffff");

        assertArrayEquals(array, ref.toByteArray());
    }

    @Test
    public void testPlainString() {
        val ref = ValueRef.of("0xffffffffffffffffffff");
        assertEquals("1208925819614629174706175", ref.toPlainString());
    }

    @Test
    public void testEmpty() {
        val ref = ValueRef.of("0x");
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
