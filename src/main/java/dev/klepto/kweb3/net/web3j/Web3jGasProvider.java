package dev.klepto.kweb3.net.web3j;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import dev.klepto.kweb3.abi.type.Uint;
import dev.klepto.kweb3.abi.type.util.Types;
import dev.klepto.kweb3.gas.GasFee;
import dev.klepto.kweb3.gas.GasFeeProvider;
import dev.klepto.kweb3.gas.LegacyGasFee;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.val;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;

import java.math.BigInteger;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import static dev.klepto.kweb3.abi.type.util.Types.uint256;
import static dev.klepto.kweb3.util.Functions.tryOrElse;
import static dev.klepto.kweb3.util.Logging.debug;

/**
 * Default gas price provider. Selects gas price based on gas prices used in the latest block. Selected price is always
 * in-between minimum and median gas price.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Web3jGasProvider implements GasFeeProvider {

    private final Duration UPDATE_RATE = Duration.ofSeconds(5);

    private final Web3j client;
    private final AtomicReference<GasFeeStats> stats = new AtomicReference<>(null);
    private final Stopwatch lastUpdate = Stopwatch.createUnstarted();

    public Web3jGasProvider(Web3j client) {
        this.client = client;
    }

    private GasFeeStats getStats() {
        if (!lastUpdate.isRunning() || lastUpdate.elapsed(TimeUnit.NANOSECONDS) > UPDATE_RATE.toNanos()) {
            lastUpdate.reset();
            lastUpdate.start();
            stats.set(fetchStats());
        }

        return stats.get();
    }

    @SneakyThrows
    private GasFeeStats fetchStats() {
        val transactions = getLatestTransactions(50);
        val baseFeePerGas = getLatestBaseFeePerGas();

        val gasPrices = getTransactionValues(transactions, Transaction::getGasPrice);
        val maxFeesPerGas = getTransactionValues(transactions, Transaction::getMaxFeePerGas);
        val maxPriorityFeesPerGas = getTransactionValues(transactions, Transaction::getMaxPriorityFeePerGas);

        val minGasPrice = gasPrices.get(0);
        val minMaxFee = maxFeesPerGas.get(0);
        val minMaxPriorityFee = maxPriorityFeesPerGas.get(0);

        val medianGasPrice = gasPrices.get(gasPrices.size() / 2);
        val medianMaxFee = maxFeesPerGas.get(maxFeesPerGas.size() / 2);
        val medianMaxPriorityFee = maxPriorityFeesPerGas.get(maxPriorityFeesPerGas.size() / 2);

        val maxGasPrice = gasPrices.get(gasPrices.size() - 1);
        val maxMaxFee = maxFeesPerGas.get(maxFeesPerGas.size() - 1);
        val maxMaxPriorityFee = maxPriorityFeesPerGas.get(maxPriorityFeesPerGas.size() - 1);

        val avgGasPrice = getAverage(gasPrices);
        val avgMaxFee = getAverage(maxFeesPerGas);
        val avgMaxPriorityFee = getAverage(maxPriorityFeesPerGas);

        val gasPrice = new Fee(minGasPrice, maxGasPrice, avgGasPrice, medianGasPrice);
        val maxFee = new Fee(minMaxFee, maxMaxFee, avgMaxFee, medianMaxFee);
        val maxPriorityFee = new Fee(
                minMaxPriorityFee,
                maxMaxPriorityFee,
                avgMaxPriorityFee,
                medianMaxPriorityFee
        );

        return new GasFeeStats(
                gasPrice,
                maxFee,
                maxPriorityFee,
                baseFeePerGas
        );
    }

    @Override
    public GasFee getGasFee() {
        val stats = getStats();

        val maxPriorityFeePerGas = stats.getMaxPriorityFee().getMedian()
                .add(stats.getMaxPriorityFee().getMin())
                .div(2);

        val maxFeePerGas = stats.getMaxFeePerGas().getMedian()
                .add(stats.getMaxFeePerGas().getMin())
                .div(2);

        return new GasFee(maxFeePerGas, maxPriorityFeePerGas);
    }

    @Override
    @SneakyThrows
    public LegacyGasFee getLegacyGasFee() {
        val stats = getStats();
        val gasPrice = stats.getGasPrice().getMedian()
                .add(stats.getGasPrice().getMin())
                .div(2);
        return new LegacyGasFee(gasPrice);
    }

    @SneakyThrows
    private Uint getLatestBlockNumber() {
        return uint256(client.ethBlockNumber().send().getBlockNumber());
    }

    private Uint getLatestBaseFeePerGas() {
        val block = getBlock(getLatestBlockNumber());
        val baseFeePerGas = block.getBaseFeePerGas();
        return baseFeePerGas == null ? uint256(0) : uint256(baseFeePerGas);
    }

    @SneakyThrows
    private EthBlock.Block getBlock(Uint blockNumber) {
        return client.ethGetBlockByNumber(
                DefaultBlockParameter.valueOf(blockNumber.toBigInteger()), true
        ).send().getResult();
    }

    @SneakyThrows
    private List<Transaction> getTransactions(Uint blockNumber) {
        val block = getBlock(blockNumber);
        return block.getTransactions().stream()
                .map(Transaction.class::cast).toList();
    }

    @SneakyThrows
    private List<Transaction> getLatestTransactions(int count) {
        val result = new ArrayList<Transaction>();
        var blockNumber = getLatestBlockNumber();
        while (result.size() < count) {
            result.addAll(getTransactions(blockNumber));
            blockNumber = blockNumber.sub(1);
        }
        return Lists.partition(result, count).get(0);
    }

    private static Uint getAverage(List<Uint> values) {
        return values.stream()
                .reduce(uint256(0), Uint::add)
                .div(values.size());
    }

    private static <T extends BigInteger> List<Uint> getTransactionValues(List<Transaction> transactions,
                                                                          Function<Transaction, T> remapper) {
        val result = transactions.stream()
                .filter(Objects::nonNull)
                .map(tryOrElse(remapper, BigInteger.ZERO))
                .filter(Objects::nonNull)
                .filter(value -> value.compareTo(BigInteger.ZERO) > 0)
                .sorted(BigInteger::compareTo)
                .map(Types::uint256)
                .toList();
        return !result.isEmpty() ? result : List.of(uint256(0));
    }

    @Value
    public static class GasFeeStats {
        Fee gasPrice;
        Fee maxFeePerGas;
        Fee maxPriorityFee;
        Uint baseFee;
    }

    @Value
    public static class Fee {
        Uint min, max, average, median;
    }

}
