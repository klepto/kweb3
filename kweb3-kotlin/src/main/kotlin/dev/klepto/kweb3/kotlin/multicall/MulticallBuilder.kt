package dev.klepto.kweb3.kotlin.multicall

import dev.klepto.kweb3.core.contract.Web3Contract
import dev.klepto.kweb3.core.ethereum.type.EthValue
import dev.klepto.kweb3.core.ethereum.type.primitive.EthAddress
import dev.klepto.kweb3.core.ethereum.type.primitive.EthAddress.address
import kotlin.reflect.KClass

/**
 * @author Augustinas R. <http://github.com/klepto>
 */
@Suppress("UNCHECKED_CAST")
class MulticallBuilder<T : EthValue>(
    private var multicall: MulticallContract,
    private var calls: MutableList<MulticallExecutor.Call<Web3Contract>> = mutableListOf(),
    private var batchSize: Int = 1024,
    private var allowFailure: Boolean = false
) {

    /**
     * Sets the [multicall] contract to be used with this *multicall* request.
     *
     * @param multicall the multicall executor
     */
    fun multicall(multicall: MulticallContract): MulticallBuilder<T> {
        this.multicall = multicall
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
     * Creates a new [CallBuilder] for appending calls associated with a single
     * smart contract.
     */
    inline fun <reified C : Web3Contract> contract(
        address: String,
        noinline block: CallBuilder<C, T>.() -> Unit
    ): MulticallBuilder<T> {
        return contract(address(address), block)
    }

    /**
     * Creates a new [CallBuilder] for appending calls associated with a single
     * smart contract.
     */
    inline fun <reified C : Web3Contract> contract(
        address: EthAddress,
        noinline block: CallBuilder<C, T>.() -> Unit
    ): MulticallBuilder<T> {
        return contract(C::class, address, block)
    }

    /**
     * Creates a new [CallBuilder] for appending calls associated with a single
     * smart contract.
     */
    fun <C : Web3Contract> contract(
        contractType: KClass<C>,
        address: EthAddress,
        block: CallBuilder<C, T>.() -> Unit
    ): MulticallBuilder<T> {
        val builder = CallBuilder<C, T>(contractType::java.get(), address)
        block(builder)
        calls.addAll(builder.build().map { it as MulticallExecutor.Call<Web3Contract> })
        return this
    }

    /**
     * Builds a new [MulticallExecutor] for list of calls associated with this
     * builder.
     *
     * @return a new multicall executor
     */
    fun build(): MulticallExecutor<T> {
        return MulticallExecutor(multicall, calls, batchSize, allowFailure)
    }

    /**
     * Executes all calls in the queue and returns a list of their results. The
     * results are ordered in the same way as the calls. If [allowFailure] is
     * set `true`, results may contain `null`, indicating a failed call.
     *
     * @return a list of results from each call
     */
    suspend fun execute(): List<T?> {
        return build().execute()
    }


    /**
     * A builder for constructing a list of calls associated with a single
     * smart contract.
     */
    class CallBuilder<C : Web3Contract, T : EthValue>(
        private val contractType: Class<C>,
        private val contractAddress: EthAddress,
        private var calls: MutableList<suspend C.() -> T> = mutableListOf()
    ) {
        /**
         * Appends a new call to the list of calls associated with this contract
         * builder.
         *
         * @param block the call code
         */
        fun call(block: suspend C.() -> T) {
            calls.add(block)
        }

        /**
         * Builds a list of [MulticallExecutor.Call] objects from the list of calls
         * within this builder.
         */
        fun build(): List<MulticallExecutor.Call<C>> {
            return calls.map {
                MulticallExecutor.Call(contractType, contractAddress, it)
            }
        }
    }

}