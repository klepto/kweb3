package dev.klepto.kweb3.contract.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates value size of contract function parameter or result. This has to match sizing specified in the contract
 * function on the blockchain. For more information on sizing limits look at specific
 * {@link dev.klepto.kweb3.type.EthType}.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 * @see dev.klepto.kweb3.type.EthInt
 * @see dev.klepto.kweb3.type.EthUint
 * @see dev.klepto.kweb3.type.EthBytes
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
public @interface ValueSize {

    /**
     * The value size of contract function parameter or result.
     */
    int value() default -1;

}
