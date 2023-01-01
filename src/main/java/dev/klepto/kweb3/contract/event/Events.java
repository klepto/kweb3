package dev.klepto.kweb3.contract.event;

import lombok.Value;
import lombok.experimental.Delegate;


import java.util.List;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Value
public class Events implements List<Object> {

    @Delegate
    List<Object> events;

    public <T> EventStream<T> stream(Class<T> type) {
        return new EventStream<>(events.stream().filter(type::isInstance).map(type::cast));
    }

}
