package dev.klepto.kweb3.kotlin.multicall

import dev.klepto.kweb3.core.Web3Result
import dev.klepto.kweb3.core.contract.ContractCall
import dev.klepto.kweb3.core.ethereum.type.EthValue
import dev.klepto.kweb3.core.ethereum.type.primitive.EthBytes
import dev.klepto.kweb3.core.util.Hex
import dev.klepto.kweb3.kotlin.CoroutineContractExecutor
import dev.klepto.kweb3.kotlin.CoroutineWeb3Client
import dev.klepto.kweb3.kotlin.multicall.MulticallExecutor.Companion.contractExecutor
import java.util.*
import kotlin.math.min

/**
 * Batches and sends *multicall* requests to the target [executor]
 * and returns decoded responses. Information about request encoding
 * and response decoding is generated automatically by the help of
 * *coroutines*. For this reason, calls only support smart contract
 * implementations containing `suspend` keyword.
 *
 * @param executor the [MulticallExecutor] implementation
 * @param calls the list of suspending smart contract calls
 * @param batchSize the maximum size of a single batch
 * @param allowFailure indicates whether to allow failed calls
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
class MulticallDispatcher<T : EthValue>(
    private val executor: MulticallExecutor,
    private val calls: List<suspend () -> T>,
    private val batchSize: Int,
    private var allowFailure: Boolean
) {

    /**
     * Dispatches all calls in the queue and returns a list of their results.
     * The results are ordered in the same way as the calls. If [allowFailure]
     * is set `true`, results may contain `null`, indicating a failed call.
     *
     * @return a list of results from each call
     */
    suspend fun execute(): List<T?> {
        require(batchSize > 0) { "Batch size must be positive." }
        val results = mutableListOf<T?>()
        val callQueue = LinkedList(calls)
        while (!callQueue.isEmpty()) {
            val batch = callQueue.subList(0, min(batchSize, callQueue.size))
            val result = executeBatch(batch)
            for (i in result.indices) {
                callQueue.pop()
            }
            results.addAll(result)
        }
        return results
    }


    /**
     * Dispatches a single batch of calls and returns a list of their results.
     *
     * @param batch the list of calls to be dispatched
     * @return a list of results from each call
     */
    private suspend fun executeBatch(batch: List<suspend () -> T>): List<T?> {
        val encodedCalls = encodeCalls(batch)
        val results = executor.execute(allowFailure, encodedCalls)
        return decodeResults(batch, results)
    }


    /**
     * Encodes all calls in the queue and returns a list of their encoded
     * [MulticallExecutor.Call] objects.
     *
     * @param calls the list of calls to be encoded
     * @return a list of encoded calls
     */
    private suspend fun encodeCalls(calls: List<suspend () -> T>): List<MulticallExecutor.Call> {
        val client = executor.client
        require(client is CoroutineWeb3Client) { "Client must be a CoroutineWeb3Client." }
        return client.log(calls).map {
            MulticallExecutor.Call(it.transaction.to, it.transaction.data)
        }
    }

    /**
     * Decodes all results in the queue and returns a list of their decoded
     * responses.
     *
     * @param calls the list of calls to be decoded
     * @param results the list of results to be decoded
     * @return a list of decoded results
     */
    private suspend fun decodeResults(
        calls: List<suspend () -> T>,
        results: List<EthBytes?>
    ): List<T?> {
        val result = mutableListOf<T?>()
        results.forEachIndexed { index, data ->
            if (data == null) {
                result.add(null)
                return@forEachIndexed
            }

            val byteArray = data.toByteArray()
            if (byteArray.isEmpty()) {
                result.add(null)
                return@forEachIndexed
            }

            val value = Hex.toHex(byteArray)
            val interceptor = ConstantResultInterceptor(value)
            executor.contractExecutor().withInterceptor(interceptor) {
                if (allowFailure) {
                    try {
                        result.add(calls[index]())
                    } catch (e: Exception) {
                        result.add(null)
                    }
                } else {
                    result.add(calls[index]())
                }
            }
        }
        return result
    }

    /**
     * A [CoroutineContractExecutor] interceptor that returns a constant value
     * as a web3 result.
     *
     * @param value the value to be returned as a result
     */
    private class ConstantResultInterceptor(private val value: String) : CoroutineContractExecutor() {
        override fun request(call: ContractCall, data: String): Web3Result<String> {
            val result = Web3Result<String>()
            result.complete(value)
            return result
        }
    }

}