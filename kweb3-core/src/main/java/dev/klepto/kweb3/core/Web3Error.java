package dev.klepto.kweb3.core;

import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.helpers.MessageFormatter;

import java.util.Arrays;

/**
 * Represents kweb3 runtime error. Contains various helper methods for stacktrace manipulation in-order to produce
 * cleaner error messages in-cases such as using {@link java.lang.reflect.Proxy} calls.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Web3Error extends RuntimeException {

    /**
     * Constructs a new runtime error.
     */
    public Web3Error() {
        super();
    }

    /**
     * Constructs a new runtime error with a given cause
     *
     * @param cause the cause of the error
     */
    public Web3Error(@NotNull Throwable cause) {
        super(cause);
    }

    /**
     * Constructs a new runtime error with a given message
     *
     * @param message the message of the error
     * @param args    the message arguments
     */
    public Web3Error(@NotNull String message, @Nullable Object... args) {
        super(message(message, args));
    }

    /**
     * Replaces stack trace class name at a given depth. Used for cleaner exception
     *
     * @param depth the target depth
     * @return this exception instance
     */
    @NotNull
    public Web3Error replaceStackClass(int depth, @NotNull Class<?> newClass) {
        val stackTrace = getStackTrace();
        if (depth >= stackTrace.length) {
            return this;
        }

        val source = stackTrace[depth];
        val stackTraceElement = new StackTraceElement(
                newClass.toString(),
                source.getMethodName(),
                source.getFileName(),
                source.getLineNumber()
        );

        stackTrace[depth] = stackTraceElement;
        setStackTrace(stackTrace);
        return this;
    }

    /**
     * Discards stack trace up to a given depth. Used for cleaning up redundant exception stack traces.
     *
     * @param depth the target depth
     * @return this exception instance
     */
    @NotNull
    public Web3Error discardStack(int depth) {
        val stackTrace = getStackTrace();
        if (depth >= stackTrace.length) {
            return this;
        }

        setStackTrace(Arrays.copyOfRange(stackTrace, depth, stackTrace.length));
        return this;
    }

    /**
     * Formats a message using SLF4J message formatter.
     *
     * @param message the message to format
     * @param args    the message arguments
     * @return the formatted message
     */
    public static String message(String message, Object... args) {
        return MessageFormatter.arrayFormat(message, args).getMessage();
    }

    /**
     * Unwraps the given throwable and throws the original cause, or throws itself it is an instance of Web3Error.
     *
     * @param throwable the throwable to unwrap
     * @return the unwrapped throwable
     */
    public static Throwable unwrap(Throwable throwable) {
        if (throwable instanceof Web3Error) {
            return throwable;
        } else {
            val cause = throwable.getCause();
            if (cause != null) {
                return unwrap(cause);
            }
            return throwable.getCause();
        }
    }

}
