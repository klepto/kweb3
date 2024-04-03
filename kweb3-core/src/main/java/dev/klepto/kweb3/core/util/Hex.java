package dev.klepto.kweb3.core.util;

import com.google.common.base.CharMatcher;
import com.google.common.io.BaseEncoding;
import lombok.val;
import org.jetbrains.annotations.NotNull;

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
    public static boolean isValid(@NotNull String value) {
        value = stripPrefix(value);
        return value.chars().allMatch(character -> VALID_HEX.indexOf(character) >= 0);
    }

    /**
     * Strips the default <code>0x</code> prefix from hexadecimal string.
     *
     * @param hex a hexadecimal string that may or may not contain <code>0x</code> prefix
     * @return a hexadecimal string without the default prefix
     */
    @NotNull
    public static String stripPrefix(@NotNull String hex) {
        return hex.toLowerCase().replace("0x", "");
    }

    /**
     * Converts <code>byte</code> array to a hexadecimal string with <code>0x</code> prefix and leading zeros.
     *
     * @param value the integer value
     * @return a hexadecimal representation of integer
     */
    @NotNull
    public static String toHex(byte @NotNull [] value) {
        return toHex(value, true, true);
    }

    /**
     * Converts <code>byte</code> array to a hexadecimal string.
     *
     * @param value       the byte array value
     * @param prefix      if true, appends <code>0x</code> prefix to the resulting string
     * @param leadingZero if true, ensures that result length is divisible by 2
     * @return a hexadecimal representation of integer
     */
    @NotNull
    public static String toHex(byte @NotNull [] value, boolean prefix, boolean leadingZero) {
        if (value.length == 0) {
            return prefix ? "0x0" : "0";
        }

        var hex = BaseEncoding.base16().encode(value).toLowerCase();
        if (!leadingZero) {
            hex = CharMatcher.is('0').trimLeadingFrom(hex);
            if (hex.isEmpty()) {
                hex = "0";
            }
        }
        if (prefix) {
            hex = "0x" + hex;
        }
        return hex;
    }

    /**
     * Converts {@link BigInteger} to a hexadecimal string with <code>0x</code> prefix and leading zeros.
     *
     * @param value the integer value
     * @return a hexadecimal representation of integer
     */
    @NotNull
    public static String toHex(@NotNull BigInteger value) {
        return toHex(value, true, true);
    }

    /**
     * Converts {@link BigInteger} to a hexadecimal string.
     *
     * @param value       the integer value
     * @param prefix      if true, appends <code>0x</code> prefix to the resulting string
     * @param leadingZero if true, ensures that result length is divisible by 2
     * @return a hexadecimal representation of integer
     */
    @NotNull
    public static String toHex(@NotNull BigInteger value, boolean prefix, boolean leadingZero) {
        if (value.equals(BigInteger.ZERO)) {
            return prefix ? "0x0" : "0";
        }
        
        var hex = value.toString(16).toLowerCase();
        if (leadingZero && hex.length() % 2 != 0) {
            hex = "0" + hex;
        }
        return (prefix ? "0x" : "") + hex;
    }

    /**
     * Converts given hexadecimal string to {@link BigInteger}.
     *
     * @param hex the hexadecimal string
     * @return a big integer value of hexadecimal string
     */
    @NotNull
    public static BigInteger toBigInteger(@NotNull String hex) {
        val stripped = stripPrefix(hex);
        if (stripped.isEmpty()) {
            return BigInteger.ZERO;
        }

        return new BigInteger(stripPrefix(hex), 16);
    }

    /**
     * Converts given hexadecimal string to <code>byte</code> array.
     *
     * @param hex the hexadecimal string
     * @return a byte array containing hexadecimal string value
     */
    public static byte @NotNull [] toByteArray(@NotNull String hex) {
        val stripped = stripPrefix(hex);
        if (stripped.isEmpty()) {
            return new byte[0];
        }

        val encoding = BaseEncoding.base16().omitPadding();
        return encoding.decode(stripped.toUpperCase());
    }

}
