package dev.klepto.kweb3.util.number;

import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.type.Bytes;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

import static dev.klepto.kweb3.type.Address.address;
import static dev.klepto.kweb3.type.sized.Uint256.uint256;
import static dev.klepto.kweb3.util.number.Numeric.*;
import static org.junit.jupiter.api.Assertions.*;


/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class NumericTest {

    public String stripDecimal(Object value, int scale) {
        return Numeric.toBigDecimal(value).setScale(scale, RoundingMode.FLOOR).toPlainString();
    }

    @Test
    public void testAdd() {
        val uintValue = 10;
        val decimalValue = 5.55;
        val uint = uint256(uintValue);
        val decimal = decimal(decimalValue);

        assertEquals(uint256(uintValue + decimalValue), uint.add(decimal));
        assertEquals(uint256(uintValue + uintValue), uint.add(uint));
        assertEquals(decimalValue + uintValue, decimal.add(uint).toDouble());
        assertEquals(decimalValue + decimalValue, decimal.add(decimal).toDouble());
    }

    @Test
    public void testSub() {
        val uintValue = 10;
        val decimalValue = 5.55;
        val uint = uint256(uintValue);
        val decimal = decimal(decimalValue);

        assertEquals(uint256(uintValue - decimalValue), uint.sub(decimal));
        assertEquals(uint256(0), uint.sub(uint));
        assertEquals(decimalValue - uintValue, decimal.sub(uint).toDouble());
        assertEquals(0, decimal.sub(decimal).toDouble());
    }

    @Test
    public void testMul() {
        val uintValue = 10;
        val decimalValue = 5.55;
        val uint = uint256(uintValue);
        val decimal = decimal(decimalValue);

        assertEquals(uint256(uintValue * decimalValue), uint.mul(decimal));
        assertEquals(uint256(uintValue * uintValue), uint.mul(uint));
        assertEquals(decimalValue * uintValue, decimal.mul(uint).toDouble());
        assertEquals(decimalValue * decimalValue, decimal.mul(decimal).toDouble());
    }

    @Test
    public void testDiv() {
        val uintValue = 10;
        val decimalValue = 5.55;
        val uint = uint256(uintValue);
        val decimal = decimal(decimalValue);

        assertEquals(uint256(uintValue / decimalValue), uint.div(decimal));
        assertEquals(uint256(1), uint.div(uint));
        assertEquals(stripDecimal(0.555, 5), stripDecimal(decimal.div(uint), 5)); // floating-point rounding
        assertEquals(1, decimal.div(decimal).toDouble());
    }

    @Test
    public void testPow() {
        val uint = uint256(10);
        val decimal = decimal(5.55, 0);

        assertEquals(uint256(Math.pow(10, 5)), uint.pow(decimal));
        assertEquals(stripDecimal(Math.pow(5.55, 10), 5), stripDecimal(decimal.pow(uint), 5)); // floating-point rounding
    }

    @Test
    public void testNegate() {
        val uint = uint256(10);
        val decimal = decimal(5.55, 0);

        assertEquals(-10, uint.negate().toInt());
        assertEquals(-5.55, decimal.negate().toDouble());
        assertEquals(10, uint.negate().negate().toInt());
        assertEquals(5.55, decimal.negate().negate().toDouble());
    }

    @Test
    public void testMoreThan() {
        val uint = uint256(10);
        val decimal = decimal(5.55, 0);

        assertTrue(uint.moreThan(decimal));
        assertFalse(decimal.moreThan(uint));
        assertFalse(uint.moreThan(uint));
        assertFalse(decimal.moreThan(decimal));
    }

    @Test
    public void testMoreThanOrEquals() {
        val uint = uint256(10);
        val decimal = decimal(5.55, 0);

        assertTrue(uint.moreThanOrEquals(decimal));
        assertFalse(decimal.moreThanOrEquals(uint));
        assertTrue(uint.moreThanOrEquals(uint));
        assertTrue(decimal.moreThanOrEquals(decimal));
    }

    @Test
    public void testLessThan() {
        val uint = uint256(10);
        val decimal = decimal(5.55, 0);

        assertFalse(uint.lessThan(decimal));
        assertTrue(decimal.lessThan(uint));
        assertFalse(uint.lessThan(uint));
        assertFalse(decimal.lessThan(decimal));
    }

    @Test
    public void testLessThanOrEquals() {
        val uint = uint256(10);
        val decimal = decimal(5.55, 0);

        assertFalse(uint.lessThanOrEquals(decimal));
        assertTrue(decimal.lessThanOrEquals(uint));
        assertTrue(uint.lessThanOrEquals(uint));
        assertTrue(decimal.lessThanOrEquals(decimal));
    }

    @Test
    public void testEqualTo() {
        val uint = uint256(10);
        val decimal = decimal(5.55, 0);

        assertFalse(uint.equalTo(decimal));
        assertFalse(decimal.equalTo(uint));
        assertTrue(decimal.equalTo(decimal));
        assertTrue(uint.equalTo(uint));
    }

    @Test
    public void testCompareTo() {
        val uint = uint256(10);
        val decimal = decimal(5.55, 0);

        assertEquals(1, uint.compareTo(decimal));
        assertEquals(-1, decimal.compareTo(uint));
        assertEquals(0, uint.compareTo(10));
        assertEquals(0, decimal.compareTo(5.55));
    }

    @Test
    public void testToDecimal() {
        val uint = uint256(10);
        val decimal = decimal(10.5);

        assertEquals(new BigDecimal(10), uint.toDecimal().getValue().setScale(0));
        assertEquals(new BigDecimal("10.5"), decimal.getValue().setScale(1));
    }

    @Test
    public void testToFloat() {
        val uint = uint256(10);
        val decimal = decimal(5.55, 0);

        assertEquals(10f, uint.toFloat());
        assertEquals(5.55f, decimal.toFloat());
    }

    @Test
    public void testToDouble() {
        val uint = uint256(10);
        val decimal = decimal(5.55, 0);

        assertEquals(10d, uint.toDouble());
        assertEquals(5.55d, decimal.toDouble());
    }

    @Test
    public void testToByte() {
        val uint = uint256(10);
        val decimal = decimal(5.55, 0);

        assertEquals((byte) 10, uint.toByte());
        assertEquals((byte) 5.55, decimal.toByte());
    }

    @Test
    public void testToShort() {
        val uint = uint256(10);
        val decimal = decimal(5.55, 0);

        assertEquals((short) 10, uint.toShort());
        assertEquals((short) 5.55, decimal.toShort());
    }

    @Test
    public void testToInt() {
        val uint = uint256(10);
        val decimal = decimal(5.55, 0);

        assertEquals(10, uint.toInt());
        assertEquals((int) 5.55, decimal.toInt());
    }

    @Test
    public void testToLong() {
        val uint = uint256(10);
        val decimal = decimal(5.55, 0);

        assertEquals((long) 10, uint.toLong());
        assertEquals((long) 5.55, decimal.toLong());
    }

    @Test
    public void testToBigInteger() {
        val uint = uint256(10);
        val decimal = decimal(5.55, 0);

        assertEquals(BigInteger.valueOf(10), uint.toBigInteger());
        assertEquals(BigInteger.valueOf((long) 5.55), decimal.toBigInteger());
    }

    @Test
    public void testToBigDecimal() {
        val ten = new BigDecimal(10);
        val fivePointFiveFive = new BigDecimal("5.55");

        assertEquals(ten, uint256(10).toBigDecimal());
        assertEquals(fivePointFiveFive.setScale(Numeric.MINIMUM_DECIMALS), decimal(5.55).toBigDecimal());

        assertEquals(fivePointFiveFive, toBigDecimal((Object) 5.55f));
        assertEquals(fivePointFiveFive, toBigDecimal((Object) 5.55d));
        assertEquals(ten, toBigDecimal((Object) (byte) 10));
        assertEquals(ten, toBigDecimal((Object) (short) 10));
        assertEquals(ten, toBigDecimal((Object) 10));
        assertEquals(ten, toBigDecimal((Object) (long) 10));
        assertEquals(ten, toBigDecimal((Object) BigInteger.valueOf(10)));
        assertEquals(ten, toBigDecimal((Object) "0x0a"));
        assertEquals(ten, toBigDecimal((Object) new byte[]{10}));
        assertEquals(ten, toBigDecimal((Object) ten));
        assertEquals(ten, toBigDecimal((Object) uint256(10)));
        assertNull(toBigDecimal(new Object()));
    }

    @Test
    public void testToHex() {
        assertEquals("0x00", uint256(0).toHex());
        assertEquals("0x0a", uint256(10).toHex());
        assertEquals("0x075bcd15", uint256(123456789).toHex());

        assertEquals("0x0a", Numeric.toHex(new byte[]{10}));
        assertEquals("0x0a", Numeric.toHex(new Bytes(new byte[]{10})));
        assertEquals("0x0a", Numeric.toHex("0x0a"));
    }

    @Test
    public void testToByteArray() {
        assertArrayEquals(new byte[]{0}, uint256(0).toByteArray());
        assertArrayEquals(new byte[]{10}, uint256(10).toByteArray());
        assertArrayEquals(new byte[]{7, 91, -51, 21}, uint256(123456789).toByteArray());
    }

    @Test
    public void testToBytes() {
        val data = new byte[]{7, 91, -51, 21};
        assertArrayEquals(data, uint256(123456789).toBytes().getValue());
    }

    @Test
    public void testToUint() {
        val value = 123456789;
        val bigInteger = BigInteger.valueOf(value);

        assertEquals(bigInteger, decimal(value).toUint8().getValue());
        assertEquals(bigInteger, decimal(value).toUint32().getValue());
        assertEquals(bigInteger, decimal(value).toUint112().getValue());
        assertEquals(bigInteger, decimal(value).toUint160().getValue());
        assertEquals(bigInteger, decimal(value).toUint256().getValue());
        assertEquals(BigInteger.valueOf(5), decimal(5.5).toUint256().getValue());
    }

    @Test
    public void testToAddress() {
        val address = address(123456789);
        val hex = "0x00000000000000000000000000000000075BCd15";
        assertEquals(new Address(0), decimal(0).toAddress());
        assertEquals(address, decimal(123456789).toAddress());
        assertEquals(address, Numeric.toAddress(hex));
        assertEquals(address, Numeric.toAddress(address));
    }

    @Test
    public void testToString() {
        val value = decimal(123456789);

        assertEquals("123456789.000000000000000000", value.toString());
        assertEquals("123456789", value.toString(0));
        assertEquals("123456.789", value.toString(3));
    }

    @Test
    public void testTo() {
        assertEquals(123456.789f, Numeric.to(Float.class, 123456.789));
        assertEquals(123456.789d, Numeric.to(Double.class, 123456.789));
        assertEquals((byte) 12, Numeric.to(Byte.class, 12));
        assertEquals((short) 1234, Numeric.to(Short.class, 1234));
        assertEquals(1234567, Numeric.to(Integer.class, 1234567));
        assertEquals(123456789111213L, Numeric.to(Long.class, 123456789111213L));
        assertEquals(BigInteger.valueOf(123456789111213L), Numeric.to(BigInteger.class, 123456789111213L));
        assertEquals("0x075bcd15", Numeric.to(String.class, 123456789));
        assertArrayEquals(new byte[]{7, 91, -51, 21}, Numeric.to(byte[].class, 123456789));
        assertEquals(new BigDecimal("123456.789"), Numeric.to(BigDecimal.class, 123456.789));
    }

    @Test
    public void testDecimal() {
        assertEquals(Numeric.MINIMUM_DECIMALS, decimal(0).getValue().scale());
        assertEquals(new BigDecimal("123.000000000000000000"), decimal(123).getValue());
        assertEquals(new BigDecimal("12.300000000000000000"), decimal(123, 1).getValue());
        assertEquals(new BigDecimal("1230.000000000000000000"), decimal(123, -1).getValue());
    }

    @Test
    public void testTokens() {
        assertEquals(BigInteger.TEN.pow(Numeric.MINIMUM_DECIMALS), tokens(1).getValue());
        assertEquals(BigInteger.TEN.pow(5), tokens(1, 5).getValue());
        assertEquals(BigInteger.TEN.pow(5).multiply(BigInteger.valueOf(123)), tokens(123, 5).getValue());
    }

}
