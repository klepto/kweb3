package dev.klepto.kweb3.core.ethereum.subscribe;

import dev.klepto.kweb3.core.ethereum.rpc.RpcClient;
import dev.klepto.kweb3.core.ethereum.rpc.RpcMessage;
import dev.klepto.kweb3.core.ethereum.rpc.RpcRequest;
import dev.klepto.kweb3.core.ethereum.rpc.api.RpcApiRequestMessage;
import dev.klepto.kweb3.core.ethereum.type.data.EthBlock;
import dev.klepto.kweb3.core.ethereum.type.primitive.EthUint;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static dev.klepto.kweb3.core.ethereum.type.primitive.EthUint.uint256;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@RequiredArgsConstructor
public class FallbackEthereumSubscriber implements EthereumSubscriber {

    private final RpcClient client;
    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private final AtomicReference<EthUint> blockNumber = new AtomicReference<>(EthUint.ZERO);
    private final Queue<Consumer<EthBlock>> subscribers = new LinkedList<>();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    /**
     * Subscribes given consumer to new block headers.
     *
     * @param subscriber the consumer to be called when a new block header is received
     */
    @Override
    public void onBlock(Consumer<EthBlock> subscriber) {
        subscribers.add(subscriber);
        if (!initialized.getAndSet(true)) {
            client.request(new SubscriptionRequest());
        }
    }

    /**
     * Fetches a new block and emits it to all subscribers.
     *
     * @param blockNumber the block number to fetch and emit
     */
    public void emitNewBlock(EthUint blockNumber) {
        if (blockNumber.compareTo(this.blockNumber.get()) <= 0) {
            return;
        }

        client.ethGetBlockByNumber(blockNumber.toHex())
                .map(EthBlock::parse)
                .get(block -> {
                    this.blockNumber.set(block.number());
                    subscribers.forEach(subscriber -> subscriber.accept(block));
                });
    }

    /**
     * Initializes the fallback mechanism for endpoints that do not support <code>eth_subscribe</code>. Instead of
     * relying on receiving notifications from the endpoint about new blocks, this method initializes a periodic polling
     * mechanism that fetches the latest block number and emits it to all subscribers.
     */
    private void initFallback() {
        val pollingInterval = client.endpoint().settings().pollingInterval();
        executor.scheduleAtFixedRate(() -> {
            client.ethBlockNumber()
                    .map(EthUint::uint256)
                    .get(this::emitNewBlock);
        }, 0, pollingInterval.toMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * Closes the subscriber and stops all polling mechanisms.
     */
    public void close() {
        executor.shutdownNow();
    }

    /**
     * Subscription request for new block headers. This request never finishes, this ensures that for websocket
     * connections this request will keep receiving subscription events and that on endpoint change the request will
     * automatically get re-applied to the new endpoint.
     */
    private class SubscriptionRequest implements RpcRequest {
        private final AtomicReference<String> subscriptionId = new AtomicReference<>();

        @Override
        public boolean send(@NotNull RpcClient client) {
            client.ethSubscribe("newHeads", null, null)
                    .get(subscriptionId::set)
                    .error(cause -> initFallback());
            return true;
        }

        @Override
        public boolean isComplete(@NotNull RpcClient client, @NotNull RpcMessage message) {
            if (!(message instanceof RpcApiRequestMessage response)) {
                return false;
            }

            if (subscriptionId.get() == null) {
                return false;
            }

            if (!"eth_subscription".equals(response.method())) {
                return false;
            }

            if (!response.params().isJsonObject()) {
                return false;
            }

            val params = response.params().getAsJsonObject();
            if (!params.has("subscription") || !params.has("result")) {
                return false;
            }

            val subscription = params.get("subscription").getAsString();
            if (!subscriptionId.get().equals(subscription)) {
                return false;
            }

            val result = params.get("result").getAsJsonObject();
            if (!result.has("number")) {
                return false;
            }

            val blockNumber = uint256(result.get("number").getAsString());
            emitNewBlock(blockNumber);
            return false;
        }

        @Override
        public boolean onError(@NotNull RpcClient client, @NotNull Throwable error) {
            return false;
        }
    }

}
