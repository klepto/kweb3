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
public class Int extends AbiNumericValue<Int, BigInteger> implements Numeric<Int> {

    public Int(Object value) {
        this(value, 0);
    }

    public Int(Object value, int size) {
        super(
                Convertibles.toBigInteger(value),
                size == 0 ? MAX_SIZE : size
        );
    }

    @Override
    public Int withValue(Object value) {
        return new Int(value, getSize());
    }

    @Override
    public Int withSize(int size) {
        return new Int(getValue(), size);
    }

    @Override
    public BigInteger getMinValue() {
        return BigInteger.valueOf(-2).pow(getSize() - 1);
    }

    @Override
    public BigInteger getMaxValue() {
        return BigInteger.valueOf(2).pow(getSize() - 1).subtract(BigInteger.ONE);
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof Int && equalTo(object);
    }

    @Override
    public String toString() {
        return "int" + getSize() + "(" + getValue() + ")";
    }

}
