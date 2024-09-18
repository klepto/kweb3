package dev.klepto.kweb3.core.ethereum.type.reference;

import dev.klepto.kweb3.core.ethereum.type.EthNumeric;
import dev.klepto.kweb3.core.util.Hex;

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
        return value().byteValue();
    }

    @Override
    public short toShort() {
        return value().shortValue();
    }

    @Override
    public int toInt() {
        return value().intValue();
    }

    @Override
    public long toLong() {
        return value().longValue();
    }

    @Override
    public float toFloat() {
        return value().floatValue();
    }

    @Override
    public double toDouble() {
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
    public String toHex() {
        return Hex.toHex(toByteArray());
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
