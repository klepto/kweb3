package dev.klepto.kweb3.core;

import lombok.experimental.Delegate;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Asynchronous result backed by {@link CompletableFuture}.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Web3Result<T> implements CompletionStage<T> {

    @Delegate
    private final CompletionStage<T> stage;

    /**
     * Creates new, uncompleted web3 result.
     */
    public Web3Result() {
        this(new CompletableFuture<>());
    }

    /**
     * Creates new web3 result with given completion stage.
     *
     * @param stage the completion stage
     */
    private Web3Result(CompletionStage<T> stage) {
        this.stage = stage;
    }

    /**
     * Returns {@code true} if completed in any fashion: normally, exceptionally, or via cancellation.
     *
     * @return {@code true} if completed
     */
    public boolean isDone() {
        return stage.toCompletableFuture().isDone();
    }

    /**
     * Completes the result with the given value.
     *
     * @param value the result completion value
     */
    public void complete(@NotNull T value) {
        stage.toCompletableFuture().complete(value);
    }

    /**
     * Completes the result with given {@link Throwable} to indicate an error.
     *
     * @param cause the cause of the error
     */
    public void completeExceptionally(@NotNull Throwable cause) {
        stage.toCompletableFuture().completeExceptionally(cause);
    }

    /**
     * Cancels the result by completing it with {@link Web3Error}.
     */
    public void cancel() {
        stage.toCompletableFuture().cancel(true);
    }

    /**
     * Waits indefinitely if necessary for the result to complete, and then retrieves it.
     *
     * @return the result
     * @throws Web3Error if the computation threw an exception
     */
    @NotNull
    public T get() {
        try {
            return stage.toCompletableFuture().get();
        } catch (Throwable cause) {
            throw new Web3Error(cause);
        }
    }

    /**
     * Registers a consumer to be called when result is complete.
     *
     * @param consumer the result consumer
     */
    @NotNull
    public Web3Result<T> get(@NotNull Consumer<T> consumer) {
        val newStage = stage.whenComplete((result, error) -> {
            if (result != null) {
                consumer.accept(result);
            }
        });
        return new Web3Result<>(newStage);
    }

    /**
     * Registers a consumer to be called when result is complete.
     *
     * @param consumer the result consumer
     */
    @NotNull
    public Web3Result<T> error(@NotNull Consumer<Throwable> consumer) {
        val newStage = stage.whenComplete((result, error) -> {
            if (error != null) {
                consumer.accept(error);
            }
        });
        return new Web3Result<>(newStage);
    }

    /**
     * Creates a new Web3Result that maps the result using the given mapping function.
     *
     * @param mapper the mapping function
     * @return new Web3Result that will produce remapped result
     */
    @NotNull
    public <R> Web3Result<R> map(@NotNull Function<T, R> mapper) {
        val newStage = stage.thenApply(mapper);
        return new Web3Result<>(newStage);
    }

}
