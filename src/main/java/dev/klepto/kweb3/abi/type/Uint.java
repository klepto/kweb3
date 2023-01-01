package dev.klepto.kweb3.abi.type;

import dev.klepto.kweb3.abi.type.util.Convertibles;
import dev.klepto.kweb3.abi.type.util.Numeric;
import lombok.Getter;
import lombok.val;

import java.math.BigInteger;

import static dev.klepto.kweb3.Web3Error.require;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
public class Uint extends AbiNumericValue<Uint, BigInteger> implements Numeric<Uint> {

    public Uint(Object value) {
        this(value, 0);
    }

    public Uint(Object value, int size) {
        super(Convertibles.toBigInteger(value), size == 0 ? MAX_SIZE : size);
    }

    @Override
    public Uint withValue(Object value) {
        return new Uint(value, getSize());
    }

    @Override
    public Uint withSize(int size) {
        return new Uint(getValue(), size);
    }

    @Override
    public BigInteger getMaxValue() {
        return BigInteger.valueOf(2).pow(getSize()).subtract(BigInteger.ONE);
    }

    @Override
    public BigInteger getMinValue() {
        return BigInteger.ZERO;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Uint && equalTo(object);
    }

    @Override
    public String toString() {
        return "uint" + getSize() + "(" + getValue() + ")";
    }

}
