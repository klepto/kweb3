package dev.klepto.kweb3.multicall;

import com.google.common.collect.Lists;
import dev.klepto.kweb3.core.Web3Result;
import dev.klepto.kweb3.core.contract.Web3Contract;
import dev.klepto.kweb3.core.type.EthAddress;
import dev.klepto.kweb3.core.type.EthArray;
import dev.klepto.kweb3.core.type.EthBytes;
import dev.klepto.kweb3.core.type.EthUint;
import dev.klepto.kweb3.core.util.Hex;
import lombok.With;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

import static dev.klepto.kweb3.core.type.EthUint.uint256;
import static dev.klepto.kweb3.core.util.Conditions.require;

/**
 * Enables building and executing type-safe multicall requests. A multicall is a common smart contract pattern used to
 * minimize amount of RPC requests while fetching blockchain data, where, instead of making several individual view
 * requests you aggregate them through intermediary contract called <i>multicall</i>.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
@SuppressWarnings("unchecked")
public class MulticallBuilder {

    public static final int DEFAULT_BATCH_SIZE = 512;
    public static final EthUint DEFAULT_GAS_LIMIT = uint256(1_000_000);
    public static final EthUint DEFAULT_SIZE_LIMIT = uint256(1_000_000);

    private final @NotNull MulticallContract multicallContract;
    private final @NotNull List<Call> calls;
    private final int batchSize;
    private final @NotNull EthUint gasLimit;
    private final @NotNull EthUint sizeLimit;

    /**
     * Creates a new multicall builder using provided multicall smart contract implementation.
     *
     * @param multicallContract the multicall smart contract implementation
     */
    public MulticallBuilder(MulticallContract multicallContract) {
        this(multicallContract, List.of(), DEFAULT_BATCH_SIZE, DEFAULT_GAS_LIMIT, DEFAULT_SIZE_LIMIT);
    }

    private MulticallBuilder(@NotNull MulticallContract multicallContract,
                             @NotNull List<Call> calls,
                             int batchSize,
                             @NotNull EthUint gasLimit,
                             @NotNull EthUint sizeLimit) {
        this.multicallContract = multicallContract;
        this.batchSize = batchSize;
        this.gasLimit = gasLimit;
        this.sizeLimit = sizeLimit;
        this.calls = calls;
    }

    /**
     * Appends new contract call to multi-call builder queue.
     *
     * @param contract the contract for this call
     * @param request  the request supplier for this call
     * @param response the response consumer for this call
     * @return a new instance of multi-call builder containing new contract call in its queue
     */
    @NotNull
    public <T extends Web3Contract, R> MulticallBuilder queue(@NotNull T contract,
                                                              @NotNull Function<T, R> request,
                                                              @NotNull Consumer<R> response) {
        val calls = new ArrayList<>(this.calls);
        calls.add(new Call(contract, request, response));
        return new MulticallBuilder(multicallContract, calls, batchSize, gasLimit, sizeLimit);
    }

    /**
     * Executes this multi-call builder. All contract calls registered via
     * {@link MulticallBuilder#queue(Web3Contract, Function, Consumer)} will be executed through provided multi-call
     * smart contract interface. All bytes received will be automatically decoded ran through all the provided result
     * consumers, aggregated and returned.
     *
     * @return the web3 result containing raw result of {@link MulticallContract} call containing all calls associated
     * with this builder
     */
    public Web3Result<EthArray<EthBytes>> execute() {
        require(batchSize > 0, "Non-positive batch size.");
        val batches = Lists.partition(calls, batchSize);
        val batchResults = batches.stream().map(this::executeBatch).toList();

        System.out.println(batches.size());

        val result = new Web3Result<EthArray<EthBytes>>();
        val counter = new AtomicInteger(0);
        batchResults.forEach(batchResult -> {
            if (counter.incrementAndGet() != batches.size()) {
                return;
            }

            val combined = new ArrayList<EthBytes>();
            batchResults.stream()
                    .map(Web3Result::get)
                    .forEach(array -> combined.addAll(array.asList()));
            result.complete(EthArray.array(combined));
        });
        return result;
    }

    /**
     * Executes a single multicall batch.
     *
     * @param batch a batch containing list of calls to be requested with multicall
     * @return an ethereum array containing resulting bytes of this batch
     */
    @NotNull
    private Web3Result<EthArray<EthBytes>> executeBatch(List<Call> batch) {
        val client = multicallContract.getClient();
        val data = new ArrayList<EthBytes>();
        val addresses = new ArrayList<EthAddress>();
        val logger = new LoggingWeb3Client();

        // Generate ABI data and populate address list.
        batch.forEach(call -> {
            addresses.add(call.contract().getAddress());
            val contract = logger.contract(call.contract().getContractClass());
            call.request().apply(contract);
        });

        // Populate ABI data list.
        val logs = logger.getLogs();
        logs.forEach(log -> {
            data.add(EthBytes.bytes(log.data()));
        });

        // Create RPC request.
        val result = multicallContract.execute(
                gasLimit,
                sizeLimit,
                EthArray.array(addresses),
                EthArray.array(data)
        );

        // Register result consumers.
        for (var i = 0; i < batch.size(); i++) {
            val index = i;
            val call = batch.get(index);
            val log = logs.get(index);

            result.get(value -> {
                val bytes = (EthBytes) value.get(index);
                val abiResult = new Web3Result<String>();
                abiResult.complete(Hex.toHex(bytes.toByteArray()));

                val decoded = client.getContracts().getExecutor().decode(log.call(), abiResult);
                call.response().accept(decoded);
            });
        }

        return result;
    }

    /**
     * Contains a single multi-call request.
     *
     * @param contract the contract
     * @param request  the call request supplier
     * @param response the call response consumer
     */
    private record Call(@NotNull Web3Contract contract, @NotNull Function request, @NotNull Consumer response) {
    }

}
