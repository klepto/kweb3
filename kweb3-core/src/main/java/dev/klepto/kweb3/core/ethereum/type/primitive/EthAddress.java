package dev.klepto.kweb3.core.ethereum.type.primitive;

import com.esaulpaugh.headlong.abi.Address;
import dev.klepto.kweb3.core.ethereum.type.EthNumeric;
import dev.klepto.kweb3.core.ethereum.type.reference.ValueRef;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.ByteBuffer;

/**
 * Container for <code>ethereum address</code> value.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class EthAddress extends EthUint {

    /**
     * Zero address constant.
     */
    public static final EthAddress ZERO = address(0);

    /**
     * Creates a new instance of address with given {@link Number} value.
     *
     * @param number the value
     * @return a new instance of address with given {@link Number} value
     */
    public static EthAddress address(Number number) {
        return new EthAddress(ValueRef.of(number));
    }

    /**
     * Creates a new instance of address with given hexadecimal {@link String} value.
     *
     * @param hex the value
     * @return a new instance of address with given hexadecimal {@link String} value
     */
    public static EthAddress address(String hex) {
        return new EthAddress(ValueRef.of(hex));
    }

    /**
     * Creates a new instance of address with given {@link EthNumeric} value.
     *
     * @param numeric the value
     * @return a new instance of address with given {@link EthNumeric} value
     */
    public static EthAddress address(EthNumeric<?> numeric) {
        return new EthAddress(numeric);
    }

    /**
     * Creates a new instance of address with given {@link ByteBuffer} value.
     *
     * @param buffer the value
     * @return a new instance of address with given {@link ByteBuffer} value
     */
    public static EthAddress address(ByteBuffer buffer) {
        return new EthAddress(ValueRef.of(buffer));
    }

    private EthAddress(ValueRef<?> valueRef) {
        super(valueRef, 160);
    }

    @Override
    public String toHexString() {
        return Address.toChecksumAddress(super.toHexString());
    }

    @Override
    @NotNull
    public String toString() {
        return "address(" + toHexString() + ")";
    }

    @Override
    public int hashCode() {
        return toBigInteger().hashCode();
    }

    @Override
    public boolean equals(@Nullable Object object) {
        if (object instanceof Number number) {
            return equals(number);
        }
        return false;
    }

    public boolean matches(@Nullable Object object) {
        if (object == null) {
            return false;
        }
        if (this == object) {
            return true;
        }
        if (!(object instanceof EthAddress other)) {
            return false;
        }
        return toHexString().equals(other.toHexString());
    }

}
