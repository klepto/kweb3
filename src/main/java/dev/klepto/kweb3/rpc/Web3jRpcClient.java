package dev.klepto.kweb3.rpc;

import dev.klepto.kweb3.Web3Network;
import dev.klepto.kweb3.rpc.api.RpcEthCall;
import dev.klepto.kweb3.rpc.api.RpcEthEstimateGas;
import dev.klepto.kweb3.rpc.api.RpcEthGasPrice;
import dev.klepto.kweb3.rpc.api.RpcEthSendRawTransaction;
import lombok.RequiredArgsConstructor;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@RequiredArgsConstructor
public class Web3jRpcClient implements RpcClient {

    private final Web3Network network;

    @Override
    public RpcEthCall.Response ethCall(RpcEthCall.Request request) {
        // TODO: Implement
        return null;
    }

    @Override
    public RpcEthEstimateGas.Response ethEstimateGas(RpcEthEstimateGas.Request request) {
        // TODO: Implement
        return null;
    }

    @Override
    public RpcEthGasPrice.Response ethGasPrice() {
        // TODO: Implement
        return null;
    }

    @Override
    public RpcEthSendRawTransaction.Response ethSendRawTransaction(RpcEthSendRawTransaction.Request request) {
        // TODO: Implement
        return null;
    }

}
