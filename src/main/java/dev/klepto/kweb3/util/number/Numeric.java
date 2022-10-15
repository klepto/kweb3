package dev.klepto.kweb3.util.number;

import com.google.common.io.BaseEncoding;
import com.google.gson.internal.Primitives;
import dev.klepto.kweb3.contract.Contract;
import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.type.Bytes;
import dev.klepto.kweb3.type.sized.*;
import dev.klepto.kweb3.util.reflection.Creatable;
import lombok.Getter;
import lombok.val;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface Numeric<T, V> extends Valuable<V>, Creatable<T>, Comparable<Object> {

    int DEFAULT_DECIMALS = 18;

    default T add(Object value) {
        val newValue = toBigDecimal().add(toBigDecimal(value));
        return create(newValue);
    }

    default T sub(Object value) {
        val newValue = toBigDecimal().subtract(toBigDecimal(value));
        return create(newValue);
    }

    default T mul(Object value) {
        val newValue = toBigDecimal().multiply(toBigDecimal(value));
        return create(newValue);
    }

    default T div(Object value) {
        val newValue = toBigDecimal().divide(toBigDecimal(value), RoundingMode.FLOOR);
        return create(newValue);
    }

    default T pow(Object value) {
        val newValue = toBigDecimal().pow(toInt(value));
        return create(newValue);
    }

    default boolean moreThan(Object value) {
        return compareTo(value) > 0;
    }

    default boolean moreThanOrEquals(Object value) {
        return compareTo(value) >= 0;
    }

    default boolean lessThan(Object value) {
        return compareTo(value) < 0;
    }

    default boolean lessThanOrEquals(Object value) {
        return compareTo(value) <= 0;
    }

    default boolean equalTo(Object value) {
        return compareTo(value) == 0;
    }

    @Override
    default int compareTo(Object other) {
        return toBigDecimal().compareTo(toBigDecimal(other));
    }

    default Decimal toDecimal() {
        return toDecimal(getValue());
    }

    default float toFloat() {
        return toFloat(getValue());
    }

    default double toDouble() {
        return toDouble(getValue());
    }

    default byte toByte() {
        return toByte(getValue());
    }

    default short toShort() {
        return toShort(getValue());
    }

    default int toInt() {
        return toInt(getValue());
    }

    default long toLong() {
        return toLong(getValue());
    }

    default BigInteger toBigInteger() {
        return toBigInteger(getValue());
    }

    default BigDecimal toBigDecimal() {
        return toBigDecimal(getValue());
    }

    default String toHex() {
        return toHex(getValue());
    }

    default byte[] toByteArray() {
        return toByteArray(getValue());
    }

    default Bytes toBytes() {
        return toBytes(getValue());
    }

    default Uint8 toUint8() {
        return toUint8(getValue());
    }

    default Uint32 toUint32() {
        return toUint32(getValue());
    }

    default Uint112 toUint112() {
        return toUint112(getValue());
    }

    default Uint160 toUint160() {
        return toUint160(getValue());
    }

    default Uint256 toUint256() {
        return toUint256(getValue());
    }

    default Address toAddress() {
        return toAddress(getValue());
    }

    default String toString(int scale) {
        return new BigDecimal(toBigInteger(), scale).toPlainString();
    }

    /*
     * Static convert any type to BigDecimal.
     */

    static BigDecimal toBigDecimal(Object value) {
        val type = Primitives.wrap(value.getClass());
        if (type == Float.class) return toBigDecimal((Float) value);
        if (type == Double.class) return toBigDecimal((Double) value);
        if (type == Byte.class) return toBigDecimal((Byte) value);
        if (type == Short.class) return toBigDecimal((Short) value);
        if (type == Integer.class) return toBigDecimal((Integer) value);
        if (type == Long.class) return toBigDecimal((Long) value);
        if (type == BigInteger.class) return toBigDecimal((BigInteger) value);
        if (type == String.class) return toBigDecimal((String) value);
        if (type == byte[].class) return toBigDecimal((byte[]) value);
        if (type == BigDecimal.class) return (BigDecimal) value;
        if (Numeric.class.isAssignableFrom(type)) return ((Numeric) value).toBigDecimal();
        return null;
    }

    static BigDecimal toBigDecimal(Float value) {
        return toBigDecimal(value.doubleValue());
    }

    static BigDecimal toBigDecimal(Double value) {
        return BigDecimal.valueOf(value);
    }

    static BigDecimal toBigDecimal(Byte value) {
        return toBigDecimal(value.longValue());
    }

    static BigDecimal toBigDecimal(Short value) {
        return toBigDecimal(value.longValue());
    }

    static BigDecimal toBigDecimal(Integer value) {
        return toBigDecimal(value.longValue());
    }

    static BigDecimal toBigDecimal(Long value) {
        return BigDecimal.valueOf(value);
    }

    static BigDecimal toBigDecimal(BigInteger value) {
        return new BigDecimal(value);
    }

    static BigDecimal toBigDecimal(String value) {
        return toBigDecimal(new BigInteger(stripHexPrefix(value), 16));
    }

    static BigDecimal toBigDecimal(byte[] value) {
        return toBigDecimal(BaseEncoding.base16().encode(value));
    }

    static BigDecimal toBigDecimal(Numeric<?, ?> value) {
        return value.toBigDecimal();
    }

    /*
     * Static convert BigDecimal to any type.
     */
    static <T> T to(Class<T> type, Object value) {
        var result = (Object) null;
        val wrappedType = Primitives.wrap(type);
        if (wrappedType == Float.class) result = toFloat(value);
        if (wrappedType == Double.class) result = toDouble(value);
        if (wrappedType == Byte.class) result = toByte(value);
        if (wrappedType == Short.class) result = toShort(value);
        if (wrappedType == Integer.class) result = toInt(value);
        if (wrappedType == Long.class) result = toLong(value);
        if (wrappedType == BigInteger.class) result = toBigInteger(value);
        if (wrappedType == String.class) result = toHex(value);
        if (wrappedType == byte[].class) result = toByteArray(value);
        if (wrappedType == BigDecimal.class) result = toBigDecimal(value);
        return (T) result;
    }

    static Decimal toDecimal(Object value) {
        return new Decimal(toBigDecimal(value).setScale(DEFAULT_DECIMALS));
    }

    static float toFloat(Object value) {
        return toBigDecimal(value).floatValue();
    }

    static double toDouble(Object value) {
        return toBigDecimal(value).doubleValue();
    }

    static byte toByte(Object value) {
        return toBigDecimal(value).byteValue();
    }

    static short toShort(Object value) {
        return toBigDecimal(value).shortValue();
    }

    static int toInt(Object value) {
        return toBigDecimal(value).intValue();
    }

    static long toLong(Object value) {
        return toBigDecimal(value).longValue();
    }

    static BigInteger toBigInteger(Object value) {
        return toBigDecimal(value).toBigInteger();
    }

    static String toHex(Object value) {
        if (value instanceof byte[] || value instanceof Bytes) {
            val bytes = value instanceof Bytes ? ((Bytes) value).getValue() : (byte[]) value;
            return "0x" + BaseEncoding.base16().encode(bytes);
        }

        if (value instanceof String && ((String) value).startsWith("0x")) {
            return (String) value;
        }

        return "0x" + toBigInteger(value).toString(16);
    }

    static byte[] toByteArray(Object value) {
        val hex = value instanceof String ? (String) value : toHex(value);
        val stripped = stripHexPrefix(hex).toUpperCase();
        return BaseEncoding.base16().decode(stripped);
    }

    static Bytes toBytes(Object value) {
        return new Bytes(toByteArray(value));
    }

    static Uint8 toUint8(Object value) {
        return new Uint8(value);
    }

    static Uint32 toUint32(Object value) {
        return new Uint32(value);
    }

    static Uint112 toUint112(Object value) {
        return new Uint112(value);
    }

    static Uint160 toUint160(Object value) {
        return new Uint160(value);
    }

    static Uint256 toUint256(Object value) {
        return new Uint256(value);
    }

    static Address toAddress(Object value) {
        if (value instanceof Contract) {
            return ((Contract) value).getAddress();
        }

        if (value instanceof Address) {
            return ((Address) value);
        }

        return new Address(toHex(value));
    }

    /*
     * Static utility methods.
     */
    private static String stripHexPrefix(String value) {
        return value.toLowerCase().replace("0x", "");
    }

    static Numeric.Decimal numeric(Object value) {
        return toDecimal(value);
    }

    static Uint256 tokens(Object value) {
        return tokens(value, DEFAULT_DECIMALS);
    }

    static Uint256 tokens(Object value, Object decimals) {
        return numeric(value)
                .mul(numeric(10).pow(decimals))
                .toUint256();
    }

    /**
     * Numeric that supports decimal values.
     */
    @Getter
    class Decimal implements Numeric<Decimal, BigDecimal> {
        private final BigDecimal value;

        public Decimal(BigDecimal value) {
            this.value = value;
        }

        @Override
        public Decimal div(Object value) {
            val newValue = this.value.divide(
                    Numeric.toBigDecimal(value),
                    this.value.scale(),
                    RoundingMode.FLOOR
            );
            return new Decimal(newValue);
        }

        @Override
        public String toString() {
            return getValue().toPlainString();
        }
    }


}
