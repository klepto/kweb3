package dev.klepto.kweb3.core.contract;

import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.abi.descriptor.TypeDescriptor;
import dev.klepto.kweb3.core.contract.annotation.Cost;
import dev.klepto.kweb3.core.contract.annotation.Transaction;
import dev.klepto.kweb3.core.contract.annotation.View;
import dev.klepto.kweb3.core.contract.type.EthStructContainer;
import dev.klepto.kweb3.core.contract.type.EthTupleContainer;
import dev.klepto.kweb3.core.type.EthType;
import dev.klepto.unreflect.MethodAccess;
import dev.klepto.unreflect.Unreflect;
import dev.klepto.unreflect.UnreflectType;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static dev.klepto.kweb3.core.util.Conditions.require;
import static dev.klepto.kweb3.core.util.hash.Keccak256.keccak256;
import static dev.klepto.unreflect.Unreflect.reflect;

/**
 * Default implementation of {@link ContractParser}. Uses {@link Unreflect} to infer smart-contract function ABI types
 * to be used with contract interface call encoding/decoding.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class DefaultContractParser implements ContractParser {

    private final Map<Method, ContractFunction> cache = new ConcurrentHashMap<>();

    /**
     * Parses contract function using {@link Unreflect} API and caches it for future use.
     *
     * @param method the contract interface method
     * @return the contract function description
     */
    @Override
    public @NotNull ContractFunction parseFunction(@NotNull Method method) {
        cache.computeIfAbsent(method, key -> {
            val methodAccess = reflect(method);
            val name = parseFunctionName(methodAccess);
            val parametersDescriptor = parseParametersTypeDescriptor(method);
            val signature = name + parametersDescriptor.toAbiDescriptor();
            val signatureHash = "0x" + keccak256(signature).substring(0, 8).toLowerCase();
            val returnDescriptor = parseReturnTypeDescriptor(method);
            val returnType = parseReturnType(method);
            val returnTuple = returnType.matches(EthTupleContainer.class)
                    && !returnType.matches(EthStructContainer.class);

            return new ContractFunction(
                    methodAccess,
                    name,
                    signatureHash,
                    parametersDescriptor,
                    returnDescriptor,
                    returnType,
                    returnTuple,
                    -1
            );
        });

        return cache.get(method);
    }

    /**
     * Parses contract parameters type description {@link Unreflect} API.
     *
     * @param method the contract interface method
     * @return the contract parameters type description
     */
    @Override
    public @NotNull TypeDescriptor parseParametersTypeDescriptor(@NotNull Method method) {
        val methodAccess = reflect(method);
        val parameters = methodAccess.parameters()
                .filter(parameter -> !parameter.containsAnnotation(Cost.class))
                .filter(parameter -> parameter.type().matches(EthType.class))
                .collect(toImmutableList());
        return ContractCodec.parseTupleDescriptor(parameters);
    }

    /**
     * Parses contract return type description using {@link Unreflect} API.
     *
     * @param method the contract interface method
     * @return the contract return type description
     */
    @Override
    public @NotNull TypeDescriptor parseReturnTypeDescriptor(@NotNull Method method) {
        return ContractCodec.parseDescriptor(reflect(method));
    }

    /**
     * Parses contract JVM return type.
     *
     * @param method the contract interface method
     * @return the contract JVM return type
     */
    @Override
    public @NotNull UnreflectType parseReturnType(@NotNull Method method) {
        val type = UnreflectType.of(method);
        if (type.matchesExact(Web3Result.class)) {
            val genericType = type.genericType();
            require(genericType != null, "Could not infer generic type of Web3Result.");
            return genericType;
        }
        return type;
    }

    /**
     * Parses function name either from method annotations, or the method name if name isn't explicitly specified.
     *
     * @param methodAccess the method access
     * @return the string containing contract function name
     */
    public @NotNull String parseFunctionName(@NotNull MethodAccess methodAccess) {
        val viewAnnotation = methodAccess.annotation(View.class);
        val transactionAnnotation = methodAccess.annotation(Transaction.class);
        var name = viewAnnotation != null ? viewAnnotation.value()
                : transactionAnnotation != null ? transactionAnnotation.value()
                : "";
        return !name.isBlank() ? name : methodAccess.name();
    }

}
