package dev.klepto.kweb3.kotlin.multicall

import dev.klepto.kweb3.core.contract.Web3Contract
import dev.klepto.kweb3.core.contract.type.EthStructContainer
import dev.klepto.kweb3.core.ethereum.type.EthValue
import dev.klepto.kweb3.core.ethereum.type.primitive.EthAddress
import dev.klepto.kweb3.core.ethereum.type.primitive.EthBytes
import dev.klepto.kweb3.kotlin.CoroutineContractExecutor

/**
 * Executes multiple calls to the blockchain in a single transaction
 * through a use of *multicall* smart contract.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
interface MulticallContract : Web3Contract {
    /**
     * Executes multiple calls to the blockchain in a single transaction and
     * returns list of their return data. If [allowFailure] is set to `false`,
     * any failed call will cause this function to fail. If [allowFailure] is
     * set to `true`, failed calls will contain `null` return data in their
     * respective index of the resulting list.
     *
     * @param allowFailure signifies whether to allow individual calls to fail
     * @param calls the list of smart contract calls
     * @return an ordered list of return data from each call, possibly `null`
     *     for failed calls
     */
    suspend fun execute(
        allowFailure: Boolean,
        calls: List<Call>,
    ): List<EthBytes?>

    /**
     * A single smart contract call to be executed in a *multicall*
     * transaction.
     *
     * @param address the address of the smart contract
     * @param data the encoded call data represented as ethereum bytes
     */
    data class Call(
        val address: EthAddress,
        val data: EthBytes,
    ) : EthStructContainer

    companion object {
        /**
         * Gets the underlying [CoroutineContractExecutor] used to execute this
         * *multicall* transactions.
         */
        fun MulticallContract.contractExecutor(): CoroutineContractExecutor {
            val contractExecutor = client.contractExecutor
            require(contractExecutor is CoroutineContractExecutor) {
                "${this::class.simpleName} requires a ${CoroutineContractExecutor::class.simpleName}."
            }
            return contractExecutor
        }

        /**
         * Creates a new [MulticallBuilder2] for constructing a *multicall* request
         * using this *multicall* executor.
         *
         * @return a new multicall builder
         */
        inline fun <reified T : EthValue> MulticallContract.builder(): MulticallBuilder<T> {
            return MulticallBuilder(this)
        }
    }
}
