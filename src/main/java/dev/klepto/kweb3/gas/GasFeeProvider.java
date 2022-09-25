package dev.klepto.kweb3.gas;

import dev.klepto.kweb3.Web3Client;

/**
 * Provides gas fee price for {@link Web3Client}.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface GasFeeProvider {

    GasFee getGasFee();

    LegacyGasFee getLegacyGasFee();

}
