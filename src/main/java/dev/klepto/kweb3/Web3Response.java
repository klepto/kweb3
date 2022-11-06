package dev.klepto.kweb3;

import dev.klepto.kweb3.type.Address;
import lombok.Value;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Contains basic information about web3 contract response.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Value
public class Web3Response {

    @Nullable
    String transactionHash;

    Web3Request request;

    @Nullable
    Web3Error error;

    @Nullable
    List<Object> result;

    @Nullable
    List<Event> events;

    @Value
    public static class Event {
        String name;
        Address address;
        List<Object> values;
    }

}
