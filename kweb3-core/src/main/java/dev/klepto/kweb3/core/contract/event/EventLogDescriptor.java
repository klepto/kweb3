package dev.klepto.kweb3.core.contract.event;

import dev.klepto.kweb3.core.ethereum.abi.descriptor.EthTupleTypeDescriptor;
import dev.klepto.unreflect.FieldAccess;
import dev.klepto.unreflect.UnreflectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Describes a container for a smart contract log event.
 *
 * @param type              the type of the log container
 * @param name              the event name of the log
 * @param signature         the event signature of the log
 * @param addressField      the address field of the log
 * @param valueFields       all value fields of the log
 * @param indexedFields     all indexed fields of the log
 * @param valueDescriptor   the descriptor of the value fields
 * @param indexedDescriptor the descriptor of the indexed fields
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public record EventLogDescriptor(@NotNull UnreflectType type,
                                 @NotNull String name,
                                 @NotNull String signature,
                                 @Nullable FieldAccess addressField,
                                 @NotNull List<FieldAccess> valueFields,
                                 @NotNull List<FieldAccess> indexedFields,
                                 @NotNull EthTupleTypeDescriptor valueDescriptor,
                                 @NotNull EthTupleTypeDescriptor indexedDescriptor) {
}
