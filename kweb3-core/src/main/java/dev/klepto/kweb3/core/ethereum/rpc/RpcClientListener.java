package dev.klepto.kweb3.core.ethereum.rpc;

import org.jetbrains.annotations.NotNull;

public interface RpcClientListener {
    void onMessage(@NotNull RpcMessage message);

    void onError(@NotNull Throwable throwable);

    void onClose();
}