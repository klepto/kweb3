package dev.klepto.kweb3.core.contract;

import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.contract.annotation.Cost;
import dev.klepto.kweb3.core.contract.type.EthVoid;
import dev.klepto.kweb3.core.ethereum.abi.AbiCodec;
import dev.klepto.kweb3.core.ethereum.abi.HeadlongCodec;
import dev.klepto.kweb3.core.ethereum.abi.descriptor.EthTupleTypeDescriptor;
import dev.klepto.kweb3.core.ethereum.type.EthValue;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.function.Supplier;

import static dev.klepto.kweb3.core.Web3Error.message;
import static dev.klepto.kweb3.core.util.Collections.arrayRemove;
import static dev.klepto.kweb3.core.util.Conditions.require;

/**
 * Default implementation of {@link ContractExecutor}. Encodes and executes appropriate smart-contract views and/or
 * transactions and returns the result for any given contract interface method call.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class ReflectionContractExecutor implements ContractExecutor {

    /**
     * The default codec implementation.
     *
     * @see HeadlongCodec
     */
    private final AbiCodec codec = new HeadlongCodec();

    /**
     * Executes a contract function from start to finish using this executor implementation.
     *
     * @param call the contract interface method call
     * @return the contract function execution result, the type must match return type of the interface method
     */
    @Override
    public @Nullable Object execute(@NotNull ContractCall call) {
        return ContractExecutor.super.execute(call);
    }

    /**
     * Encodes contract function call by ensuring to exclude {@link Cost} arguments and any non-ethereum related
     * arguments (which can be result of bytecode transformations, such as suspend functions).
     *
     * @param call the contract interface method call
     * @return the string containing argument call data
     */
    @Override
    public @NotNull String encode(@NotNull ContractCall call) {
        // Remove cost argument as it's only used to send value with transactions and is not encoded in the call data.
        val argsWithoutCost = arrayRemove(call.args(), call.function().costParameterIndex());

        // Remove all non-ethereum type arguments, such as suspend method transformations.
        val dataArgs = Arrays.stream(argsWithoutCost)
                .filter(EthValue.class::isInstance)
                .map(EthValue.class::cast)
                .toArray(EthValue[]::new);


        val descriptor = (EthTupleTypeDescriptor) call.function().parametersDescriptor();
        val normalizedArgs = ContractCodec.encodeParameterValues(descriptor, dataArgs);
        return codec.encode(normalizedArgs, descriptor);
    }

    /**
     * Creates an RPC request using information available in {@link ContractCall#proxy()}.
     *
     * @param call the contract interface method call
     * @param data the ABI-compatible call data
     * @return the RPC request result
     */
    @Override
    public @NotNull Web3Result<String> request(@NotNull ContractCall call, @NotNull String data) {
        val client = call.proxy().client();
        val clientAddress = client.getAddress() != null ? client.getAddress().toHexString() : null;
        val contractAddress = call.proxy().address().toHexString();
        val callData = call.function().signature() + data;
        return client.getRpc().ethCall(clientAddress, contractAddress, null, null, null, callData, null);
    }

    /**
     * Decodes RPC result into contract return type supporting both asynchronous and blocking return types.
     *
     * @param call   the contract interface method call
     * @param result the result of the RPC call
     * @return the already decoded, or to be decoded in the future contract result
     */
    @Override
    public @Nullable Object decode(@NotNull ContractCall call, @NotNull Web3Result<String> result) {
        return result.map(value -> decodeResult(call, value));
    }

    /**
     * Decodes RPC result string into contract return type using default codec.
     *
     * @param call   the contract interface method call
     * @param result the result string of the RPC call
     * @return the decoded contract result
     */
    @Nullable
    public Object decodeResult(@NotNull ContractCall call, @Nullable String result) {
        val function = call.function();
        val type = function.returnType();
        if (EthVoid.isVoid(type)) {
            return new Object();
        }

        val noResultMessage = (Supplier<String>) () ->
                message(
                        "No result for smart contract call. \nFunction: {}\nNetwork: {}",
                        call,
                        call.proxy().client().getChain()
                );
        require(result != null, noResultMessage);
        val descriptor = function.returnTuple()
                ? function.returnDescriptor()
                : function.returnDescriptor().wrap();
        val tuple = codec.decode(result, descriptor);

        require(tuple != null && !tuple.isEmpty(), noResultMessage);
        val value = (EthValue) (function.returnTuple() ? tuple : tuple.get(0));
        return ContractCodec.decodeReturnValue(type, value);
    }

}
