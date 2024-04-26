package dev.klepto.kweb3.core.ethereum.rpc.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.klepto.kweb3.core.ethereum.rpc.RpcMessage;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

import static dev.klepto.kweb3.core.util.Conditions.require;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface RpcApiMessage extends RpcMessage {

    /**
     * {@link Gson} instance for json encoding/decoding.
     */
    Gson GSON = new Gson();

    /**
     * Default JSON RPC version.
     */
    String JSON_VERSION = "2.0";

    /**
     * Returns the message id, or <code>-1</code> if the message does not have an id.
     *
     * @return the message id or <code>-1</code>
     */
    long id();

    /**
     * Returns the JSON-RPC version.
     *
     * @return the JSON-RPC version
     */
    default String jsonrpc() {
        return JSON_VERSION;

    }

    /**
     * Serializes the message into a JSON string.
     *
     * @return the JSON string representation of the message
     */
    default String serialize() {
        return GSON.toJson(this);
    }

    /**
     * Encodes a message into a string.
     *
     * @param request the request message to encode
     * @return a string representation of the request message
     */
    static String encode(RpcApiMessage request) {
        return GSON.toJson(request);
    }

    /**
     * Encodes an array of messages into a string.
     *
     * @param requests the request messages to encode
     * @return a string representation of the request messages
     */
    static String encode(List<RpcApiMessage> requests) {
        return GSON.toJson(requests);
    }

    /**
     * Decodes an array of messages from a string.
     *
     * @param response the response string to decode
     * @return a messages array
     */
    static List<RpcApiMessage> decode(String response) {
        val element = GSON.fromJson(response, JsonElement.class);
        if (element.isJsonObject()) {
            return List.of(decode(element.getAsJsonObject()));
        }

        require(element.isJsonArray(), "Response is not a valid JSON array.");
        val array = element.getAsJsonArray();
        val messages = new ArrayList<RpcApiMessage>(array.size());
        for (int i = 0; i < array.size(); i++) {
            messages.add(decode(array.get(i).getAsJsonObject()));
        }
        return messages;
    }

    /**
     * Decodes a message from a JSON object.
     *
     * @param object the response object to decode
     * @return an RPC message object
     */
    static RpcApiMessage decode(JsonObject object) {
        if (object.has("method")) {
            return GSON.fromJson(object, RpcApiRequestMessage.class);
        }
        return GSON.fromJson(object, RpcApiResponseMessage.class);
    }

}
