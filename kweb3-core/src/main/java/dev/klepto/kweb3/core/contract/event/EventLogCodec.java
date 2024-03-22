package dev.klepto.kweb3.core.contract.event;

import dev.klepto.kweb3.core.abi.AbiCodec;
import dev.klepto.kweb3.core.abi.HeadlongCodec;
import dev.klepto.kweb3.core.contract.ContractCodec;
import dev.klepto.unreflect.ClassAccess;
import dev.klepto.unreflect.Unreflect;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import static dev.klepto.kweb3.core.type.EthAddress.address;
import static dev.klepto.kweb3.core.util.Collections.arrayRemove;
import static dev.klepto.kweb3.core.util.Conditions.require;
import static dev.klepto.kweb3.core.util.hash.Keccak256.keccak256;

/**
 * Provides methods for encoding and decoding log data.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class EventLogCodec {

    private static final AbiCodec abiCodec = new HeadlongCodec();

    /**
     * Decodes the log data into a log container instance.
     *
     * @param descriptor the log container descriptor
     * @param topics     the log topics
     * @param data       the log data
     * @param address    the log address
     * @return the decoded log container instance
     */
    @NotNull
    public static Web3EventLog decode(@NotNull EventLogDescriptor descriptor,
                                      @NotNull String[] topics,
                                      @NotNull String data,
                                      @NotNull String address) {
        val result = descriptor.type().allocate();

        val encodedIndexedValues = arrayRemove(topics, 0);
        for (var i = 0; i < encodedIndexedValues.length; i++) {
            val field = descriptor.indexedFields().get(i);
            val valueDescriptor = descriptor.indexedDescriptor().children().get(i);
            val value = abiCodec.decode(encodedIndexedValues[i], valueDescriptor);
            require(value != null, "Failed to decode log data for {}.", descriptor.type());
            field.bind(result).set(value);
        }

        val values = abiCodec.decode(data, descriptor.valueDescriptor());
        require(values != null, "Failed to decode log data for {}.", descriptor.type());
        for (var i = 0; i < values.size(); i++) {
            val field = descriptor.valueFields().get(i);
            field.bind(result).set(values.get(i));
        }

        if (descriptor.addressField() != null) {
            descriptor.addressField()
                    .bind(result)
                    .set(address(address));
        }

        return (Web3EventLog) result;
    }

    /**
     * Parses the log descriptor of a given log container type.
     *
     * @param type the log container type
     * @return the parsed log container descriptor
     */
    @NotNull
    public static EventLogDescriptor parseDescriptor(Class<? extends Web3EventLog> type) {
        val classAccess = Unreflect.reflect(type);
        val name = parseEventName(classAccess);
        val fields = classAccess.fields().toList();
        val addressField = fields.stream()
                .filter(field -> field.containsAnnotation(Web3EventLog.Address.class))
                .findFirst().orElse(null);
        val nonAddressFields = fields.stream()
                .filter(field -> !field.containsAnnotation(Web3EventLog.Address.class))
                .toList();
        val valueFields = nonAddressFields.stream()
                .filter(field -> !field.containsAnnotation(Web3EventLog.Indexed.class))
                .toList();
        val indexedFields = nonAddressFields.stream()
                .filter(field -> field.containsAnnotation(Web3EventLog.Indexed.class))
                .toList();
        val descriptor = ContractCodec.parseTupleDescriptor(nonAddressFields);
        val valueDescriptor = ContractCodec.parseTupleDescriptor(valueFields);
        val indexedDescriptor = ContractCodec.parseTupleDescriptor(indexedFields);
        val signature = name + descriptor.toAbiDescriptor();
        val signatureHash = "0x" + keccak256(signature).toLowerCase();
        return new EventLogDescriptor(
                classAccess.type(),
                name,
                signatureHash,
                addressField,
                valueFields,
                indexedFields,
                valueDescriptor,
                indexedDescriptor
        );
    }

    /**
     * Parses the event name of the log container.
     *
     * @param classAccess the class access of the log container
     * @return the event name
     */
    @NotNull
    private static String parseEventName(ClassAccess<?> classAccess) {
        if (classAccess.containsAnnotation(Web3EventLog.Event.class)) {
            val annotatedName = classAccess.annotation(Web3EventLog.Event.class).value();
            if (!annotatedName.isEmpty()) {
                return annotatedName;
            }
        }
        return classAccess.type().toClass().getSimpleName();
    }

}
