package dev.klepto.kweb3.kotlin

import dev.klepto.kweb3.core.Web3Error
import dev.klepto.kweb3.core.contract.ContractCall
import dev.klepto.kweb3.core.contract.ReflectionContractExecutor
import dev.klepto.kweb3.kotlin.ContractCallExtensions.continuation
import dev.klepto.kweb3.kotlin.ContractCallExtensions.isSuspending
import kotlinx.coroutines.future.await

/**
 * Contract executor implementation supporting coroutine (suspend)
 * functions.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
open class CoroutineContractExecutor : ReflectionContractExecutor() {

    /**
     * Executes a contract function from start to finish with a lock provided
     * by [CoroutineWeb3Client] mutex.
     *
     * @param call the contract interface method call
     * @return the contract function execution result, the type must match
     *     return type of the interface method
     */
    override fun execute(call: ContractCall): Any {
        try {
            if (call.isSuspending()) {
                return ::executeSuspending.call(call, call.continuation())
            }
        } catch (cause: Throwable) {
            throw Web3Error.unwrap(cause)
        }
        return super.execute(call)
    }

    /**
     * Executes smart contract interface call by suspending current coroutine
     * until result is received.
     *
     * @param call the contract interface method call
     * @return the decoded smart contract result
     */
    suspend fun executeSuspending(call: ContractCall): Any {
        val data = encode(call)
        val result = request(call, data).await()
        return decodeResult(call, result)
    }

}
