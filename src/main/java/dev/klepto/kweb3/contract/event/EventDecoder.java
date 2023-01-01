package dev.klepto.kweb3.contract.event;

import dev.klepto.kweb3.Web3Client;
import dev.klepto.kweb3.Web3Response;
import dev.klepto.kweb3.contract.*;
import dev.klepto.kweb3.util.Keccak;
import io.github.classgraph.ClassGraph;
import lombok.SneakyThrows;
import lombok.val;

import java.util.*;


/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class EventDecoder {

    private final List<Class<?>> eventClasses;
    private final Map<Class<?>, ValueType> eventTypes;
    private final Map<Class<?>, String> eventSignatures;

    public EventDecoder() {
        this(findEventClasses());
    }

    public EventDecoder(List<Class<?>> eventClasses) {
        this.eventClasses = eventClasses;
        this.eventTypes = getEventTypes();
        this.eventSignatures = getEventSignatures();
    }

    public Events decode(Web3Response response) {
        return decode(response.getClient(), response.getLogs());
    }

    public Events decode(Web3Client client, List<Web3Response.Log> logs) {
        val result = new ArrayList<>();
        for (val log : logs) {
            if (log.getTopics().isEmpty()) {
                continue;
            }

            var logEvent = (Object) null;
            val logSignature = log.getTopics().get(0);
            for (val eventType : eventClasses) {
                val eventSignature = eventSignatures.get(eventType);
                if (!eventSignature.equals(logSignature)) {
                    continue;
                }

                if (logEvent == null) {
                    logEvent = decodeEvent(client, eventType, log);
                    result.add(logEvent);
                } else {
                    result.add(copyEvent(logEvent, eventType));
                }
            }
        }
        return new Events(result);
    }

    @SneakyThrows
    private Object decodeEvent(Web3Client client, Class<?> eventClass, Web3Response.Log log) {
        val eventFields = Arrays.asList(eventClass.getDeclaredFields());
        val eventType = eventTypes.get(eventClass);
        val valueFields = eventFields.stream()
                .filter(field -> !field.isAnnotationPresent(Event.Indexed.class))
                .filter(field -> !field.isAnnotationPresent(Event.Address.class))
                .toList();
        val valueType = ContractCodec.parseFieldsType(valueFields);
        val value = client.abiDecode(log.getData(), valueType.getAbiType());

        var valueOffset = 0;
        var topicOffset = 1;
        val parameters = new ArrayList<>();
        for (var i = 0; i < eventFields.size(); i++) {
            val field = eventFields.get(i);
            var fieldValue = (Object) null;

            if (field.isAnnotationPresent(Event.Address.class)) {
                fieldValue = log.getAddress();
            } else if (field.isAnnotationPresent(Event.Indexed.class)) {
                val fieldType = eventType.getChildren().get(i);
                val fieldAbi = log.getTopics().get(topicOffset++);
                fieldValue = client.abiDecode(fieldAbi, fieldType.getAbiType()).get(0);
            } else {
                fieldValue = value.get(valueOffset++);
            }

            parameters.add(fieldValue);
        }

        val constructor = eventClass.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        return constructor.newInstance(parameters.toArray());
    }

    private Map<Class<?>, ValueType> getEventTypes() {
        val result = new HashMap<Class<?>, ValueType>();
        for (val type : eventClasses) {
            val fields = Arrays.asList(type.getDeclaredFields());
            result.put(type, ContractCodec.parseFieldsType(fields));
        }
        return result;
    }

    private Map<Class<?>, String> getEventSignatures() {
        val result = new HashMap<Class<?>, String>();
        for (val type : eventClasses) {
            var eventName = type.getAnnotation(Event.class).value();
            if (eventName.isBlank()) {
                eventName = type.getSimpleName();
            }

            val eventFields = Arrays.stream(type.getDeclaredFields())
                    .filter(field -> !field.isAnnotationPresent(Event.Address.class))
                    .toList();
            val eventType = ContractCodec.parseFieldsType(eventFields);
            val eventSignature = eventName + eventType.getAbiType().toString();
            val eventHash = "0x" + Keccak.hash(eventSignature);
            result.put(type, eventHash);
        }
        return result;
    }

    @SneakyThrows
    private static Object copyEvent(Object event, Class<?> eventClass) {
        val fields = event.getClass().getDeclaredFields();
        val parameters = new ArrayList<>();
        for (var i = 0; i < fields.length; i++) {
            val field = fields[i];
            field.setAccessible(true);
            parameters.add(field.get(event));
        }

        val constructor = eventClass.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        return constructor.newInstance(parameters.toArray());
    }

    private static List<Class<?>> findEventClasses() {
        val eventClasses = new ArrayList<Class<?>>();
        try (val scanResult = new ClassGraph().enableAnnotationInfo().scan()) {
            for (val eventClassInfo : scanResult.getClassesWithAnnotation(Event.class)) {
                eventClasses.add(eventClassInfo.loadClass());
            }
        }
        return eventClasses;
    }

}
