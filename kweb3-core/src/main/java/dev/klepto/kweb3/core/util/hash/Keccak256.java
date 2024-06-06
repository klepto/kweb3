package dev.klepto.kweb3.core.util.hash;

import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Helper methods for {@link Keccak}-256 message digest.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public final class Keccak256 {

    private Keccak256() {
    }

    /**
     * Applies Keccak-256 algorithm on given input string with default charset and returns hexadecimal string result.
     *
     * @param input the input string
     * @return the resulting hexadecimal string
     */
    @NotNull
    public static String keccak256(@NotNull String input) {
        return keccak256(input, StandardCharsets.US_ASCII);
    }

    /**
     * Applies Keccak-256 algorithm on given input string with specified charset and returns hexadecimal string result.
     *
     * @param input   the input string
     * @param charset the input charset
     * @return the resulting hexadecimal string
     */
    @NotNull
    public static String keccak256(String input, @NotNull Charset charset) {
        return String.format("%064x", new BigInteger(1, keccak256Bytes(input, charset)));
    }

    /**
     * Applies Keccak-256 algorithm on given input string with specified charset and returns byte array result.
     *
     * @param input   the input string
     * @param charset the input charset
     * @return the resulting byte array
     */
    public static byte @NotNull [] keccak256Bytes(@NotNull String input, @NotNull Charset charset) {
        val digest = new Keccak(256);
        val data = input.getBytes(charset);
        val hash = digest.digest(data);
        digest.engineReset();
        return hash;
    }

}
