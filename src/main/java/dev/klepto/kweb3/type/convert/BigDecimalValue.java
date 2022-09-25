package dev.klepto.kweb3.type.convert;

import java.math.BigDecimal;

/**
 * Interface for values that can be converted to {@link BigDecimal}.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface BigDecimalValue {

    BigDecimal getBigDecimalValue();

}
