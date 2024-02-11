package dev.klepto.kweb3.kotlin

import dev.klepto.kweb3.core.Web3Result
import dev.klepto.kweb3.core.contract.ContractCall
import dev.klepto.kweb3.core.contract.DefaultContractExecutor
import kotlin.coroutines.Continuation
import kotlin.coroutines.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Contract executor implementation supporting coroutine (suspend)
 * functions.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
class CoroutineContractExecutor : DefaultContractExecutor() {

    /**
     * Decodes RPC result into contract return type. If contract function's
     * last parameter is a [Continuation], this assumes that contract function
     * contains `suspend` keyword and process it as a coroutine, otherwise
     * defaults back to behavior implemented by [DefaultContractExecutor].
     *
     * @param call the contract interface method call
     * @param result the result of the RPC call
     * @return the already decoded, or to be decoded in the future contract
     *     result
     */
    @Suppress("UNCHECKED_CAST")
    override fun decode(call: ContractCall, result: Web3Result<String>): Any {
        val decoded = result.map { decodeResult(call, it) }

        val lastArg = call.args.last()
        if (lastArg is Continuation<*>) {
            val continuation = lastArg as Continuation<Any>
            decoded
                    .get { continuation.resume(it) }
                    .error { continuation.resumeWithException(it) }
            return COROUTINE_SUSPENDED
        }

        return super.decode(call, result)
    }

}