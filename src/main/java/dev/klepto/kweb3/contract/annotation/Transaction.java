package dev.klepto.kweb3.contract.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates that contract function is a blockchain transaction and therefor requires cryptographic signature and gas
 * fees.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transaction {

    /**
     * The actual function name on the blockchain contract. Used for mapping custom function names.
     */
    String value() default "";

}
