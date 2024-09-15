package dev.klepto.kweb3.core.ethereum.type.primitive;

import dev.klepto.kweb3.core.ethereum.type.EthValue;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class EthString implements EthValue, CharSequence {

    /**
     * Empty <code>ethereum string</code> constant.
     */
    public static final EthString EMPTY = string("");

    /**
     * Creates a new instance of string with given {@link String} value.
     *
     * @param value the value
     * @return a new instance of string with given {@link String} value
     */
    public static @NotNull EthString string(@NotNull String value) {
        return new EthString(value);
    }

    /**
     * Creates a new instance of string with given {@link Number} value.
     *
     * @param value the value
     * @return a new instance of string with given {@link Number} value
     */
    public static @NotNull EthString string(@NotNull Number value) {
        return string(value.toString());
    }

    @NotNull
    private final @Delegate String value;

    private EthString(@NotNull String value) {
        this.value = value;
    }

    @NotNull
    public String value() {
        return value;
    }

    @Override
    @NotNull
    public String toString() {
        return "string(" + value() + ")";
    }

    @Override
    public int hashCode() {
        return Objects.hash(value());
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!(object instanceof EthString other)) {
            return false;
        }
        return value.equals(other.value);
    }

}
