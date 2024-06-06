package dev.klepto.kweb3.core.ethereum.type.primitive;

import com.esaulpaugh.headlong.abi.Address;
import dev.klepto.kweb3.core.ethereum.type.EthNumericValue;
import dev.klepto.kweb3.core.ethereum.type.EthValue;
import lombok.With;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;

/**
 * Container for <code>ethereum address</code> value.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public class EthAddress implements EthValue, EthNumericValue<EthAddress> {

    /**
     * Zero address constant.
     */
    public static final EthAddress ZERO = address(0);

    /**
     * Address value.
     */
    @NotNull
    private final Address address;

    /**
     * Checksum address value.
     */
    @NotNull
    private final String checksumAddress;

    /**
     * Constructs new <code>ethereum address</code> from the specified {@link Address}.
     *
     * @param address the address
     */
    private EthAddress(@NotNull Address address, @NotNull String checksumAddress) {
        this.address = address;
        this.checksumAddress = checksumAddress;
    }

    /**
     * Creates a new instance of numeric type with given value.
     *
     * @param value the value to set
     * @return a new instance of numeric type with given value
     */
    @Override
    public @NotNull EthAddress withValue(@NotNull BigInteger value) {
        return address(value);
    }

    /**
     * Returns {@link BigInteger} value that represents this <code>ethereum address</code>.
     *
     * @return the big integer value of this <code>ethereum address</code>
     */
    @Override
    public @NotNull BigInteger value() {
        return address.value();
    }

    /**
     * Returns the checksum hex of this <code>ethereum address</code> with checksum.
     *
     * @return a check-summed hex of this <code>ethereum address</code>
     */
    @Override
    public @NotNull String toHex() {
        return checksumAddress;
    }

    /**
     * Returns string representation of this <code>ethereum address</code>.
     *
     * @return the string representation of this <code>ethereum address</code>.
     */
    @Override
    @NotNull
    public String toString() {
        return "address(" + toHex() + ")";
    }

    /**
     * Returns hash code of this <code>ethereum address</code>.
     *
     * @return hash code of this <code>ethereum address</code>
     */
    @Override
    public int hashCode() {
        return address.hashCode();
    }

    /**
     * Arithmetic equals method for <code>ethereum address</code> values.
     *
     * @param object the object to compare with
     * @return true if the objects have the same value; false otherwise
     */
    public boolean equals(@Nullable Object object) {
        if (object instanceof Number number) {
            return equals(number);
        }
        if (object instanceof EthNumericValue<?> numeric) {
            return equals(numeric.value());
        }
        return false;
    }

    /**
     * Compares this <code>ethereum address</code> to the specified object.
     *
     * @param object the object to compare with
     * @return true if the objects are the same; false otherwise
     */
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
        return checksumAddress.equals(other.checksumAddress);
    }

    /* Solidity style static initializers */
    @NotNull
    public static EthAddress address(@NotNull Number value) {
        val checksumAddress = Address.toChecksumAddress(EthNumericValue.parseBigInteger(value));
        return address(checksumAddress);
    }

    @NotNull
    public static EthAddress address(@NotNull String hex) {
        val address = Address.wrap(Address.toChecksumAddress(hex));
        return new EthAddress(address, address.toString());
    }

}
