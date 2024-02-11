package dev.klepto.kweb3;

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

    public Web3Error() {
        super();
    }

    public Web3Error(@NotNull Throwable cause) {
        super(cause);
    }

    public Web3Error(@NotNull String message, @Nullable Object... args) {
        super(MessageFormatter.arrayFormat(message, args).getMessage());
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

}
