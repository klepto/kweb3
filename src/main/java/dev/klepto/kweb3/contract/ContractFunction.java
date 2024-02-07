package dev.klepto.kweb3.contract;

import dev.klepto.kweb3.abi.descriptor.TypeDescriptor;
import dev.klepto.unreflect.MethodAccess;
import dev.klepto.unreflect.UnreflectType;

/**
 * Represents blockchain contract function and the associated data types.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public record ContractFunction(
        MethodAccess method,
        String name,
        String signature,
        TypeDescriptor parametersDescriptor,
        TypeDescriptor returnDescriptor,
        UnreflectType returnType,
        boolean returnTuple,
        int costParameterIndex
) {
}
