package dev.klepto.kweb3;


import org.slf4j.helpers.MessageFormatter;

/**
 * Represents web3 error. Contains utility methods such as {@link Web3Error#require(boolean, String, Object...)} and
 * {@link Web3Error#error(String, Object...)} in-order to easier invoke errors within kweb3 API.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Web3Error extends RuntimeException {

    public Web3Error() {
        super();
    }

    public Web3Error(String message, Object... args) {
        super(MessageFormatter.arrayFormat(message, args).getMessage());
    }

    public static void require(boolean expression, String message, Object... args) {
        if (!expression) {
            error(message, args);
        }
    }

    public static void error(String message, Object... args) {
        throw new Web3Error(message, args);
    }

}