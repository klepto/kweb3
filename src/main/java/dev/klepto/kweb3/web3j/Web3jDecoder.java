package dev.klepto.kweb3.web3j;

import dev.klepto.kweb3.Web3Error;
import dev.klepto.kweb3.Web3Request;
import dev.klepto.kweb3.Web3Response;
import dev.klepto.kweb3.util.reflection.Reflection;
import lombok.val;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.tx.Contract;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static dev.klepto.kweb3.type.Address.address;
import static dev.klepto.kweb3.util.Collections.cast;

/**
 * Decodes Web3j types into solidity types.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface Web3jDecoder {

    static Object decodeValue(Object value) {
        if (value instanceof DynamicArray) {
            val array = ((DynamicArray) value).getValue()
                    .stream()
                    .map(Web3jDecoder::decodeValue)
                    .toArray();
            return cast(array, array[0].getClass());
        } else if (value instanceof Uint) {
            val className = "dev.klepto.kweb3.type.sized." + value.getClass().getSimpleName();
            return Reflection.create(className, ((Uint) value).getValue());
        } else if (value instanceof String) {
            return new dev.klepto.kweb3.type.Address((String) value);
        } else if (value instanceof Address) {
            return new dev.klepto.kweb3.type.Address(((Address) value).getValue());
        } else if (value instanceof DynamicBytes) {
            return new dev.klepto.kweb3.type.Bytes(((DynamicBytes) value).getValue());
        } else if (value instanceof Utf8String) {
            return ((Utf8String) value).getValue();
        }

        throw new Web3Error("Couldn't decode web3j value {} of type {}.", value, value.getClass());
    }

    static List<Object> decodeResult(String result, List types) {
        val castTypes = types.stream().map(t -> (TypeReference<Type>) t).toList();
        return FunctionReturnDecoder.decode(result, castTypes).stream()
                .map(Web3jDecoder::decodeValue).toList();
    }

    static List<Web3Response.Event> decodeEvents(List<Web3Request.Event> eventTypes, List<Log> logs) {
        val result = new ArrayList<Web3Response.Event>();
        val encodedEvents = eventTypes.stream().map(Web3jEncoder::encodeEvent).toList();
        for (var i = 0; i < encodedEvents.size(); i++) {
            val eventType = eventTypes.get(i);
            val encodedEvent = encodedEvents.get(i);
            for (val log : logs) {
                val parameters = Contract.staticExtractEventParameters(encodedEvent, log);
                if (parameters == null) {
                    continue;
                }
                result.add(decodeEvent(eventType, log.getAddress(), parameters));
            }
        }
        return result;
    }

    static Web3Response.Event decodeEvent(Web3Request.Event eventType, String address, EventValues parameters) {
        val indexQueue = new LinkedList<>(parameters.getIndexedValues());
        val nonIndexQueue = new LinkedList<>(parameters.getNonIndexedValues());
        val values = new ArrayList<>();
        for (var i = 0; i < eventType.getValueTypes().size(); i++) {
            val value = eventType.getIndexedValues()[i] ? indexQueue.poll() : nonIndexQueue.poll();
            values.add(decodeValue(value));
        }
        return new Web3Response.Event(eventType.getName(), address(address), values);
    }

}
