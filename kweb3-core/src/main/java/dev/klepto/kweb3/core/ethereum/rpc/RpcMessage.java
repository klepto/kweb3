package dev.klepto.kweb3.core.ethereum.rpc;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.With;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

/**
 * Marker interface for JSON protocol message, implemented by {@link RequestMessage} and {@link ResponseMessage}.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface RpcMessage {

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
            return GSON.fromJson(response, RequestMessage.class);
        }
        return GSON.fromJson(response, ResponseMessage.class);
    }

    /**
     * Represents ethereum JSON RPC request body.
     *
     * @param jsonrpc the json rpc version
     * @param id      the unique identifier of this request
     * @param method  the json rpc method
     * @param params  the json rpc method parameters
     */
    @With
    record RequestMessage(@NotNull String jsonrpc,
                          long id,
                          @NotNull String method,
                          @NotNull JsonElement params) implements RpcMessage {
        /**
         * Creates a new rpc request using default values.
         */
        public RequestMessage() {
            this(JSON_VERSION, 1, "", new JsonArray());
        }

        /**
         * Parameters builder that uses var-args for less-verbose request building.
         *
         * @param params the request parameter objects
         * @return a new request containing given parameter objects
         */
        public RequestMessage withParams(Object... params) {
            return new RequestMessage(jsonrpc, id, method, GSON.toJsonTree(params));
        }
    }

    /**
     * Represents ethereum JSON RPC response body.
     *
     * @param jsonrpc the json rpc version
     * @param id      the unique identifier of the request this response is responding to
     * @param result  the response result or null if error occurred
     * @param error   the error of the request or null if no error occurred
     */
    @With
    record ResponseMessage(@NotNull String jsonrpc,
                           long id,
                           @Nullable JsonElement result,
                           @Nullable Error error) implements RpcMessage {

        /**
         * Returns the result as string.
         *
         * @return the result as string
         */
        @Nullable
        public String resultAsString() {
            if (result == null) {
                return null;
            }
            if (result.isJsonPrimitive()) {
                return result.getAsString();
            }
            return result.toString();
        }

        /**
         * Returns the result as the specified type.
         *
         * @param type the class type to convert the result to
         * @return the result as the specified type
         */
        public <T> T resultAs(Class<T> type) {
            return GSON.fromJson(result, type);
        }

        /**
         * Returns the result as the specified type.
         *
         * @param type the type to convert the result to
         * @return the result as the specified type
         */
        public <T> T resultAs(Type type) {
            return GSON.fromJson(result, type);
        }

        /**
         * Represents JSON response error.
         *
         * @param code    the error code
         * @param message the error message
         */
        public record Error(int code, @NotNull String message) {
        }

    }


}
