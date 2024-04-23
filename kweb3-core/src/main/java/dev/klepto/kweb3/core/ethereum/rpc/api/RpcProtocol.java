package dev.klepto.kweb3.core.ethereum.rpc.api;

/**
 * Implementation of all supported JSON RPC API methods.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface RpcProtocol extends
        EthBlockNumber,
        EthCall,
        EthEstimateGas,
        EthGasPrice,
        EthGetBlock,
        EthGetLogs,
        EthSendRawTransaction,
        EthSubscribe {
}
