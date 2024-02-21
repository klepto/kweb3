package dev.klepto.kweb3.core.contract;

import dev.klepto.kweb3.core.Web3Client;
import dev.klepto.kweb3.core.type.EthAddress;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Intercepts calls on un-implemented methods in the contract interface. Encodes and executes appropriate blockchain
 * transactions and returns the result.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
@RequiredArgsConstructor
public class ContractProxy implements InvocationHandler {

    private final ContractProxies contracts;
    private final Class<? extends Web3Contract> type;
    private final Web3Client client;
    private final EthAddress address;
    private final Map<Object, Object> attributes = new ConcurrentHashMap<>();

    /**
     * Intercepts contract interface method call. Called automatically by JVM {@link java.lang.reflect.Proxy} via
     * {@link InvocationHandler} interface.
     *
     * @param proxy  the proxy instance that the method was invoked on
     * @param method the {@code Method} instance corresponding to the interface method invoked on the proxy instance.
     *               The declaring class of the {@code Method} object will be the interface that the method was declared
     *               in, which may be a superinterface of the proxy interface that the proxy class inherits the method
     *               through.
     * @param args   an array of objects containing the values of the arguments passed in the method invocation on the
     *               proxy instance, or {@code null} if interface method takes no arguments. Arguments of primitive
     *               types are wrapped in instances of the appropriate primitive wrapper class, such as
     *               {@code java.lang.Integer} or {@code java.lang.Boolean}.
     * @return the result of this method call
     * @throws Throwable a runtime error occurred
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        args = args == null ? new Object[0] : args;

        // Invoke interface default methods first. Allows for custom logic/helper methods in contract implementations.
        if (method.isDefault()) {
            return InvocationHandler.invokeDefault(proxy, method, args);
        }

        switch (method.getName()) {
            case "getContractClass":
                return type;
            case "toString":
                return type.getSimpleName();
            case "hashCode":
                return hashCode();
            case "equals":
                return equals(args[0]);
            case "getAddress":
                return address;
            case "getClient":
                return client;
            case "getProxy":
                return this;
            default:
                val function = contracts.getParser().parseFunction(method);
                val call = new ContractCall(this, function, args);
                return contracts.getExecutor().execute(call);
        }
    }

    /**
     * Generates hash code for this proxy contract.
     *
     * @return the contract hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(client.getNetwork(), type, address);
    }

    /**
     * Returns true if an object is equal to this contract. Contracts are equal if they are on the same network, have
     * same type and have same address.
     *
     * @param object the object to check
     * @return true if given object is equal to this contract
     */
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        if (!(object instanceof Web3Contract contract)) {
            return false;
        }

        return Objects.equals(type, contract.getContractClass())
                && Objects.equals(address, contract.getAddress())
                && Objects.equals(client.getNetwork(), contract.getClient().getNetwork());
    }

}
