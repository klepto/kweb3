package dev.klepto.kweb3.kotlin

import dev.klepto.kweb3.core.contract.ContractCall
import kotlin.coroutines.Continuation

/**
 * Extensions for [ContractCall].
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
object ContractCallExtensions {

    /**
     * Returns `true` if contract call is a suspending function called from
     * coroutine context.
     */
    fun ContractCall.isSuspending(): Boolean {
        return args.last() is Continuation<*>
    }

    /**
     * Returns [Continuation] associated with this contract call or `null` if
     * this contract call is not suspending.
     */
    @Suppress("UNCHECKED_CAST")
    fun ContractCall.continuation(): Continuation<Any>? {
        if (isSuspending()) {
            return args.last() as Continuation<Any>
        }

        return null
    }

}