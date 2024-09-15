package dev.klepto.kweb3.kotlin

import dev.klepto.kweb3.core.contract.Web3Contract
import dev.klepto.kweb3.core.ethereum.type.primitive.EthUint

/**
 * @author Augustinas R. <http://github.com/klepto>
 */
interface DiamondSwapFactory : Web3Contract {

    suspend fun getTotalPairs(): EthUint

}