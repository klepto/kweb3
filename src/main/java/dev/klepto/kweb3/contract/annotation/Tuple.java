package dev.klepto.kweb3.contract.annotation;

import dev.klepto.kweb3.contract.ContractExecutor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates a contract function return type as a tuple type. This tells {@link ContractExecutor} to not attempt to
 * decode it as struct, but rather as a multiple return values.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Tuple {

}
