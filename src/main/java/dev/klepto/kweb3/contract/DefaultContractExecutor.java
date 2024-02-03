package dev.klepto.kweb3.contract;

import dev.klepto.kweb3.Web3Result;
import dev.klepto.kweb3.abi.AbiCodec;
import dev.klepto.kweb3.abi.HeadlongCodec;
import dev.klepto.kweb3.contract.annotation.Cost;
import dev.klepto.kweb3.type.EthTuple;
import dev.klepto.kweb3.type.EthType;
import lombok.val;
import one.util.streamex.StreamEx;

import static dev.klepto.kweb3.type.EthTuple.tuple;
import static dev.klepto.kweb3.util.Collections.arrayRemove;

/**
 * Default implementation of {@link ContractExecutor}. Encodes and executes appropriate blockchain views and/or
 * transactions and returns the result for any given contract method.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class DefaultContractExecutor implements ContractExecutor {

    /**
     * The default codec implementation.
     *
     * @see HeadlongCodec
     */
    private final AbiCodec codec = new HeadlongCodec();

    /**
     * Encodes contract function call by ensuring to exclude {@link Cost} arguments and any non-ethereum related
     * arguments (which can be result of bytecode transformations, such as suspend functions).
     *
     * @param call the contract interface method call
     * @return the string containing argument call data
     */
    @Override
    public String encode(ContractCall call) {
        // Remove cost argument as it's only used to send value with transactions and is not encoded in the call data.
        val argsWithoutCost = arrayRemove(call.args(), call.function().costParameterIndex());

        // Remove all non-ethereum type arguments, such as suspend method transformations.
        val dataArgs = StreamEx.of(argsWithoutCost)
                .filter(EthType.class::isInstance)
                .map(EthType.class::cast)
                .toList();

        val descriptor = call.function().parametersDescriptor();
        return codec.encode(tuple(dataArgs), descriptor);
    }

    /**
     * Creates an RPC request using information available in {@link ContractCall#proxy()}.
     *
     * @param call the contract interface method call
     * @param data the ABI-compatible call data
     * @return the RPC request result
     */
    @Override
    public Web3Result<String> request(ContractCall call, String data) {
        val client = call.proxy().getClient();
        val clientAddress = client.getAddress() != null ? client.getAddress().toHex() : null;
        val contractAddress = call.proxy().getAddress().toHex();
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
    public Object decode(ContractCall call, Web3Result<String> result) {
        // For asynchronous signature, map future web3 result for decoding.
        if (call.function().method().type().matchesExact(Web3Result.class)) {
            return result.map(value -> decodeResult(call, value).get(0)).error(Throwable::printStackTrace);
        }

        // For blocking signature, wait for result and decode.
        return decodeResult(call, result.get()).get(0);
    }

    /**
     * Decodes RPC result string into contract return type using default codec.
     *
     * @param call   the contract interface method call
     * @param result the result string of the RPC call
     * @return the decoded contract result
     */
    private EthTuple decodeResult(ContractCall call, String result) {
        return codec.decode(result, call.function().returnDescriptor());
    }

}
