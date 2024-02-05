package dev.klepto.kweb3.contract;

import dev.klepto.kweb3.abi.descriptor.TypeDescriptor;
import dev.klepto.kweb3.contract.annotation.Cost;
import dev.klepto.kweb3.contract.annotation.Transaction;
import dev.klepto.kweb3.contract.annotation.Tuple;
import dev.klepto.kweb3.contract.annotation.View;
import dev.klepto.kweb3.type.EthType;
import dev.klepto.unreflect.MethodAccess;
import dev.klepto.unreflect.Unreflect;
import lombok.val;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static dev.klepto.kweb3.util.hash.Keccak256.keccak256;
import static dev.klepto.unreflect.Unreflect.reflect;

/**
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
    public ContractFunction parseFunction(Method method) {
        cache.computeIfAbsent(method, key -> {
            val methodAccess = reflect(method);
            val name = parseFunctionName(methodAccess);
            val parametersDescriptor = parseParametersType(method);
            val signature = name + parametersDescriptor.toAbiDescriptor();
            val signatureHash = "0x" + keccak256(signature).substring(0, 8).toLowerCase();
            val returnDescriptor = parseReturnType(method);
            return new ContractFunction(methodAccess, name, signatureHash, parametersDescriptor, returnDescriptor, -1);
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
    public TypeDescriptor parseParametersType(Method method) {
        val methodAccess = reflect(method);
        val parameters = methodAccess.parameters()
                .filter(parameter -> !parameter.containsAnnotation(Cost.class))
                .filter(parameter -> parameter.type().matches(EthType.class) || parameter.containsAnnotation(Tuple.class))
                .collect(toImmutableList());
        return ContractCodec.parseTupleDescriptor(parameters);
    }

    /**
     * Parses contract return type description {@link Unreflect} API.
     *
     * @param method the contract interface method
     * @return the contract return type description
     */
    @Override
    public TypeDescriptor parseReturnType(Method method) {
        val methodAccess = reflect(method);
        return ContractCodec.parseDescriptor(methodAccess);
    }

    /**
     * Parses function name either from method annotations, or the method name if name isn't explicitly specified.
     *
     * @param methodAccess the method access
     * @return the string containing contract function name
     */
    public String parseFunctionName(MethodAccess methodAccess) {
        val viewAnnotation = methodAccess.annotation(View.class);
        val transactionAnnotation = methodAccess.annotation(Transaction.class);
        var name = viewAnnotation != null ? viewAnnotation.value()
                : transactionAnnotation != null ? transactionAnnotation.value()
                : "";
        return !name.isBlank() ? name : methodAccess.name();
    }

}
