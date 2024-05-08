package dev.klepto.kweb3.core.ethereum.subscribe;

import dev.klepto.kweb3.core.ethereum.type.data.EthBlock;

import java.util.function.Consumer;

/**
 * A subscriber for ethereum blockchain events.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthereumSubscriber {

    /**
     * Subscribes given consumer to new block headers.
     *
     * @param subscriber the consumer to be called when a new block header is received
     */
    void onBlock(Consumer<EthBlock> subscriber);

    /**
     * Closes the subscriber and releases all resources.
     */
    void close();

}
