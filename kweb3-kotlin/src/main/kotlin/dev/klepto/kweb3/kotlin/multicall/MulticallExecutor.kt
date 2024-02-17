package dev.klepto.kweb3.kotlin.multicall

import dev.klepto.kweb3.core.Web3Result
import dev.klepto.kweb3.core.contract.ContractCall
import dev.klepto.kweb3.core.contract.DefaultContractExecutor
import dev.klepto.kweb3.core.contract.LoggingContractExecutor
import dev.klepto.kweb3.core.type.EthArray
import dev.klepto.kweb3.core.type.EthArray.array
import dev.klepto.kweb3.core.type.EthBytes
import dev.klepto.kweb3.core.type.EthUint
import dev.klepto.kweb3.core.type.EthUint.uint256
import dev.klepto.kweb3.core.util.Hex
import dev.klepto.kweb3.kotlin.CoroutineContractExecutor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * A suspending multicall executor that executes given [calls] through
 * provided multicall [contract] implementation.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
class MulticallExecutor<T>(private val contract: MulticallContract,
                           private val calls: List<suspend () -> T>) {

    private val defaultBatchSize = 512
    private val defaultGasLimit = uint256(1_000_000)
    private val defaultSizeLimit = uint256(1_000_000)

    /**
     * Executes all [calls] through a multicall smart [contract] and returns
     * an ordered list containing result of all calls. For calls that failed
     * to execute due to gas limit, size limit or internal error, a `null` is
     * preserved at a matching result index.
     *
     * @param batchSize the maximum size of a multicall batch
     * @param gasLimit the maximum amount of gas for a single multicall request
     *     is allowed to use (ignored if not supported by underlying smart
     *     contract)
     * @param sizeLimit the maximum size of return value for a single multicall
     *     request (ignored if not supported by underlying smart contract)
     * @return a list containing results of all successful and reverted calls
     */
    suspend fun execute(batchSize: Int = defaultBatchSize,
                        gasLimit: EthUint = defaultGasLimit,
                        sizeLimit: EthUint = defaultSizeLimit): List<T?> {
        require(batchSize > 0) { "Batch size must be positive." }
        val executor = contract.client.contracts.executor
        require(executor is CoroutineContractExecutor) {
            "${MulticallExecutor::class.simpleName} can only " +
                    "be used from ${CoroutineContractExecutor::class.simpleName} context."
        }

        val batches = calls.chunked(batchSize)
        return batches.flatMap {
            executeBatch(executor, it, gasLimit, sizeLimit)
        }
    }

    /**
     * Executes a single multicall [batch] using underlying multicall
     * [contract]. Every call in a given [batch] is first encoded into a single
     * multicall request, then multicall executes the request and dispatches
     * decoded values to back every call in the [batch].
     *
     * @param executor the underlying coroutine executor to be used for this
     *     request
     * @param batch a list of smart contract calls
     * @param gasLimit a gas limit parameter to pass to multicall smart
     *     contract
     * @param sizeLimit a size limit parameter to pass to multicall smart
     *     contract
     * @return an ordered list containing decoded multicall results or `null`
     *     for failed calls
     */
    private suspend fun executeBatch(executor: CoroutineContractExecutor,
                                     batch: List<suspend () -> T>,
                                     gasLimit: EthUint,
                                     sizeLimit: EthUint): List<T?> {
        val encodedCalls = encodeCalls(executor, batch)
        val results = contract.tryAggregate(gasLimit, sizeLimit, encodedCalls)
        return decodeResults(executor, batch, results)
    }

    /**
     * Encodes given list of smart contract [calls] using
     * [MutexLoggingInterceptor].
     *
     * @param executor the coroutine contract executor
     * @param calls the list of calls
     * @return an ethereum array of multicall calls
     */
    private suspend fun encodeCalls(executor: CoroutineContractExecutor,
                                    calls: List<suspend () -> T>): EthArray<MulticallContract.Call> {
        val mutex = Mutex()
        val logger = MutexLoggingInterceptor(mutex)
        val scope = CoroutineScope(Dispatchers.IO)

        executor.withInterceptor(logger) {
            calls.forEach { call ->
                mutex.lock()
                val job = scope.launch { call() }
                mutex.withLock {
                    job.cancel()
                }
            }
        }

        val result = logger.logs.map {
            MulticallContract.Call(it.contractAddress, EthBytes.bytes(it.data))
        }

        return array(result)
    }

    /**
     * Decodes given list of multicall [results] using
     * [ConstantResultInterceptor].
     *
     * @param executor the coroutine contract executor
     * @param calls the list of calls
     * @param results an ethereum array of results
     * @return the decoded list of multicall results
     */
    private suspend fun decodeResults(executor: CoroutineContractExecutor,
                                      calls: List<suspend () -> T>,
                                      results: EthArray<EthBytes>): List<T?> {
        require(calls.size == results.size) {
            "Results size does not match calls size."
        }

        val result = mutableListOf<T?>()
        calls.forEachIndexed { index, call ->
            val byteArray = results[index].toByteArray()
            if (byteArray.isEmpty()) {
                result.add(null)
                return@forEachIndexed
            }

            val value = Hex.toHex(byteArray)
            val interceptor = ConstantResultInterceptor(value)
            executor.withInterceptor(interceptor) {
                result.add(call())
            }
        }

        return result
    }

    /**
     * A [CoroutineContractExecutor] interceptor that unlocks a given [mutex]
     * after logging a request.
     *
     * @param mutex the mutex to be unlocked
     */
    private class MutexLoggingInterceptor(private val mutex: Mutex) : LoggingContractExecutor() {
        override fun request(call: ContractCall, data: String): Web3Result<String> {
            val result = super.request(call, data)
            mutex.unlock()
            return result
        }
    }

    /**
     * A [CoroutineContractExecutor] interceptor that returns a constant value
     * as a web3 result.
     *
     * @param value the value to be returned as a result
     */
    private class ConstantResultInterceptor(private val value: String) : DefaultContractExecutor() {
        override fun request(call: ContractCall, data: String): Web3Result<String> {
            val result = Web3Result<String>()
            result.complete(value)
            return result
        }
    }

}