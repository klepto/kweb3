package dev.klepto.kweb3.net;

import com.google.common.util.concurrent.Striped;
import dev.klepto.kweb3.Web3Request;
import dev.klepto.kweb3.Web3Response;
import dev.klepto.kweb3.abi.type.Address;
import dev.klepto.kweb3.chain.Chain;
import dev.klepto.kweb3.contract.Contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;

/**
 * A thread-safe implementation of {@link ContractClient}.
 *
 * @author Ryley Kimmel <ryley@hlwgroup.dev>
 */
public abstract class SafeContractClient extends ContractClient {

    private static final int DEFAULT_CONCURRENCY_LEVEL = 16;

    private final Striped<Lock> striped;

    public SafeContractClient(Chain chain, String privateKey, int concurrencyLevel) {
        super(chain, privateKey, new ArrayList<>(), new HashMap<>());
        this.striped = Striped.lock(concurrencyLevel);
    }

    public SafeContractClient(Chain chain, String privateKey) {
        this(chain, privateKey, DEFAULT_CONCURRENCY_LEVEL);
    }

    @Override
    public <T extends Contract> T contract(Class<T> type, Address address) {
        var lock = striped.get(address);
        lock.lock();
        try {
            return super.contract(type, address);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Web3Response send(Web3Request request) {
        var lock = striped.get(request);
        lock.lock();
        try {
            return super.send(request);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<Web3Request> getLogs(Runnable runnable) {
        var lock = striped.get(runnable);
        lock.lock();
        try {
            return super.getLogs(runnable);
        } finally {
            lock.unlock();
        }
    }
}
