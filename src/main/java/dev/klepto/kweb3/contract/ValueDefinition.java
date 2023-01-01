package dev.klepto.kweb3.contract;

import com.google.common.reflect.TypeToken;
import lombok.Value;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Value
public class ValueDefinition {

    TypeToken<?> type;
    Type annotation;
    boolean indexed;

}
