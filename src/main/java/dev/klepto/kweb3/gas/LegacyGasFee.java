package dev.klepto.kweb3.gas;

import dev.klepto.kweb3.type.Uint256;
import lombok.Value;

/**
 * Gas fee representation for legacy transactions.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Value
public class LegacyGasFee {

    Uint256 gasPrice;

}
