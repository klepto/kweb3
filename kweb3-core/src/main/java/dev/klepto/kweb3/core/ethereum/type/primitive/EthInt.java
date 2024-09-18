package dev.klepto.kweb3.core.ethereum.type.primitive;

import dev.klepto.kweb3.core.ethereum.type.EthNumeric;
import dev.klepto.kweb3.core.ethereum.type.reference.ValueRef;
import lombok.experimental.Delegate;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * Container for <code>ethereum int</code> value.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class EthInt extends Number implements EthNumeric<EthInt>, EthNumeric.Math<EthInt> {

    /**
     * Maximum bit size for ethereum <code>int</code> type.
     */
    public static final int MAX_BIT_SIZE = 256;

    /**
     * Zero <code>int</code> constant.
     */
    public static final EthInt ZERO = int256(0);

    /**
     * One <code>int</code> constant.
     */
    public static final EthInt ONE = int256(1);

    /**
     * Two <code>int</code> constant.
     */
    public static final EthInt TWO = int256(2);

    /**
     * Ten <code>int</code> constant.
     */
    public static final EthInt TEN = int256(10);

    /**
     * Creates a new instance of int256 with given {@link Number} value.
     *
     * @param number the value
     * @return a new instance of int256 with given {@link Number} value
     */
    public static @NotNull EthInt int256(@NotNull Number number) {
        return new EthInt(ValueRef.of(number), MAX_BIT_SIZE);
    }

    /**
     * Creates a new instance of int256 with given {@link ValueRef} value.
     *
     * @param ref the value
     * @return a new instance of int256 with given {@link ValueRef} value
     */
    public static @NotNull EthInt int256(@NotNull ValueRef<?> ref) {
        return new EthInt(ref, MAX_BIT_SIZE);
    }

    /**
     * Creates a new instance of int256 with given hexadecimal {@link String} value.
     *
     * @param hex the value
     * @return a new instance of int256 with given hexadecimal {@link String} value
     */
    public static @NotNull EthInt int256(@NotNull String hex) {
        return new EthInt(ValueRef.of(true, hex), MAX_BIT_SIZE);
    }

    /**
     * Creates a new instance of int256 with given {@code byte array} value.
     *
     * @param bytes the value
     * @return a new instance of int256 with given {@code byte array} value
     */
    public static @NotNull EthInt int256(byte @NotNull [] bytes) {
        return new EthInt(ValueRef.of(true, bytes), MAX_BIT_SIZE);
    }

    /**
     * Creates a new instance of int256 with given {@link ByteBuffer} value.
     *
     * @param buffer the value
     * @return a new instance of int256 with given {@link ByteBuffer} value
     */
    public static @NotNull EthInt int256(@NotNull ByteBuffer buffer) {
        return new EthInt(ValueRef.of(true, buffer), MAX_BIT_SIZE);
    }

    @Delegate
    @NotNull
    private final ValueRef<?> valueRef;
    private final int bitSize;

    private EthInt(@NotNull ValueRef<?> valueRef, int bitSize) {
        this.valueRef = valueRef;
        this.bitSize = bitSize;
    }

    @Override
    public InvalidValue validate() {
        val value = toBigInteger();
        if (value.bitLength() > bitSize) {
            return InvalidValue.OUT_OF_RANGE;
        }
        if (bitSize > MAX_BIT_SIZE || bitSize < 1) {
            return InvalidValue.UNSUPPORTED_SIZE;
        }
        return null;
    }

    @Override
    public EthInt size(int bitSize) {
        return new EthInt(valueRef, bitSize);
    }

    @Override
    public @NotNull EthInt value(@NotNull ValueRef<?> valueRef) {
        return new EthInt(valueRef, bitSize);
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
    @NotNull
    public String toString() {
        return "int" + bitSize + "(" + toPlainString() + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(bitSize, toBigInteger());
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (object instanceof Number number) {
            return equals(number);
        }
        return false;
    }

    public boolean matches(@Nullable Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!(object instanceof EthInt other)) {
            return false;
        }
        return bitSize == other.bitSize && toBigInteger().equals(other.toBigInteger());
    }

    // Required for Number implementation.
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
    public EthInt inc() {
        return plus(1);
    }

    // Kotlin operator function, cannot be inherited from an interface.
    public EthInt dec() {
        return minus(1);
    }

}
