package dev.klepto.kweb3.contract;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates that contract function is a "view" function. View functions don't alter the state of the blockchain and are
 * therefor free to execute.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface View {

    String value() default "";

}