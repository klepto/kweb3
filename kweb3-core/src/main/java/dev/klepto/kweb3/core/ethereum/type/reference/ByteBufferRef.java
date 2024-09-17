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

    private final boolean signed;
    private final ByteBuffer value;

    public ByteBufferRef(boolean signed, ByteBuffer value) {
        this.signed = signed;
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
        if (value.remaining() == 0) {
            return 0;
        }
        return value.get(0);
    }

    @Override
    public short toShort() {
        if (value.remaining() == 0) {
            return 0;
        }
        return value.getShort(0);
    }

    @Override
    public int toInt() {
        if (value.remaining() == 0) {
            return 0;
        }
        return value.getInt(0);
    }

    @Override
    public long toLong() {
        if (value.remaining() == 0) {
            return 0;
        }
        return value.getLong(0);
    }

    @Override
    public float toFloat() {
        if (value.remaining() == 0) {
            return 0;
        }
        return (float) toInt();
    }

    @Override
    public double toDouble() {
        if (value.remaining() == 0) {
            return 0;
        }
        return (double) toLong();
    }

    @Override
    public BigInteger toBigInteger() {
        if (value.remaining() == 0) {
            return BigInteger.ZERO;
        }
        return new BigInteger(signed ? -1 : 1, toByteArray());
    }

    @Override
    public BigDecimal toBigDecimal() {
        if (value.remaining() == 0) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(toBigInteger());
    }

    @Override
    public String toHex() {
        if (value.remaining() == 0) {
            return "0x0";
        }
        var hex = toBigInteger().toString(16);
        return "0x" + hex;
    }

    @Override
    public ByteBuffer toByteBuffer() {
        return value;
    }

    @Override
    public byte[] toByteArray() {
        if (value.remaining() == 0) {
            return new byte[0];
        }

        byte[] array = new byte[value.remaining()];
        value.get(array);
        return array;
    }

    @Override
    public String toPlainString() {
        if (value.remaining() == 0) {
            return "0";
        }
        return toBigInteger().toString();
    }
}
