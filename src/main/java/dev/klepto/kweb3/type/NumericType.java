package dev.klepto.kweb3.type;

import dev.klepto.kweb3.util.number.Numeric;
import dev.klepto.kweb3.util.number.Valuable;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public abstract class NumericType<T extends SolidityType<T, V>, V> extends SolidityType<T, V>
        implements Numeric<T, V>, Valuable<V> {

    @Override
    public boolean equals(Object value) {
        try {
            return compareTo(value) == 0;
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public String toString() {
        return toHex();
    }

}
