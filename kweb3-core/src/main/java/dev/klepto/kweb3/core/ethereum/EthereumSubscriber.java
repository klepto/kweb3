package dev.klepto.kweb3.core.ethereum;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.util.concurrent.Uninterruptibles;
import dev.klepto.kweb3.core.ethereum.rpc.RpcClient;
import dev.klepto.kweb3.core.ethereum.type.data.EthBlock;
import dev.klepto.kweb3.core.ethereum.type.primitive.EthUint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.io.Closeable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Produces and dispatches Ethereum events.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Slf4j
@RequiredArgsConstructor
@SuppressWarnings({"unchecked", "rawtypes"})
public class EthereumSubscriber implements Closeable {

    private final RpcClient rpc;
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final Multimap<SubscriberType, Consumer<?>> subscribers = Multimaps.newSetMultimap(
            new ConcurrentHashMap<>(),
            ConcurrentHashMap::newKeySet
    );

    private EthUint blockNumber = EthUint.ZERO;

    /**
     * Starts the subscriber.
     */
    public void start() {
        executor.scheduleAtFixedRate(this::update, 0, 100, TimeUnit.MILLISECONDS);
    }

    /**
     * Fetches and dispatches events for all subscribers.
     */
    private void update() {
        val pollingRate = rpc.endpoint().settings().pollingInterval();
        if (subscribers.isEmpty()) {
            return;
        }

        try {
            rpc.ethGetBlockByNumber("latest").get(response -> {
                updateNewBlocks(EthBlock.parse(response));
            });
        } catch (Exception cause) {
            log.error("Failed to fetch new blocks", cause);
        }

        Uninterruptibles.sleepUninterruptibly(pollingRate);
    }

    /**
     * Updates all subscribers with the new block.
     *
     * @param block the new block
     */
    private void updateNewBlocks(EthBlock block) {
        if (block.number().compareTo(blockNumber) <= 0) {
            return;
        }

        blockNumber = block.number();
        subscribers.get(SubscriberType.NEW_BLOCKS).forEach(consumer -> {
            ((Consumer) consumer).accept(block);
        });
    }

    /**
     * Subscribes to a specific type of event.
     *
     * @param type     the type of event
     * @param consumer the consumer to be called when the event is received
     */
    public void subscribe(SubscriberType type, Consumer<?> consumer) {
        subscribers.put(type, consumer);
    }

    /**
     * Unsubscribes given consumer from all events.
     *
     * @param consumer the consumer to unsubscribe
     */
    public void unsubscribe(Consumer<?> consumer) {
        subscribers.keySet().forEach(key -> {
            subscribers.remove(key, consumer);
        });
    }

    /**
     * Stops all subscriptions.
     */
    @Override
    public void close() {
        executor.shutdownNow();
    }

    /**
     * Supported subscriber types.
     */
    public enum SubscriberType {
        NEW_BLOCKS
    }

}
