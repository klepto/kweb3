package dev.klepto.kweb3.core.ethereum.rpc.protocol;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.klepto.kweb3.core.ethereum.rpc.protocol.api.*;
import lombok.val;

public interface RpcProtocol extends
        EthBlockNumber,
        EthCall,
        EthEstimateGas,
        EthGasPrice,
        EthGetLogs,
        EthSendRawTransaction,
        EthSubscribe,
        EthGetBlock {

    /**
     * {@link Gson} instance for json encoding/decoding.
     */
    Gson GSON = new Gson();

    /**
     * Default JSON RPC version.
     */
    String JSON_VERSION = "2.0";

    /**
     * Encodes a JSON message into a string.
     *
     * @param request the request message to encode
     * @return a string representation of the request message
     */
    static String encode(RpcMessage request) {
        return GSON.toJson(request);
    }

    /**
     * Decodes a JSON message from a string.
     *
     * @param response the response string to decode
     * @return a JSON message object
     */
    static RpcMessage decode(String response) {
        val object = GSON.fromJson(response, JsonObject.class);
        if (object.has("method")) {
            return GSON.fromJson(response, RpcRequest.class);
        }
        return GSON.fromJson(response, RpcResponse.class);
    }

}
