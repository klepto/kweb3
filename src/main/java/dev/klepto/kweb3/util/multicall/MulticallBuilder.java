package dev.klepto.kweb3.util.multicall;

import com.google.common.collect.Lists;
import dev.klepto.kweb3.Web3Request;
import dev.klepto.kweb3.abi.type.Address;
import dev.klepto.kweb3.abi.type.Bytes;
import dev.klepto.kweb3.abi.type.Uint;
import dev.klepto.kweb3.abi.type.util.Convertibles;
import dev.klepto.kweb3.contract.ContractCodec;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.val;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

import static dev.klepto.kweb3.Web3Error.require;
import static dev.klepto.kweb3.abi.type.util.Types.uint256;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@RequiredArgsConstructor
public class MulticallBuilder {

    public static final int DEFAULT_BATCH_SIZE = 512;
    public static final Uint DEFAULT_GAS_LIMIT = uint256(1_000_000);
    public static final Uint DEFAULT_SIZE_LIMIT = uint256(1_000_000);

    private final MulticallContract contract;
    private final List<Call> calls = new ArrayList<>();

    private int batchSize = DEFAULT_BATCH_SIZE;
    private Uint gasLimit = DEFAULT_GAS_LIMIT;
    private Uint sizeLimit = DEFAULT_SIZE_LIMIT;

    public MulticallBuilder batchSize(int batchSize) {
        this.batchSize = batchSize;
        return this;
    }

    public MulticallBuilder gasLimit(Uint gasLimit) {
        this.gasLimit = gasLimit;
        return this;
    }

    public MulticallBuilder sizeLimit(Uint sizeLimit) {
        this.sizeLimit = sizeLimit;
        return this;
    }

    public <T> MulticallBuilder queue(Supplier<T> contractFunction, Consumer<T> consumer) {
        val callCount = contract.getClient().getLogs(contractFunction::get).size();
        require(callCount == 1, "Multicall: You can only queue one call at a time.");
        calls.add(new Call(contractFunction, consumer));
        return this;
    }

    public <T, E> MulticallBuilder queueEach(Iterable<E> elements,
                                             Function<E,T> contractFunction,
                                             Consumer<T> consumer) {
        elements.forEach(element -> queue(() -> contractFunction.apply(element), consumer));
        return this;
    }

    public <T> MulticallBuilder queueUntil(int limitExclusive,
                                            IntFunction<T> contractFunction,
                                            BiConsumer<T, Integer> consumer) {
        for (var i = 0; i < limitExclusive; i++) {
            val index = i;
            val indexedFunction = (Supplier<T>) () -> contractFunction.apply(index);
            val indexedConsumer = (Consumer<T>) (result) -> consumer.accept(result, index);
            queue(indexedFunction, indexedConsumer);
        }
        return this;
    }

    public void execute() {
        val batches = Lists.partition(calls, batchSize);
        batches.forEach(this::execute);
    }

    private void execute(List<Call> calls) {
        val requests = new ArrayList<Web3Request>();
        val addresses = new ArrayList<Address>();
        val data = new ArrayList<Bytes>();

        for (val call : calls) {
            val request = contract.getClient().getLogs(call.supplier::get).get(0);
            val abi = contract.getClient().abiEncode(request);
            val address = request.getContractAddress();
            val callData = Convertibles.toBytes(abi);

            requests.add(request);
            addresses.add(address);
            data.add(callData);
        }

        val response = contract.execute(gasLimit, sizeLimit, addresses, data);
        for (var i = 0; i < response.length; i++) {
            val responseData = response[i];
            val consumer = calls.get(i).getConsumer();
            if (responseData == null) {
                consumer.accept(null);
                continue;
            }

            val request = requests.get(i);
            val abi = Convertibles.toHex(responseData);
            val returnType = request.getFunction().getReturnType();
            val result = contract.getClient().abiDecode(abi, returnType.getAbiType());
            consumer.accept(ContractCodec.decodeResponse(returnType, result));
        }
    }

    @Value
    public class Call {
        Supplier supplier;
        Consumer consumer;
    }

}
