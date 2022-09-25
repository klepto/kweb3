package dev.klepto.kweb3.type;

import dev.klepto.kweb3.util.Keccak;
import lombok.val;

import java.math.BigInteger;

/**
 * Represents Address solidity type.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Address extends Uint160 {

    public Address() {
        this(BigInteger.ZERO);
    }

    public Address(BigInteger value) {
        super(value);
    }

    public Address(String value) {
        this(getBigInteger(value));
    }

    @Override
    public String getEncodedName() {
        return "address";
    }

    @Override
    public String getStringValue() {
        return getChecksum(getBigIntegerValue().toString(16));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Uint) {
            return ((Uint) obj).getValue().equals(getBigIntegerValue());
        }
        if (obj instanceof String) {
            return new Address((String) obj).toString().equals(toString());
        }
        if (obj instanceof BigInteger) {
            return new Address((BigInteger) obj).getBigIntegerValue().equals(getBigIntegerValue());
        }
        return false;
    }

    private static BigInteger getBigInteger(String address) {
        address = address.replace("0x", "");
        return new BigInteger(address, 16);
    }

    private static String getChecksum(String address) {
        address = address.toLowerCase().replace("0x", "");
        val hash = Keccak.hash(address);

        val result = new StringBuilder("0x");
        for (var i = 0; i < address.length(); i++) {
            val character = address.charAt(i) + "";
            val checksum = hash.charAt(i) + "";
            val uppercase = Integer.parseInt(checksum, 16) >= 8;
            result.append(uppercase ? character.toUpperCase() : character);
        }

        return result.toString();
    }

    @Override
    public String toString() {
        return getStringValue();
    }
}