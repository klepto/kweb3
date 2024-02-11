package dev.klepto.kweb3.core.util;

import dev.klepto.kweb3.core.Web3Error;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility methods for runtime argument and state checking.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public final class Conditions {

    private Conditions() {
    }

    /**
     * Requires provided expression to be true. Throws a runtime exception otherwise.
     *
     * @param expression a {@code boolean} expression
     * @throws Web3Error if {@code expression} is false
     */
    public static void require(boolean expression) {
        if (!expression) {
            throw new Web3Error();
        }
    }

    /**
     * Requires provided expression to be true. Throws a runtime with a given message otherwise.
     *
     * @param expression a {@code boolean} expression
     * @param message    the message to include with runtime exception
     * @param args       the message arguments to include with runtime exception
     * @throws Web3Error if {@code expression} is false
     */
    public static void require(boolean expression, @NotNull String message, @Nullable Object... args) {
        if (!expression) {
            throw new Web3Error(message, args);
        }
    }

}
