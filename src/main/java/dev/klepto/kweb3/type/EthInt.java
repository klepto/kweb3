package dev.klepto.kweb3.type;

import lombok.With;

import java.math.BigInteger;

import static dev.klepto.kweb3.error.Conditions.require;

/**
 * Represents ethereum <code>int</code> data type.
 *
 * @param size  the size in bits of this <code>int</code>, from 1 to 256
 * @param value the integer value
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public record EthInt(int size, BigInteger value) implements EthNumericType {

    public EthInt {
        require(size >= value.bitLength(), "int{} cannot fit value: {}", size, value);
    }

    /* Solidity style int initializers */
    public static EthInt int256(BigInteger value) {
        return new EthInt(256, value);
    }

    public static EthInt int32(BigInteger value) {
        return new EthInt(32, value);
    }

    public static EthInt int64(BigInteger value) {
        return new EthInt(64, value);
    }

    public static EthInt int256(long value) {
        return int256(BigInteger.valueOf(value));
    }

    public static EthInt int32(long value) {
        return int32(BigInteger.valueOf(value));
    }

    public static EthInt int64(long value) {
        return int64(BigInteger.valueOf(value));
    }

}
