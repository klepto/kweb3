package dev.klepto.kweb3.type;

import lombok.With;

import java.math.BigInteger;

import static dev.klepto.kweb3.error.Conditions.require;

/**
 * Represents ethereum <code>uint</code> data type.
 *
 * @param size  the size in bits of this <code>uint</code>, from 1 to 256
 * @param value the positive integer value
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public record EthUint(int size, BigInteger value) implements EthNumericType {

    public EthUint {
        require(size >= value.bitLength(), "uint{} cannot fit value: {}", size, value);
    }

    /* Solidity style uint initializers */
    public static EthUint uint256(BigInteger value) {
        return new EthUint(256, value);
    }

    public static EthUint uint256(long value) {
        return uint256(BigInteger.valueOf(value));
    }

}
