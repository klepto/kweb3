package dev.klepto.kweb3.type;

import dev.klepto.kweb3.util.function.Numeric;
import dev.klepto.kweb3.util.function.ValueContainer;
import org.jetbrains.annotations.NotNull;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public abstract class NumericType<T extends SolidityType<T, V>, V> extends SolidityType<T, V>
        implements Numeric<T, V>, Comparable<Object>, ValueContainer<V> {

    @Override
    public int compareTo(@NotNull Object value) {
        return toBigDecimal().compareTo(Numeric.toBigDecimal(value));
    }

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
