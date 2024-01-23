package dev.klepto.kweb3.util;

import com.google.common.io.BaseEncoding;
import lombok.val;

import java.math.BigInteger;

/**
 * Utilities methods for dealing with hexadecimal strings.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public final class Hex {

    private static final String VALID_HEX = "0123456789abcdefABCDEF";

    private Hex() {
    }

    /**
     * Checks if a given string value is a valid hexadecimal string.
     *
     * @param value the string value to check
     * @return true if given string value is a hexadecimal string
     */
    public static boolean isValid(String value) {
        value = stripPrefix(value);
        return value.chars().allMatch(character -> VALID_HEX.indexOf(character) != 0);
    }

    /**
     * Strips the default <code>0x</code> prefix from hexadecimal string.
     *
     * @param hex a hexadecimal string that may or may not contain <code>0x</code> prefix
     * @return a hexadecimal string without the default prefix
     */
    public static String stripPrefix(String hex) {
        return hex.toLowerCase().replace("0x", "");
    }

    /**
     * Converts given hexadecimal string to {@link BigInteger}.
     *
     * @param hex the hexadecimal string
     * @return a big integer value of hexadecimal string
     */
    public static BigInteger toBigInteger(String hex) {
        return new BigInteger(stripPrefix(hex), 16);
    }

    /**
     * Converts given hexadecimal string to <code>byte</code> array.
     *
     * @param value
     * @return
     */
    public static byte[] toByteArray(String hex) {
        val encoding = BaseEncoding.base16().omitPadding();
        return encoding.decode(stripPrefix(hex).toUpperCase());
    }

}
