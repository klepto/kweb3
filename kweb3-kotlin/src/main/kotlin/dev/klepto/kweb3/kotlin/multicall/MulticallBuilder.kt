package dev.klepto.kweb3.kotlin.multicall

/**
 * A type-bound builder for constructing a *multicall* request. This
 * builder enables individual call queueing via [call], [callRange] and
 * [callIndices], and allows to configure the *multicall* request with
 * various parameters such as executor, batch size and allow failure flag.
 *
 * @property executor the multicall executor
 * @property calls the list of calls
 * @property batchSize the maximum size of a single batch
 * @property allowFailure signifies whether to allow individual calls to
 *     fail
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
class MulticallBuilder<T>(
    private var executor: MulticallExecutor,
    private var calls: MutableList<suspend () -> T> = mutableListOf(),
    private var batchSize: Int = 1024,
    private var allowFailure: Boolean = false
) {

    /**
     * Sets the [executor] to be used with this *multicall* request.
     *
     * @param executor the multicall executor
     */
    fun executor(executor: MulticallExecutor): MulticallBuilder<T> {
        this.executor = executor
        return this
    }

    /**
     * Replaces the list of [calls] to be used with this *multicall* request.
     * Any previous calls in the [calls] queue will be discarded.
     *
     * @param calls the list of calls
     */
    fun calls(calls: List<suspend () -> T>): MulticallBuilder<T> {
        this.calls = calls.toMutableList()
        return this
    }

    /**
     * Sets the [batchSize] to be used with this *multicall* request. If calls
     * exceed given [batchSize], they will be partitioned and processed as
     * separate transactions.
     *
     * @param batchSize the maximum size of a single batch
     */
    fun batchSize(batchSize: Int): MulticallBuilder<T> {
        this.batchSize = batchSize
        return this
    }

    /**
     * Sets the [allowFailure] flag to be used with this multicall. If
     * [allowFailure] is set to `false`, any failed call will cause entire
     * *multicall* request to fail. If [allowFailure] is set to `true`, failed
     * calls will simply contain `null` as their result.
     *
     * @param allowFailure signifies whether to allow individual calls to fail
     */
    fun allowFailure(allowFailure: Boolean): MulticallBuilder<T> {
        this.allowFailure = allowFailure
        return this
    }

    /**
     * Appends a new call to the list of calls associated with this builder.
     *
     * @param call the call code
     */
    fun call(call: suspend () -> T): MulticallBuilder<T> {
        calls.add(call)
        return this
    }

    /**
     * Appends a new calls to the list by supplying range indices to a call
     * function.
     *
     * @param range the range of indices
     * @param call the call function
     */
    fun callRange(range: IntRange, call: suspend (index: Int) -> T): MulticallBuilder<T> {
        for (i in range) {
            calls.add(suspend { call(i) })
        }
        return this
    }

    /**
     * Appends a new calls to the list by supplying a list of indices to a call
     * function.
     *
     * @param indices the list of indices
     * @param call the call function
     */
    fun callIndices(indices: List<Int>, call: suspend (index: Int) -> T): MulticallBuilder<T> {
        for (i in indices) {
            calls.add(suspend { call(i) })
        }
        return this
    }

    /**
     * Builds a new [MulticallDispatcher] for list of calls associated with
     * this builder.
     *
     * @return a new multicall dispatcher
     */
    fun build(): MulticallDispatcher<T> {
        return MulticallDispatcher(executor, calls, batchSize, allowFailure)
    }

    /**
     * Dispatches all calls in the queue and returns a list of their results.
     * The results are ordered in the same way as the calls. If [allowFailure]
     * is set `true`, results may contain `null`, indicating a failed call.
     *
     * @return a list of results from each call
     */
    suspend fun execute(): List<T?> {
        return build().execute()
    }

}
