package dev.klepto.kweb3.core.contract.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates that contract function parameter is a cost field. Cost field is the associated payable amount you are
 * sending with the transaction in network's native currency. Only applicable to {@link Transaction} functions.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface Cost {
}
