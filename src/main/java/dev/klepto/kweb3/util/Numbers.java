package dev.klepto.kweb3.util;

import com.google.common.io.BaseEncoding;
import com.google.gson.internal.Primitives;
import dev.klepto.kweb3.type.Bytes;
import dev.klepto.kweb3.type.Uint;
import dev.klepto.kweb3.type.sized.Uint256;
import lombok.val;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Numbers {

    /*
     * Convert any types to BigDecimal.
     */

    public static BigDecimal toBigDecimal(Object object) {
        val type = Primitives.wrap(object.getClass());
        if (type == Float.class) return toBigDecimal((Float) object);
        if (type == Double.class) return toBigDecimal((Double) object);
        if (type == Byte.class) return toBigDecimal((Byte) object);
        if (type == Short.class) return toBigDecimal((Short) object);
        if (type == Integer.class) return toBigDecimal((Integer) object);
        if (type == Long.class) return toBigDecimal((Long) object);
        if (type == BigInteger.class) return toBigDecimal((BigInteger) object);
        if (type == String.class) return toBigDecimal((String) object);
        if (type == byte[].class) return toBigDecimal((byte[]) object);
        if (type == Bytes.class) return toBigDecimal((Bytes) object);
        if (type == Uint.class) return toBigDecimal((Uint) object);
        if (type == BigDecimal.class) return (BigDecimal) object;
        return null;
    }

    public static BigDecimal toBigDecimal(Float value) {
        return toBigDecimal(value.doubleValue());
    }

    public static BigDecimal toBigDecimal(Double value) {
        return BigDecimal.valueOf(value);
    }

    public static BigDecimal toBigDecimal(Byte value) {
        return toBigDecimal(value.longValue());
    }

    public static BigDecimal toBigDecimal(Short value) {
        return toBigDecimal(value.longValue());
    }

    public static BigDecimal toBigDecimal(Integer value) {
        return toBigDecimal(value.longValue());
    }

    public static BigDecimal toBigDecimal(Long value) {
        return BigDecimal.valueOf(value);
    }

    public static BigDecimal toBigDecimal(BigInteger value) {
        return new BigDecimal(value);
    }

    public static BigDecimal toBigDecimal(String value) {
        return toBigDecimal(new BigInteger(stripHexPrefix(value), 16));
    }

    public static BigDecimal toBigDecimal(byte[] value) {
        return toBigDecimal(BaseEncoding.base16().encode(value));
    }

    public static BigDecimal toBigDecimal(Bytes value) {
        return toBigDecimal(value.getValue());
    }

    public static BigDecimal toBigDecimal(Uint value) {
        return toBigDecimal(value.getValue());
    }


    /*
     * Convert BigDecimal to any types.
     */

    public static float toFloat(Object object) {
        return toBigDecimal(object).floatValue();
    }

    public static double toDouble(Object object) {
        return toBigDecimal(object).doubleValue();
    }

    public static byte toByte(Object object) {
        return toBigDecimal(object).byteValueExact();
    }

    public static short toShort(Object object) {
        return toBigDecimal(object).shortValueExact();
    }

    public static int toInt(Object object) {
        return toBigDecimal(object).intValueExact();
    }

    public static long toLong(Object object) {
        return toBigDecimal(object).longValueExact();
    }

    public static BigInteger toBigInteger(Object object) {
        return toBigDecimal(object).toBigIntegerExact();
    }

    public static String toHex(Object value) {
        return "0x" + toBigInteger(value).toString(16);
    }

    public static byte[] toByteArray(Object value) {
        return BaseEncoding.base16().decode(stripHexPrefix(toHex(value)));
    }

    public static Bytes toBytes(Object object) {
        return new Bytes(toByteArray(object));
    }

    public static Uint256 toUint256(Object object) {
        return new Uint256(toBigDecimal(object));
    }

    public static Uint256 toUint(Object object) {
        return toUint256(object);
    }


    /*
     * Utility methods.
     */

    private static String stripHexPrefix(String value) {
        return value.replace("0X", "");
    }

}
