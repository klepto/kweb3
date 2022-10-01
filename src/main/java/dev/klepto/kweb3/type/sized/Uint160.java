package dev.klepto.kweb3.type.sized;

import dev.klepto.kweb3.type.Uint;
import dev.klepto.kweb3.util.Math;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Represents Uint160 solidity type.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Uint160 extends Uint implements Math<Uint160> {

    public Uint160(BigInteger value) {
        super("uint160", value);
    }

    public Uint160(BigDecimal value) {
        this(value.toBigInteger());
    }

}
