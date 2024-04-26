package dev.klepto.kweb3.core.chain.endpoint;

import dev.klepto.kweb3.core.chain.Web3Chain;
import dev.klepto.kweb3.core.chain.Web3Chain.Currency;
import dev.klepto.kweb3.core.chain.Web3Endpoint;
import dev.klepto.kweb3.core.chain.Web3Endpoint.Settings;
import dev.klepto.kweb3.core.chain.Web3Transport;

import java.time.Duration;

/**
 * A pre-defined list of endpoints provided by <a href="https://publicnode.com">PublicNode</a>.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public final class PublicNode {

    private PublicNode() {
    }

    /**
     * Creates a websocket PublicNode endpoint for the given chain.
     *
     * @param name  the name of the endpoint
     * @param chain the chain of the endpoint
     * @return a web3 endpoint
     */
    private static Web3Endpoint publicNodeEndpoint(String name, Web3Chain chain) {
        return Web3Endpoint.builder()
                .chain(chain)
                .transport(Web3Transport.WEBSOCKET)
                .url("wss://" + name + "-rpc.publicnode.com")
//                .transport(Web3Transport.HTTP)
//                .url("https://" + name + "-rpc.publicnode.com")
                .settings(new Settings(null, null, Duration.ofMillis(200), null))
                .build();
    }

    /**
     * Ethereum mainnet.
     */
    public static final Web3Endpoint ETHEREUM = publicNodeEndpoint("ethereum",
            Web3Chain.builder()
                    .name("Ethereum")
                    .chainId(1)
                    .currency(new Currency("Ether", "ETH", 18))
                    .build()
    );

    /**
     * BNB smart chain.
     */
    public static final Web3Endpoint BSC = publicNodeEndpoint("bsc",
            Web3Chain.builder()
                    .name("BNB Smart Chain")
                    .chainId(56)
                    .currency(new Currency("BNB", "BNB", 18))
                    .build()
    );

    /**
     * Polygon chain.
     */
    public static final Web3Endpoint POLYGON = publicNodeEndpoint("polygon-bor",
            Web3Chain.builder()
                    .name("Polygon")
                    .chainId(137)
                    .currency(new Currency("Matic", "MATIC", 18))
                    .build()
    );

    /**
     * Avalanche C chain.
     */
    public static final Web3Endpoint AVALANCHE = publicNodeEndpoint("avalanche-c-chain",
            Web3Chain.builder()
                    .name("Avalanche C")
                    .chainId(43114)
                    .currency(new Currency("AVAX", "AVAX", 18))
                    .build()
    );

    /**
     * Arbitrum One chain.
     */
    public static final Web3Endpoint ARBITRUM = publicNodeEndpoint("arbitrum-one",
            Web3Chain.builder()
                    .name("Arbitrum One")
                    .chainId(42161)
                    .currency(ETHEREUM.chain().currency())
                    .build()
    );

    /**
     * Base chain.
     */
    public static final Web3Endpoint BASE = publicNodeEndpoint("base",
            Web3Chain.builder()
                    .name("Base Mainnet")
                    .chainId(8453)
                    .currency(ETHEREUM.chain().currency())
                    .build()
    );

    /**
     * Optimism chain.
     */
    public static final Web3Endpoint OPTIMISM = publicNodeEndpoint("optimism",
            Web3Chain.builder()
                    .name("Optimism")
                    .chainId(10)
                    .currency(new Currency("Optimism", "OP", 18))
                    .build()
    );

}
