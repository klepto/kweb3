package dev.klepto.kweb3.contract;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates that contract function is transaction and therefor has associated gas fees with it.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Transaction {

    String value() default "";

}
