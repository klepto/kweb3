package dev.klepto.kweb3.core.util;

import com.esaulpaugh.headlong.util.FastHex;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * Utilities methods for dealing with hexadecimal strings.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public final class Hex {
    private static final byte[] EMPTY_BYTES = new byte[0];
    private static final String PREFIX = "0x";

    private Hex() {
    }

    /**
     * Decodes the specified hexadecimal string to a byte array.
     *
     * @param hex the hexadecimal string, may contain <code>0x</code> prefix
     * @return a byte array containing decoded hexadecimal string
     */
    public static byte @NotNull [] toByteArray(@NotNull String hex) {
        int offset = 0, length = hex.length();
        if (length >= 2 && hex.charAt(0) == '0' && (hex.charAt(1) == 'x' || hex.charAt(1) == 'X')) {
            offset = 2;
            length -= 2;
        }
        if (length == 0) {
            return EMPTY_BYTES;
        }
        // Support "0x0" as a valid hex string
        if (length == 1 && hex.charAt(offset) == '0') {
            return EMPTY_BYTES;
        }
        return FastHex.decode(hex, offset, length);
    }

    /**
     * Converts <code>byte</code> array to a hexadecimal string with <code>0x</code> prefix and leading zeros.
     *
     * @param value the integer value
     * @return a hexadecimal representation of integer
     */
    @NotNull
    public static String toHex(byte @NotNull [] value) {
        return toHex(value, true);
    }

    /**
     * Converts <code>byte</code> array to a hexadecimal string.
     *
     * @param value  the byte array value
     * @param prefix if true, appends <code>0x</code> prefix to the resulting string
     * @return a hexadecimal representation of integer
     */
    @NotNull
    public static String toHex(byte @NotNull [] value, boolean prefix) {
        val result = FastHex.encodeToString(value);
        return prefix ? PREFIX + result : result;
    }

    /**
     * Converts given hexadecimal string to an unsigned {@link BigInteger}.
     *
     * @param hex the hexadecimal string
     * @return a big integer value of hexadecimal string
     */
    @NotNull
    public static BigInteger toUnsignedBigInteger(@NotNull String hex) {
        val decoded = toByteArray(hex);
        return decoded.length == 0 ? BigInteger.ZERO : new BigInteger(1, decoded);
    }

    /**
     * Converts given hexadecimal string to a {@link BigInteger}.
     *
     * @param hex the hexadecimal string
     * @return a big integer value of hexadecimal string
     */
    @NotNull
    public static BigInteger toBigInteger(@NotNull String hex) {
        val decoded = toByteArray(hex);
        return decoded.length == 0 ? BigInteger.ZERO : new BigInteger(decoded);
    }
}
