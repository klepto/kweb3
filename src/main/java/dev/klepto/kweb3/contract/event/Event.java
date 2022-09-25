package dev.klepto.kweb3.contract.event;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotates that a given class is an Event container.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Event {

    String value() default "";

}