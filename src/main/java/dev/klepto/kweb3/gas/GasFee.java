package dev.klepto.kweb3.gas;

import dev.klepto.kweb3.type.sized.Uint256;
import lombok.Value;

/**
 * Gas fee representation for EIP-1559 transactions.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Value
public class GasFee {

    Uint256 maxFeePerGas;
    Uint256 maxPriorityFeePerGas;

}
