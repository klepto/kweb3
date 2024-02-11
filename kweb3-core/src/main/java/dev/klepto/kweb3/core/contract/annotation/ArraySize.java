package dev.klepto.kweb3.core.contract.annotation;

import dev.klepto.kweb3.core.type.EthArray;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates array size of contract function array parameter or result. This has to match capacity of fixed-size array
 * on the blockchain contract.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 * @see EthArray
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
public @interface ArraySize {

    /**
     * The array size of contract function array parameter or result.
     */
    int value() default -1;

}
