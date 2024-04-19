package dev.klepto.kweb3.core.ethereum.rpc.io;

import kong.unirest.core.Config;
import kong.unirest.core.ContentType;
import kong.unirest.core.Unirest;
import kong.unirest.core.UnirestInstance;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.function.Consumer;

/**
 * Implementation of {@link Unirest} client for RPC communications.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Slf4j
@RequiredArgsConstructor
public class HttpConnection implements RpcConnection {

    private final String url;
    private final UnirestInstance unirest = new UnirestInstance(new Config());

    private @Setter Consumer<String> messageCallback;
    private @Setter Consumer<Throwable> errorCallback;
    private @Setter Runnable closeCallback;

    @Override
    public String url() {
        return url;
    }

    @Override
    public void open() {
        // Ignore, connection opens and closes on demand with every new request.
    }

    @Override
    public void close() {
        unirest.close();
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public void send(String message) {
        val request = unirest.post(url)
                .contentType(ContentType.APPLICATION_JSON)
                .body(message)
                .asStringAsync();

        request.whenComplete((response, throwable) -> {
            if (throwable != null) {
                if (errorCallback != null) {
                    errorCallback.accept(throwable);
                }
            } else {
                if (messageCallback != null) {
                    messageCallback.accept(response.getBody());
                }
            }
        });
    }

}
