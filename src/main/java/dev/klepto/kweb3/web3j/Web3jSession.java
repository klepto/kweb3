package dev.klepto.kweb3.web3j;

import lombok.Getter;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;

/**
 * Container for Web3j session.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
public class Web3jSession {

    private final Web3j web3j;
    private final Credentials credentials;
    private final TransactionManager transactionManager;
    private final long chainId;

    public Web3jSession(String rpcUrl, long chainId, String privateKey) {
        this.web3j = Web3j.build(new HttpService(rpcUrl));
        this.credentials = Credentials.create(privateKey);
        this.transactionManager = new RawTransactionManager(web3j, credentials, chainId);
        this.chainId = chainId;
    }

}
