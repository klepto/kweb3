package dev.klepto.kweb3;

import dev.klepto.kweb3.abi.type.Address;
import dev.klepto.kweb3.abi.type.Tuple;
import dev.klepto.kweb3.abi.type.Uint;
import dev.klepto.kweb3.contract.event.Events;
import lombok.Value;
import lombok.With;

import java.util.List;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Value
@With
public class Web3Response {

    Web3Client client;
    Web3Request request;
    Web3Error error;

    String transactionHash;
    Uint blockNumber;
    Tuple result;
    List<Log> logs;
    Events events;

    @Value
    public static class Log {
        Address address;
        List<String> topics;
        String data;
    }


}
