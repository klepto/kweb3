package dev.klepto.kweb3;

/**
 * Represents a web3 network/chain.
 *
 * @param rpcUrl  the RPC address of HTTP or WebSocket protocol
 * @param chainId the chain id of the network
 */
public record Web3Network(String rpcUrl, long chainId) {
}
