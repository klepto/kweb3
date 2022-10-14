package dev.klepto.kweb3.contract;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Cache {

    long value();

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

    long INDEFINITE = -1;

}
