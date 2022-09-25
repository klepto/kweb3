package dev.klepto.kweb3.contract;

import dev.klepto.kweb3.Web3Error;
import lombok.Value;
import lombok.val;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import dev.klepto.kweb3.Web3Response;

/**
 * Extension for {@link Web3Response}, rather than containing just web3 result, contains additional information about
 * contract interface such as {@link ContractResponse#getContractClass()} and {@link ContractResponse#getEventClasses()}.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Value
public class ContractResponse {

    Class<?> contractClass;

    @Nullable
    String transactionHash;

    @Nullable
    Web3Error error;

    @Nullable
    List<Object> result;

    @Nullable
    List<Object> events;

    List<Class<?>> eventClasses;

    public <T> T getEvent(Class<T> eventType) {
        val events = getEvents(eventType);
        return events.isEmpty() ? null : events.get(0);
    }

    public <T> List<T> getEvents(Class<T> eventType) {
        if (events == null) {
            return Collections.emptyList();
        }
        return events.stream()
                .filter(eventType::isInstance)
                .map(eventType::cast)
                .collect(Collectors.toList());
    }

}
