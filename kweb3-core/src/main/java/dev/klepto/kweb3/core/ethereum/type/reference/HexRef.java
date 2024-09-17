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

    private static final String PREFIX = "0x";

    private final String value;

    public HexRef(String value) {
        if (value.startsWith(PREFIX)) {
            this.value = value;
        } else {
            this.value = PREFIX + value;
        }
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public boolean toBoolean() {
        return toByte() == 1;
    }

    @Override
    public byte toByte() {
        if (value.equals(PREFIX)) {
            return 0;
        }
        return toBigInteger().byteValue();
    }

    @Override
    public short toShort() {
        if (value.equals(PREFIX)) {
            return 0;
        }
        return toBigInteger().shortValue();
    }

    @Override
    public int toInt() {
        if (value.equals(PREFIX)) {
            return 0;
        }
        return toBigInteger().intValue();
    }

    @Override
    public long toLong() {
        if (value.equals(PREFIX)) {
            return 0;
        }
        return toBigInteger().longValue();
    }

    @Override
    public float toFloat() {
        if (value.equals(PREFIX)) {
            return 0;
        }
        return toBigInteger().floatValue();
    }

    @Override
    public double toDouble() {
        if (value.equals(PREFIX)) {
            return 0;
        }
        return toBigInteger().doubleValue();
    }

    @Override
    public BigInteger toBigInteger() {
        if (value.equals(PREFIX)) {
            return BigInteger.ZERO;
        }
        return new BigInteger(1, toByteArray());
    }

    @Override
    public BigDecimal toBigDecimal() {
        if (value.equals(PREFIX)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(toBigInteger());
    }

    @Override
    public String toHex() {
        if (value.equals(PREFIX)) {
            return "0x0";
        }
        return value();
    }

    @Override
    public ByteBuffer toByteBuffer() {
        if (value.equals(PREFIX)) {
            return ByteBuffer.allocate(0);
        }
        return ByteBuffer.wrap(toByteArray());
    }

    @Override
    public byte[] toByteArray() {
        if (value.equals(PREFIX)) {
            return new byte[0];
        }
        return Hex.toByteArray(value);
    }

    @Override
    public String toPlainString() {
        if (value.equals(PREFIX)) {
            return "0";
        }
        return toBigInteger().toString();
    }
}
