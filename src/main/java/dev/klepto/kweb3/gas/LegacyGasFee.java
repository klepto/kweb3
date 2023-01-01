package dev.klepto.kweb3.gas;

import dev.klepto.kweb3.abi.type.Uint;
import lombok.Value;

/**
 * Gas fee representation for legacy transactions.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Value
public class LegacyGasFee {

    Uint gasPrice;

}
