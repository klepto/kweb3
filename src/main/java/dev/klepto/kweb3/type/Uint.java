package dev.klepto.kweb3.type;

import dev.klepto.kweb3.type.convert.BigDecimalValue;
import dev.klepto.kweb3.type.convert.BigIntegerValue;
import dev.klepto.kweb3.type.convert.StringValue;
import dev.klepto.kweb3.util.Numbers;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Abstract class for all Uint solidity types.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
public abstract class Uint extends SolidityType implements BigDecimalValue, BigIntegerValue, StringValue {

    private final BigInteger value;

    public Uint(String encodedName, BigInteger value) {
        super(encodedName);
        this.value = value;
    }

    @Override
    public BigInteger getBigIntegerValue() {
        return getValue();
    }

    @Override
    public BigDecimal getBigDecimalValue() {
        return new BigDecimal(getValue());
    }

    @Override
    public String getStringValue() {
        return getBigIntegerValue().toString(16);
    }

    public BigDecimal toEther() {
        return Numbers.toEther(getValue());
    }

    @Override
    public String toString() {
        return "Uint(" + toEther() + ")";
    }

}
