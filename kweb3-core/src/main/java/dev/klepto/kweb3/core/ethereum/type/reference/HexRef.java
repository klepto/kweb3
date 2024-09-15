package dev.klepto.kweb3.core.ethereum.type.reference;

import dev.klepto.kweb3.core.ethereum.type.EthNumeric;
import dev.klepto.kweb3.core.util.Hex;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * A weak reference to hexadecimal {@link String} representing an {@link EthNumeric} value.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class HexRef implements ValueRef<String> {

    private final String value;
    private final boolean prefix;

    public HexRef(String value) {
        this.value = value;
        this.prefix = value.startsWith("0x");
    }

    @Override
    public String value() {
        if (prefix) {
            return value;
        }
        return "0x" + value;
    }

    public String valueNoPrefix() {
        if (prefix) {
            return value.substring(2);
        }
        return value;
    }

    @Override
    public boolean toBoolean() {
        return Integer.parseInt(valueNoPrefix(), 16) == 1;
    }

    @Override
    public byte toByte() {
        return Byte.parseByte(valueNoPrefix(), 16);
    }

    @Override
    public short toShort() {
        return Short.parseShort(valueNoPrefix(), 16);
    }

    @Override
    public int toInt() {
        return Integer.parseInt(valueNoPrefix(), 16);
    }

    @Override
    public long toLong() {
        return Long.parseLong(valueNoPrefix(), 16);
    }

    @Override
    public float toFloat() {
        return (float) toLong();
    }

    @Override
    public double toDouble() {
        return (double) toLong();
    }

    @Override
    public BigInteger toBigInteger() {
        return Hex.toBigInteger(valueNoPrefix());
    }

    @Override
    public BigDecimal toBigDecimal() {
        return new BigDecimal(toBigInteger());
    }

    @Override
    public String toHexString() {
        return value();
    }

    @Override
    public ByteBuffer toByteBuffer() {
        return ByteBuffer.wrap(toByteArray());
    }

    @Override
    public byte[] toByteArray() {
        return Hex.toByteArray(value);
    }

    @Override
    public String toPlainString() {
        return toBigInteger().toString();
    }
}
