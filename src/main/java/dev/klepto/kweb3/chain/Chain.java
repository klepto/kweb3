package dev.klepto.kweb3.chain;

import lombok.Value;
import lombok.With;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Value
@With
public class Chain {

    long chainId;
    String name;
    String tokenSymbol;
    String tokenAddress;
    String rpcUrl;

    public Chain() {
        this.chainId = -1;
        this.name = null;
        this.tokenSymbol = null;
        this.tokenAddress = null;
        this.rpcUrl = null;
    }

    public Chain(long chainId, String name, String tokenSymbol, String tokenAddress, String rpcUrl) {
        this.chainId = chainId;
        this.name = name;
        this.tokenSymbol = tokenSymbol;
        this.tokenAddress = tokenAddress;
        this.rpcUrl = rpcUrl;
    }

}
