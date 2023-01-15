package dev.klepto.kweb3.contract.event;

import dev.klepto.kweb3.Web3Client;
import dev.klepto.kweb3.Web3Response;
import dev.klepto.kweb3.contract.*;
import dev.klepto.kweb3.util.Keccak;
import io.github.classgraph.ClassGraph;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.val;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class EventDecoder {

    private final Map<Class<?>, EventDefinition> definitions;

    public EventDecoder() {
        this(findEventClasses());
    }

    public EventDecoder(List<Class<?>> eventClasses) {
        this.definitions = getEventDefinitions(eventClasses);
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
            for (val eventDefinition : definitions.values()) {
                val eventSignature = eventDefinition.getSignature();
                if (!eventSignature.equals(logSignature)) {
                    continue;
                }

                if (log.getTopics().size() - 1 != eventDefinition.getIndexedFields().size()) {
                    continue;
                }

                if (logEvent == null) {
                    logEvent = decodeEvent(client, eventDefinition, log);
                    result.add(logEvent);
                } else {
                    result.add(copyEvent(logEvent, eventDefinition));
                }
            }
        }
        return new Events(result);
    }

    @SneakyThrows
    private Object decodeEvent(Web3Client client, EventDefinition definition, Web3Response.Log log) {
        val fields = definition.getFields();
        val indexedType = definition.getIndexedType();
        val valueType = definition.getValueType();
        val value = client.abiDecode(log.getData(), valueType.getAbiType());

        var valueOffset = 0;
        var topicOffset = 1;
        val parameters = new ArrayList<>();
        for (val field : fields) {
            var fieldValue = (Object) null;

            if (field.isAnnotationPresent(Event.Address.class)) {
                fieldValue = log.getAddress();
            } else if (field.isAnnotationPresent(Event.Indexed.class)) {
                val fieldType = indexedType.getChildren().get(topicOffset - 1);
                val fieldAbi = log.getTopics().get(topicOffset++);
                fieldValue = client.abiDecode(fieldAbi, fieldType.getAbiType()).get(0);
            } else {
                fieldValue = value.get(valueOffset++);
            }

            parameters.add(fieldValue);
        }

        val eventClass = definition.getEventClass();
        val constructor = eventClass.getDeclaredConstructors()[0];
        constructor.setAccessible(true);
        return constructor.newInstance(parameters.toArray());
    }


    private static Map<Class<?>, EventDefinition> getEventDefinitions(List<Class<?>> eventClasses) {
        return eventClasses.stream().collect(Collectors.toMap(key -> key, key -> getEventDefinition(key)));
    }

    private static EventDefinition getEventDefinition(Class<?> eventClass) {
        val fields = Arrays.stream(eventClass.getDeclaredFields()).toList();
        val nonAddressFields = fields.stream()
                .filter(field -> !field.isAnnotationPresent(Event.Address.class))
                .toList();
        val valueFields = fields.stream()
                .filter(field -> !field.isAnnotationPresent(Event.Indexed.class))
                .filter(field -> !field.isAnnotationPresent(Event.Address.class))
                .toList();
        val indexedFields = fields.stream()
                .filter(field -> field.isAnnotationPresent(Event.Indexed.class))
                .toList();
        val indexedType = ContractCodec.parseFieldsType(indexedFields);
        val valueType = ContractCodec.parseFieldsType(valueFields);

        var name = eventClass.getAnnotation(Event.class).value();
        if (name.isBlank()) {
            name = eventClass.getSimpleName();
        }

        val signatureType = ContractCodec.parseFieldsType(nonAddressFields);
        val signatureRaw = name + signatureType.getAbiType().toString();
        val signature = "0x" + Keccak.hash(signatureRaw);
        return new EventDefinition(
                eventClass,
                name,
                signature,
                fields,
                valueFields,
                indexedFields,
                indexedType,
                valueType
        );
    }

    @SneakyThrows
    private static Object copyEvent(Object event, EventDefinition definition) {
        val eventClass = definition.getEventClass();
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

    @Value
    private static class EventDefinition {
        Class<?> eventClass;
        String name;
        String signature;
        List<Field> fields;
        List<Field> valueFields;
        List<Field> indexedFields;
        ValueType indexedType;
        ValueType valueType;
    }

}
