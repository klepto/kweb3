package dev.klepto.kweb3.abi.type.util;

import com.google.common.base.Strings;
import com.google.common.io.BaseEncoding;
import dev.klepto.kweb3.util.Keccak;
import lombok.val;

import java.math.BigInteger;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Hex {

    private Hex() {

    }

    public static String toHex(byte[] value) {
        return "0x" + BaseEncoding.base16().encode(value).toLowerCase();
    }

    public static String toHex(BigInteger value) {
        var hex = value.toString(16);
        if (hex.length() % 2 != 0) {
            hex = "0" + hex;
        }
        return "0x" + hex;
    }

    public static String toChecksumHex(String hex) {
        hex = hex.toLowerCase().replace("0x", "");
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

    public static BigInteger toBigInteger(String hex) {
        return new BigInteger(stripPrefix(hex), 16);
    }

    public static byte[] toByteArray(String hex) {
        val encoding = BaseEncoding.base16().omitPadding();
        return encoding.decode(stripPrefix(hex).toUpperCase());
    }

    public static String stripPrefix(String value) {
        return value.toLowerCase().replace("0x", "");
    }

    public static boolean isHex(String value) {
        return value.startsWith("0x");
    }

}
