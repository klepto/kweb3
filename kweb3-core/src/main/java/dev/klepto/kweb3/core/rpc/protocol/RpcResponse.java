package dev.klepto.kweb3.core.rpc.protocol;

import com.google.gson.JsonElement;
import lombok.With;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

/**
 * Represents ethereum JSON RPC response body.
 *
 * @param jsonrpc the json rpc version
 * @param id      the unique identifier of the request this response is responding to
 * @param result  the response result or null if error occurred
 * @param error   the error of the request or null if no error occurred
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@With
public record RpcResponse(@NotNull String jsonrpc,
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
        return RpcProtocol.GSON.fromJson(result, type);
    }

    /**
     * Returns the result as the specified type.
     *
     * @param type the type to convert the result to
     * @return the result as the specified type
     */
    public <T> T resultAs(Type type) {
        return RpcProtocol.GSON.fromJson(result, type);
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
