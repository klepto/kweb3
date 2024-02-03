package dev.klepto.kweb3.contract;

import com.google.common.collect.ImmutableList;
import dev.klepto.kweb3.contract.annotation.Cost;
import dev.klepto.kweb3.contract.annotation.Transaction;
import dev.klepto.kweb3.contract.annotation.Tuple;
import dev.klepto.kweb3.contract.annotation.View;
import dev.klepto.kweb3.type.EthType;
import dev.klepto.unreflect.MethodAccess;
import dev.klepto.unreflect.ParameterAccess;
import lombok.val;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static dev.klepto.kweb3.util.hash.Keccak256.keccak256;
import static dev.klepto.unreflect.Unreflect.reflect;

/**
 * Parses contract functions and caches results for future use.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class ContractFunctions {

    private final Map<Method, ContractFunction> cache = new ConcurrentHashMap<>();

    /**
     * Parses or gets cached version of contract function for a given contract interface {@link Method}.
     *
     * @param method the contract interface method
     * @return the contract function that this method wants to invoke
     */
    public ContractFunction get(Method method) {
        cache.computeIfAbsent(method, key -> parseFunction(method));
        return cache.get(method);
    }

    /**
     * Reflectively parses a contract function based on {@link Method} metadata.
     *
     * @param method the reflection method
     * @return the contract function that this method wants to invoke
     */
    private ContractFunction parseFunction(Method method) {
        val methodAccess = reflect(method);
        val name = parseFunctionName(methodAccess);
        val parameters = getFunctionParameters(methodAccess);
        val parametersDescriptor = ContractCodec.parseTupleDescriptor(parameters);
        val signature = name + parametersDescriptor.toAbiDescriptor();
        val signatureHash = "0x" + keccak256(signature).substring(0, 8).toLowerCase();
        val returnDescriptor = ContractCodec.parseDescriptor(methodAccess);
        return new ContractFunction(methodAccess, name, signatureHash, parametersDescriptor, returnDescriptor, -1);
    }

    /**
     * Parses function name either from method annotations, or the method name if name isn't explicitly specified.
     *
     * @param methodAccess the method access
     * @return the string containing contract function name
     */
    private String parseFunctionName(MethodAccess methodAccess) {
        val viewAnnotation = methodAccess.annotation(View.class);
        val transactionAnnotation = methodAccess.annotation(Transaction.class);
        var name = viewAnnotation != null ? viewAnnotation.value()
                : transactionAnnotation != null ? transactionAnnotation.value()
                : "";
        return !name.isBlank() ? name : methodAccess.name();
    }

    /**
     * Parses list of contract function parameters. Any method parameter is considered a contract function parameter
     * unless it's annotated with {@link Cost} annotation.
     *
     * @param methodAccess the method access
     * @return the list containing all contract function parameters
     */
    private ImmutableList<ParameterAccess> getFunctionParameters(MethodAccess methodAccess) {
        return methodAccess.parameters()
                .filter(parameter -> !parameter.containsAnnotation(Cost.class))
                .filter(parameter -> parameter.type().matches(EthType.class) || parameter.containsAnnotation(Tuple.class))
                .collect(toImmutableList());
    }

}
