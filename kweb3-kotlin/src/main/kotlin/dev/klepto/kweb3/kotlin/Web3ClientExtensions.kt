package dev.klepto.kweb3.kotlin

import dev.klepto.kweb3.core.Web3Client
import dev.klepto.kweb3.core.contract.Web3Contract
import dev.klepto.kweb3.core.ethereum.type.primitive.EthAddress
import dev.klepto.kweb3.core.ethereum.type.primitive.EthAddress.address

/**
 * Extension functions for [Web3Client].
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
object Web3ClientExtensions {

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
        return contract<T>(address(address))
    }

}