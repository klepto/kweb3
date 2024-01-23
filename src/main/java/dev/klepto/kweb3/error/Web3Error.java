package dev.klepto.kweb3.error;

import org.slf4j.helpers.MessageFormatter;

/**
 * Represents kweb3 runtime error.
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

}
