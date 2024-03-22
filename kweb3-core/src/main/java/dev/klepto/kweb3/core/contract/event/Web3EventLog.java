package dev.klepto.kweb3.core.contract.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker interface for web3 smart contract log containers.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface Web3EventLog {

    /**
     * Annotates the event name that created this log. If empty, the class name is used instead.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE})
    @interface Event {
        String value() default "";
    }

    /**
     * Annotates that a given field in the container is an address field.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface Address {
    }

    /**
     * Annotates that a given field in the container is an indexed field.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    @interface Indexed {
    }

}
