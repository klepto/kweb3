package dev.klepto.kweb3.core.ethereum.type.reference;

import dev.klepto.kweb3.core.ethereum.type.EthNumeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * A weak reference to {@link ByteBuffer} representing an {@link EthNumeric} value.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class ByteBufferRef implements ValueRef<ByteBuffer> {

    private final ByteBuffer value;

    public ByteBufferRef(ByteBuffer value) {
        this.value = value;
    }

    @Override
    public ByteBuffer value() {
        return value;
    }

    @Override
    public boolean toBoolean() {
        return toByte() == 1;
    }

    @Override
    public byte toByte() {
        return value.get(0);
    }

    @Override
    public short toShort() {
        return value.getShort(0);
    }

    @Override
    public int toInt() {
        return value.getInt(0);
    }

    @Override
    public long toLong() {
        return value.getLong(0);
    }

    @Override
    public float toFloat() {
        return value.getFloat(0);
    }

    @Override
    public double toDouble() {
        return value.getDouble(0);
    }

    @Override
    public BigInteger toBigInteger() {
        return new BigInteger(toByteArray());
    }

    @Override
    public BigDecimal toBigDecimal() {
        return new BigDecimal(toBigInteger());
    }

    @Override
    public String toHexString() {
        var hex = toBigInteger().toString(16);
        return "0x" + hex;
    }

    @Override
    public ByteBuffer toByteBuffer() {
        return value;
    }

    @Override
    public byte[] toByteArray() {
        byte[] array = new byte[value.remaining()];
        value.get(array);
        return array;
    }

    @Override
    public String toPlainString() {
        return toBigInteger().toString();
    }
}
