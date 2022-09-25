package dev.klepto.kweb3.chain;

import lombok.Value;

/**
 * Contains information about an ethereum network chain.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Value
public class Chain {

    long chainId;
    String name;
    String tokenSymbol;
    String tokenAddress;
    String[] rpcUrls;

    public Chain(long chainId, String name, String tokenSymbol, String tokenAddress, String... rpcUrls) {
        this.chainId = chainId;
        this.name = name;
        this.tokenSymbol = tokenSymbol;
        this.tokenAddress = tokenAddress;
        this.rpcUrls = rpcUrls;
    }

}