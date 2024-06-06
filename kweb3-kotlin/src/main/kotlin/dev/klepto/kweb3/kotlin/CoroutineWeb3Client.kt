package dev.klepto.kweb3.kotlin

import dev.klepto.kweb3.core.Web3Client
import dev.klepto.kweb3.core.chain.Web3Endpoint
import dev.klepto.kweb3.core.contract.*
import dev.klepto.kweb3.core.ethereum.rpc.RpcClientListener
import dev.klepto.kweb3.core.ethereum.type.primitive.EthAddress

/**
 * [Web3Client] implementation that support suspend functions by use of
 * [CoroutineContractParser] and [CoroutineContractExecutor].
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
open class CoroutineWeb3Client(
    endpoint: Web3Endpoint,
    contractProvider: ContractProvider = ContractProxyProvider(),
    contractExecutor: ContractExecutor = CoroutineContractExecutor(),
    contractParser: ContractParser = CoroutineContractParser(),
    listener: RpcClientListener? = null
) :
    Web3Client(
        contractProvider,
        contractExecutor,
        contractParser,
        endpoint,
        listener
    ) {

    companion object {
        /**
         * Inline contract interface getter with reified type. Enables to use
         * generic type slot in-order to define target interface class rather than
         * the java class parameter. For example:
         * ```
         * client.contract<T>(address)
         * ```
         *
         * @param address the ethereum address of the contract
         * @param T the contract interface type
         * @return the contract instance allowing direct interaction with the
         *     blockchain
         * @see Web3Client.contract
         */
        inline fun <reified T : Web3Contract> Web3Client.contract(address: EthAddress): T {
            return contract(T::class.java, address)
        }

        /**
         * Inline contract interface getter with reified type. Enables to use
         * generic type slot in-order to define target interface class rather than
         * the java class parameter. For example:
         * ```
         * client.contract<T>(address)
         * ```
         *
         * @param address the hexadecimal string representing contract address
         * @param T the contract interface type
         * @return the contract instance allowing direct interaction with the
         *     blockchain
         * @see Web3Client.contract
         */
        inline fun <reified T : Web3Contract> Web3Client.contract(address: String): T {
            return contract<T>(EthAddress.address(address))
        }
    }

}
