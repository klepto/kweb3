package dev.klepto.kweb3.util;

import lombok.experimental.Delegate;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface Logging {

    StackWalker STACK_WALKER = StackWalker.getInstance(RETAIN_CLASS_REFERENCE);

    static Logger log() {
        return new StaticLogger(STACK_WALKER.getCallerClass());
    }

    static void info(String message, Object... args) {
        val logger = new StaticLogger(STACK_WALKER.getCallerClass());
        logger.info(message, args);
    }

    static void debug(String message, Object... args) {
        val logger = new StaticLogger(STACK_WALKER.getCallerClass());
        logger.debug(message, args);
    }

    static void error(String message, Object... args) {
        val logger = new StaticLogger(STACK_WALKER.getCallerClass());
        logger.error(message, args);
    }

    static void error(String message, Throwable t, Object... args) {
        val logger = new StaticLogger(STACK_WALKER.getCallerClass());
        logger.error(message, t, args);
    }

    class StaticLogger implements Logger {
        @Delegate private final Logger logger;

        public StaticLogger(Class<?> caller) {
            this.logger = LoggerFactory.getLogger(caller);
        }
    }


}
