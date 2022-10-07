package dev.klepto.kweb3.type;

import com.google.common.base.Strings;
import dev.klepto.kweb3.type.sized.Uint160;
import dev.klepto.kweb3.util.Keccak;
import lombok.val;

/**
 * Represents Address solidity type.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Address extends Uint160 {

    public Address(Object value) {
        super(value);
    }

    public static Address address(Object value) {
        return new Address(value);
    }

    @Override
    public String toHex() {
        val hex = super.toHex().toLowerCase().replace("0x", "");
        val hash = Keccak.hash(hex);

        val result = new StringBuilder();
        for (var i = 0; i < hex.length(); i++) {
            val character = hex.charAt(i) + "";
            val checksum = hash.charAt(i) + "";
            val uppercase = Integer.parseInt(checksum, 16) >= 8;
            result.append(uppercase ? character.toUpperCase() : character);
        }

        return "0x" + Strings.padStart(result.toString(), 40, '0');
    }

    @Override
    public String toString() {
        return toHex();
    }

}