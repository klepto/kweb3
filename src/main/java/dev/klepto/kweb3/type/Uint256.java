package dev.klepto.kweb3.type;

import dev.klepto.kweb3.util.Math;
import dev.klepto.kweb3.util.Numbers;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Represents Uint256 solidity type.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Uint256 extends Uint implements Math<Uint256> {

    public Uint256() {
        this(BigInteger.ZERO);
    }

    public Uint256(BigInteger value) {
        super("uint256", value);
    }

    public Uint256(BigDecimal value) {
        this(value.toBigInteger());
    }

    public static Uint256 uint256(BigInteger value) {
        return new Uint256(value);
    }

    public static Uint256 uint256(long value) {
        return uint256(BigInteger.valueOf(value));
    }

    public static Uint256 ether(double value) {
        return uint256(Numbers.toWei(value));
    }

    public static final Uint256 ZERO = uint256(BigInteger.ZERO);

}