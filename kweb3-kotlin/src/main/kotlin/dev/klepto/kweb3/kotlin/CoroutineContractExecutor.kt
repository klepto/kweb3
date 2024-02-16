package dev.klepto.kweb3.kotlin

import dev.klepto.kweb3.core.Web3Result
import dev.klepto.kweb3.core.contract.ContractCall
import dev.klepto.kweb3.core.contract.DefaultContractExecutor
import dev.klepto.kweb3.kotlin.ContractCallExtensions.client
import dev.klepto.kweb3.kotlin.ContractCallExtensions.continuation
import dev.klepto.kweb3.kotlin.ContractCallExtensions.isSuspending
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Contract executor implementation supporting coroutine (suspend)
 * functions.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
class CoroutineContractExecutor : DefaultContractExecutor() {

    /**
     * Executes a contract function from start to finish with a lock provided
     * by [CoroutineWeb3Client] mutex.
     *
     * @param call the contract interface method call
     * @return the contract function execution result, the type must match
     *     return type of the interface method
     */
    override fun execute(call: ContractCall): Any {
        if (call.isSuspending()) {
            return this::executeSuspending.call(call, call.continuation())
        }

        return super.execute(call)
    }

    /**
     * Executes smart contract interface call from a suspending coroutine
     * context.
     *
     * @param call the contract interface method call
     * @return the decoded smart contract result
     */
    suspend fun executeSuspending(call: ContractCall): Any {
        call.client().mutex.withLock {
            val data = encode(call)
            val result = request(call, data)
            return decodeSuspending(call, result)
        }
    }

    /**
     * Suspend current coroutine until RPC result is received and decodes RPC
     * result into contract return type.
     *
     * @param call the contract interface method call
     * @param result the result of the RPC call
     * @return the decoded smart contract result
     */
    suspend fun decodeSuspending(call: ContractCall, result: Web3Result<String>): Any {
        val decoded = result.map { decodeResult(call, it) }

        return suspendCoroutine { continuation ->
            decoded.get { continuation.resume(it) }
                    .error { continuation.resumeWithException(it) }
        }
    }

}