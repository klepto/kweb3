package dev.klepto.kweb3.contract.codec;

import com.google.common.reflect.TypeToken;
import dev.klepto.kweb3.Web3Request;
import dev.klepto.kweb3.contract.Type;
import dev.klepto.kweb3.contract.event.Event;
import dev.klepto.kweb3.contract.event.Indexed;
import io.github.classgraph.ClassGraph;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class ContractEvents {

    public static final List<Class<?>> EVENT_CLASSES = findEventClasses();
    public static final List<Web3Request.Event> EVENTS = generateEvents();

    public static String encodeEventName(Class<?> eventClass) {
        var name = eventClass.getAnnotation(Event.class).value();
        if (name.isBlank()) {
            name = eventClass.getSimpleName();
        }
        return name;
    }

    public static Web3Request.Event encodeEvent(Class<?> eventClass) {
        val name = encodeEventName(eventClass);
        val valueTypes = new ArrayList<Class<?>>();
        val fields = eventClass.getDeclaredFields();
        val indexedValues = new boolean[fields.length];
        for (var i = 0; i < fields.length; i++) {
            val field = fields[i];
            val fieldType = TypeToken.of(field.getGenericType());
            val fieldAnnotation = field.getAnnotation(Type.class);
            indexedValues[i] = field.isAnnotationPresent(Indexed.class);
            valueTypes.add(ContractEncoder.encodeParameterType(fieldType, fieldAnnotation));
        }
        return new Web3Request.Event(name, valueTypes, indexedValues);
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

    private static List<Web3Request.Event> generateEvents() {
        val events = new ArrayList<Web3Request.Event>();
        for (val eventClass : findEventClasses()) {
            events.add(encodeEvent(eventClass));
        }
        return events;
    }

}
