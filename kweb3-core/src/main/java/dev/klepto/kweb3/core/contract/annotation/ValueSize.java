package dev.klepto.kweb3.core.contract.annotation;

import dev.klepto.kweb3.core.ethereum.type.primitive.EthBytes;
import dev.klepto.kweb3.core.ethereum.type.primitive.EthInt;
import dev.klepto.kweb3.core.ethereum.type.primitive.EthUint;
import dev.klepto.kweb3.core.ethereum.type.EthValue;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates value size of contract function parameter or result. This has to match sizing specified in the contract
 * function on the blockchain. For more information on sizing limits look at specific {@link EthValue}.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 * @see EthInt
 * @see EthUint
 * @see EthBytes
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
public @interface ValueSize {

    /**
     * The value size of contract function parameter or result.
     */
    int value() default -1;

}
