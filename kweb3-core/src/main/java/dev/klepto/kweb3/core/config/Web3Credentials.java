package dev.klepto.kweb3.core.config;

import org.jetbrains.annotations.NotNull;

/**
 * Represents web3 credentials used to sign blockchain transactions.
 *
 * @param privateKey the private key represented as a 64 hexadecimal character string
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public record Web3Credentials(@NotNull String privateKey) {
}
