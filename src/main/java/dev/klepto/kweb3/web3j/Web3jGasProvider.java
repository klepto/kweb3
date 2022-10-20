package dev.klepto.kweb3.web3j;

import dev.klepto.kweb3.gas.GasFee;
import dev.klepto.kweb3.gas.GasFeeProvider;
import dev.klepto.kweb3.gas.LegacyGasFee;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.val;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.Transaction;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static dev.klepto.kweb3.type.sized.Uint256.uint256;
import static dev.klepto.kweb3.util.Functional.tryOrElse;
import static dev.klepto.kweb3.util.Logging.debug;

/**
 * Default gas price provider. Selects gas price based on gas prices used in the latest block. Selected price is always
 * in-between minimum and median gas price.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@RequiredArgsConstructor
public class Web3jGasProvider implements GasFeeProvider {

    private final Web3j client;

    @SneakyThrows
    public GasFeeStats getStats() {
        val blockNumber = client.ethBlockNumber().send().getBlockNumber();
        val block = client.ethGetBlockByNumber(
                DefaultBlockParameter.valueOf(blockNumber),
                true
        ).send().getResult();

        val baseFeePerGas = block.getBaseFeePerGas() == null
                ? BigInteger.ZERO
                : block.getBaseFeePerGas().multiply(BigInteger.TEN.pow(9));

        val transactions = block.getTransactions().stream()
                .map(Transaction.class::cast)
                .collect(Collectors.toList());

        var gasPrices = transactions.stream()
                .map(tryOrElse(Transaction::getGasPrice, BigInteger.ZERO))
                .filter(price -> price.compareTo(BigInteger.ZERO) > 0)
                .sorted(BigInteger::compareTo)
                .collect(Collectors.toList());
        if (gasPrices.isEmpty()) {
            gasPrices = List.of(BigInteger.ZERO);
        }

        var maxFeesPerGas = transactions.stream()
                .map(tryOrElse(Transaction::getMaxFeePerGas, BigInteger.ZERO))
                .filter(Objects::nonNull)
                .filter(price -> price.compareTo(BigInteger.ZERO) > 0)
                .sorted(BigInteger::compareTo)
                .collect(Collectors.toList());
        if (maxFeesPerGas.isEmpty()) {
            maxFeesPerGas = List.of(BigInteger.ZERO);
        }

        var maxPriorityFeesPerGas = transactions.stream()
                .map(tryOrElse(Transaction::getMaxPriorityFeePerGas, BigInteger.ZERO))
                .filter(Objects::nonNull)
                .filter(price -> price.compareTo(BigInteger.ZERO) > 0)
                .sorted(BigInteger::compareTo)
                .collect(Collectors.toList());
        if (maxPriorityFeesPerGas.isEmpty()) {
            maxPriorityFeesPerGas = List.of(BigInteger.ZERO);
        }

        val minGasPrice = gasPrices.get(0);
        val minMaxFee = maxFeesPerGas.get(0);
        val minMaxPriorityFee = maxPriorityFeesPerGas.get(0);
        val maxGasPrice = gasPrices.get(gasPrices.size() - 1);
        val maxMaxFee = maxFeesPerGas.get(maxFeesPerGas.size() - 1);
        val maxMaxPriorityFee = maxPriorityFeesPerGas.get(maxPriorityFeesPerGas.size() - 1);


        val avgGasPrice = gasPrices.stream()
                .reduce(BigInteger.ZERO, BigInteger::add)
                .divide(BigInteger.valueOf(gasPrices.size()));

        val avgMaxFee = maxFeesPerGas.stream()
                .reduce(BigInteger.ZERO, BigInteger::add)
                .divide(BigInteger.valueOf(maxFeesPerGas.size()));

        val avgMaxPriorityFee = maxPriorityFeesPerGas.stream()
                .reduce(BigInteger.ZERO, BigInteger::add)
                .divide(BigInteger.valueOf(maxPriorityFeesPerGas.size()));

        val medianGasPrice = gasPrices.get(gasPrices.size() / 2);
        val medianMaxFee = maxFeesPerGas.get(maxFeesPerGas.size() / 2);
        val medianMaxPriorityFee = maxPriorityFeesPerGas.get(maxPriorityFeesPerGas.size() / 2);

        val gasPrice = new Fee(minGasPrice, maxGasPrice, avgGasPrice, medianGasPrice);
        val maxFee = new Fee(minMaxFee, maxMaxFee, avgMaxFee, medianMaxFee);
        val maxPriorityFee = new Fee(
                minMaxPriorityFee,
                maxMaxPriorityFee,
                avgMaxPriorityFee,
                medianMaxPriorityFee
        );

        debug("Latest gas price: {}", gasPrice);
        debug("Latest max fee: {}", maxFee);
        debug("Latest priority fee: {}", maxPriorityFee);
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
                .divide(BigInteger.TWO);

        val maxFeePerGas = stats.getMaxFeePerGas().getMedian()
                .add(stats.getMaxFeePerGas().getMin())
                .divide(BigInteger.TWO);

        return new GasFee(uint256(maxFeePerGas), uint256(maxPriorityFeePerGas));
    }

    @Override
    public LegacyGasFee getLegacyGasFee() {
        val stats = getStats();
        val gasPrice = stats.getGasPrice().getMedian()
                .add(stats.getGasPrice().getMin())
                .divide(BigInteger.TWO);

        return new LegacyGasFee(uint256(gasPrice));
    }

    @Value
    public static class GasFeeStats {

        Fee gasPrice;
        Fee maxFeePerGas;
        Fee maxPriorityFee;
        BigInteger baseFee;

    }

    @Value
    public static class Fee {
        BigInteger min, max, average, median;
    }

}
