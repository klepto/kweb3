package dev.klepto.kweb3.contract.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a tuple parameter, field or result. This indicates to contract executor that even though, the type isn't
 * recognized by the API, it contains fields that will hold values for encoding or decoding of the contract call.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.FIELD})
public @interface Tuple {
}
