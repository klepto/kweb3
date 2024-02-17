package dev.klepto.kweb3.kotlin.multicall

/**
 * A type-bound multicall builder managing a mutable list of calls.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
class MulticallBuilder<T>(private val contract: MulticallContract,
                          private val calls: MutableList<suspend () -> T> = mutableListOf()) {

    /**
     * Appends a new call to the list of calls associated with builder.
     *
     * @return this builder instance
     */
    fun call(call: suspend () -> T): MulticallBuilder<T> {
        calls.add(call)
        return this
    }

    /**
     * Creates a new [MulticallExecutor] for list of calls associated with this
     * builder.
     *
     * @return a new instance of multicall executor using calls from this
     *     builder
     */
    fun build(): MulticallExecutor<T> {
        return MulticallExecutor(contract, calls)
    }

    /**
     * Creates a new [MulticallExecutor] for list of calls associated with this
     * builder and executes it.
     *
     * @return a list containing call results
     */
    suspend fun execute(): List<T?> {
        return build().execute()
    }

}