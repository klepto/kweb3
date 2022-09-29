package dev.klepto.kweb3.type.convert;

import java.math.BigInteger;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface NumberValue extends BigDecimalValue, BigIntegerValue, IntValue, LongValue, DoubleValue {

    default BigInteger getBigIntegerValue() {
        return getBigDecimalValue().toBigInteger();
    }

    default int getIntValue() {
        return getBigDecimalValue().intValue();
    }

    default long getLongValue() {
        return getBigDecimalValue().longValue();
    }

    default double getDoubleValue() {
        return getBigDecimalValue().doubleValue();
    }

}
