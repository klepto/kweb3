package dev.klepto.kweb3.core.ethereum.type.reference;

import dev.klepto.kweb3.core.ethereum.type.EthNumeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * A weak reference to {@link Number} representing an {@link EthNumeric}.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class NumberRef implements ValueRef<Number> {

    private final Number value;

    public NumberRef(Number value) {
        this.value = value;
    }

    @Override
    public Number value() {
        return value;
    }

    @Override
    public boolean toBoolean() {
        return toByte() == 1;
    }

    @Override
    public byte toByte() {
        if (value instanceof Byte) {
            return (Byte) value;
        }
        return value().byteValue();
    }

    @Override
    public short toShort() {
        if (value instanceof Short) {
            return (Short) value;
        }
        return value().shortValue();
    }

    @Override
    public int toInt() {
        if (value instanceof Integer) {
            return (Integer) value;
        }
        return value().intValue();
    }

    @Override
    public long toLong() {
        if (value instanceof Long) {
            return (Long) value;
        }
        return value().longValue();
    }

    @Override
    public float toFloat() {
        if (value instanceof Float) {
            return (Float) value;
        }
        return value().floatValue();
    }

    @Override
    public double toDouble() {
        if (value instanceof Double) {
            return (Double) value;
        }
        return value().doubleValue();
    }

    @Override
    public BigInteger toBigInteger() {
        if (value instanceof BigInteger) {
            return (BigInteger) value;
        }
        return BigInteger.valueOf(toLong());
    }

    @Override
    public BigDecimal toBigDecimal() {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Double) {
            return BigDecimal.valueOf(toDouble());
        }
        return new BigDecimal(toBigInteger());
    }

    @Override
    public String toHexString() {
        var hex = toBigInteger().toString(16);
        return "0x" + hex;
    }

    @Override
    public ByteBuffer toByteBuffer() {
        return ByteBuffer.wrap(toByteArray());
    }

    @Override
    public byte[] toByteArray() {
        return toBigInteger().toByteArray();
    }

    @Override
    public String toPlainString() {
        return toBigDecimal().toPlainString();
    }
}
