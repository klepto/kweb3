package dev.klepto.kweb3.contract.event;

import dev.klepto.kweb3.abi.type.util.Pair;
import lombok.Value;
import lombok.experimental.Delegate;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Value
public class EventStream<T> implements Stream<T> {

    @Delegate
    Stream<T> eventStream;

    EventFilter<T> filter = new EventFilter<>();

    public EventStream(Stream<T> eventStream) {
        this.eventStream = eventStream.filter(filter);
    }

    public <R> EventStream<T> with(Function<? super T, ? extends R> value, R expectedValue) {
        filter.getFilters().add(new Pair(value, expectedValue));
        return this;
    }

    public T first() {
        return findFirst().orElse(null);
    }

    public <R> R get(Function<? super T, ? extends R> value, R defaultValue) {
        val first = first();
        if (first == null) {
            return defaultValue;
        }

        return value.apply(first);
    }

    @Value
    public static class EventFilter<T> implements Predicate<T> {

        List<Pair<Function<? super T, ?>, ?>> filters = new ArrayList<>();

        @Override
        public boolean test(T t) {
            for (val filter : filters) {
                val value = filter.getFirst().apply(t);
                val expectedValue = filter.getSecond();
                if (!value.equals(expectedValue)) {
                    return false;
                }
            }
            return true;
        }

    }

}