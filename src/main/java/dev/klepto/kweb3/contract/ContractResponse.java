package dev.klepto.kweb3.contract;

import dev.klepto.kweb3.Web3Error;
import dev.klepto.kweb3.Web3Request;
import dev.klepto.kweb3.type.Address;
import lombok.Value;
import lombok.val;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dev.klepto.kweb3.Web3Response;

/**
 * Extension for {@link Web3Response}, rather than containing just web3 result, contains additional information about
 * contract interface class.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Value
public class ContractResponse {

    Class<?> contractClass;

    Web3Request request;

    @Nullable
    String transactionHash;

    @Nullable
    Web3Error error;

    @Nullable
    List<Object> result;

    List<Object> events;

    List<Class<?>> eventTypes;

    public <T> T getEvent(Class<T> eventType) {
        return getEvents(eventType).findFirst().orElse(null);
    }

    public <T> Stream<T> getEvents(Class<T> eventType) {
        return events.stream()
                .filter(eventType::isInstance)
                .map(eventType::cast);
    }

}
