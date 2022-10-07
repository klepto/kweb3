package dev.klepto.kweb3.type;

import dev.klepto.kweb3.util.number.Numeric;
import lombok.Getter;
;
import java.math.BigInteger;

/**
 * Abstract class for all Uint solidity types.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
public abstract class Uint<T extends Uint<T>> extends NumericType<T, BigInteger> {

    private final BigInteger value;

    public Uint(Object object) {
        this.value = Numeric.toBigInteger(object);
    }

    @Override
    public String toString() {
        return toEther();
    }

}
