package dev.klepto.kweb3.contract.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates array size of contract function array parameter or result. This has to match capacity of fixed-size array
 * on the blockchain contract.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 * @see dev.klepto.kweb3.type.EthArray
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD})
public @interface ArraySize {

    /**
     * The array size of contract function array parameter or result.
     */
    int value() default -1;

}
