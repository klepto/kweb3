package dev.klepto.kweb3.core.ethereum.type.primitive;

import dev.klepto.kweb3.core.ethereum.type.EthNumeric;
import dev.klepto.kweb3.core.ethereum.type.reference.ValueRef;
import lombok.experimental.Delegate;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * Container for <code>ethereum uint</code> value.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class EthUint extends Number implements EthNumeric<EthUint>, EthNumeric.Math<EthUint> {

    /**
     * Maximum bit size for ethereum <code>uint</code> type.
     */
    public static final int MAX_BIT_SIZE = 256;

    /**
     * Zero <code>uint</code> constant.
     */
    public static final EthUint ZERO = uint256(0);

    /**
     * One <code>uint</code> constant.
     */
    public static final EthUint ONE = uint256(1);

    /**
     * Two <code>uint</code> constant.
     */
    public static final EthUint TWO = uint256(2);

    /**
     * Ten <code>uint</code> constant.
     */
    public static final EthUint TEN = uint256(10);

    /**
     * Creates a new instance of uint256 with given {@link Number} value.
     *
     * @param number the value
     * @return a new instance of uint256 with given {@link Number} value
     */
    public static @NotNull EthUint uint256(@NotNull Number number) {
        return new EthUint(ValueRef.of(number), MAX_BIT_SIZE);
    }

    /**
     * Creates a new instance of uint256 with given hexadecimal {@link String} value.
     *
     * @param hex the value
     * @return a new instance of uint256 with given hexadecimal {@link String} value
     */
    public static @NotNull EthUint uint256(@NotNull String hex) {
        return new EthUint(ValueRef.of(hex), MAX_BIT_SIZE);
    }

    /**
     * Creates a new instance of uint256 with given {@link EthNumeric} value.
     *
     * @param numeric the value
     * @return a new instance of uint256 with given {@link EthNumeric} value
     */
    public static @NotNull EthUint uint256(@NotNull EthNumeric<?> numeric) {
        return new EthUint(numeric, MAX_BIT_SIZE);
    }

    /**
     * Creates a new instance of uint256 with given {@link ByteBuffer} value.
     *
     * @param buffer the value
     * @return a new instance of uint256 with given {@link ByteBuffer} value
     */
    public static @NotNull EthUint uint256(@NotNull ByteBuffer buffer) {
        return new EthUint(ValueRef.of(buffer), MAX_BIT_SIZE);
    }

    @Delegate
    @NotNull
    private final ValueRef<?> valueRef;
    private final int bitSize;

    protected EthUint(@NotNull ValueRef<?> valueRef, int bitSize) {
        this.valueRef = valueRef;
        this.bitSize = bitSize;
    }

    @Override
    public InvalidValue validate() {
        val value = toBigInteger();
        if (value.bitLength() > bitSize || value.compareTo(BigInteger.ZERO) < 0) {
            return InvalidValue.OUT_OF_RANGE;
        }
        if (bitSize > MAX_BIT_SIZE || bitSize < 1) {
            return InvalidValue.UNSUPPORTED_SIZE;
        }
        return null;
    }

    @Override
    public EthUint size(int bitSize) {
        return new EthUint(valueRef, bitSize);
    }

    @Override
    public @NotNull EthUint value(@NotNull ValueRef<?> valueRef) {
        return new EthUint(valueRef, bitSize);
    }

    @Override
    public int size() {
        return bitSize;
    }

    @Override
    public int bitSize() {
        return bitSize;
    }

    @Override
    public String toString() {
        return "uint" + bitSize + "(" + toPlainString() + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(bitSize, toBigInteger());
    }

    public boolean equals(@Nullable Object object) {
        if (object instanceof Number number) {
            return equals(number);
        }
        return false;
    }

    public boolean matches(Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!(object instanceof EthUint other)) {
            return false;
        }
        return bitSize == other.bitSize && toBigInteger().equals(other.toBigInteger());
    }

    /* Required by inheriting Number */
    @Override
    public int intValue() {
        return toInt();
    }

    // Required for Number implementation.
    @Override
    public long longValue() {
        return toLong();
    }

    // Required for Number implementation.
    @Override
    public float floatValue() {
        return toFloat();
    }

    // Required for Number implementation.
    @Override
    public double doubleValue() {
        return toDouble();
    }

    // Kotlin operator function, cannot be inherited from an interface.
    public EthUint inc() {
        return plus(1);
    }

    // Kotlin operator function, cannot be inherited from an interface.
    public EthUint dec() {
        return minus(1);
    }

}
