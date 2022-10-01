package dev.klepto.kweb3.chain;


/**
 * Default chain definitions. TODO: Add list of most commonly used chains.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Chains {

    public static final Chain POLYGON = new Chain(
            137,
            "Polygon",
            "MATIC",
            "0x0d500b1d8e8ef31e21c99d1db9a6444d3adf1270",
            "https://rpc-mainnet.matic.quiknode.pro"
    );

    public static final Chain BSC_TESTNET = new Chain(
            97,
            "BSC Testnet",
            "BNB",
            "0xae13d989daC2f0dEbFf460aC112a837C89BAa7cd",
            "https://data-seed-prebsc-1-s1.binance.org:8545"
    );

    public static final Chain CRONOS = new Chain(
            25,
            "Cronos",
            "CRO",
            "0x5c7f8a570d578ed84e63fdfa7b1ee72deae1ae23",
            "https://node.croswap.com/rpc"
    );

}
