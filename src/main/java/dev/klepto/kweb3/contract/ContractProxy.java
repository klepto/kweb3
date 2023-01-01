package dev.klepto.kweb3.contract;

import com.google.common.reflect.TypeToken;
import dev.klepto.kweb3.Web3Client;
import dev.klepto.kweb3.Web3Request;
import dev.klepto.kweb3.Web3Response;
import dev.klepto.kweb3.abi.type.Address;
import dev.klepto.kweb3.abi.type.Struct;
import dev.klepto.kweb3.abi.type.Uint;
import dev.klepto.kweb3.contract.event.EventDecoder;
import dev.klepto.kweb3.util.Keccak;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;

import static dev.klepto.kweb3.Web3Error.require;
import static dev.klepto.kweb3.abi.type.util.Types.struct;
import static dev.klepto.kweb3.abi.type.util.Types.uint256;
import static dev.klepto.kweb3.util.Logging.log;

/**
 * Generates web3 request from a contract Java interface call. Encodes request into solidity types, which later gets passed
 * on to web3 client implementation, results are returned in solidity types which later get decoded into desired return
 * types based on the contract interface implementation and it's annotations.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@RequiredArgsConstructor
public class ContractProxy implements InvocationHandler {

    private final Web3Client client;
    private final Address address;

    private final ContractCache responseCache = new ContractCache();
    private final Map<Method, Function> functionCache = new HashMap<>();

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isDefault()) {
            return InvocationHandler.invokeDefault(proxy, method, args);
        }

        switch (method.getName()) {
            case "equals":
                return address.equals(args[0]);
            case "toString":
                val className = proxy.getClass().getInterfaces()[0].getSimpleName();
                return className + "(" + address.toHex() + ")";
            case "hashCode":
                return address.hashCode();
            case "getAddress":
                return address;
            case "getClient":
                return client;
            default:
                return contractCall(method, args);
        }
    }

    private Object contractCall(Method method, Object[] args) {
        val request = parseRequest(method, args);

        if (method.isAnnotationPresent(Cache.class) && !client.isLogging()) {
            val annotation = method.getAnnotation(Cache.class);
            val duration = annotation.timeUnit().toMillis(annotation.value());
            val key = client.abiEncode(request);
            return responseCache.get(key, () -> {
                val result = getResponse(request);
                return new ContractCache.CacheItem(result, System.currentTimeMillis(), duration);
            });
        }

        return getResponse(request);
    }

    private Web3Request parseRequest(Method method, Object[] args) {
        val function = parseFunction(method);
        var value = uint256(0);

        var offset = 0;
        val values = new ArrayList<>();
        val parameters = method.getParameters();
        for (var i = 0; i < parameters.length; i++) {
            val parameter = parameters[i];
            val parameterValue = args[i];

            if (parameter.isAnnotationPresent(Cost.class)) {
                val valueType = ContractCodec.parseValueType(
                        TypeToken.of(parameter.getParameterizedType()),
                        TypeToken.of(Uint.class),
                        Uint.MAX_SIZE,
                        0,
                        false
                );
                value = (Uint) ContractCodec.encodeValue(parameterValue, valueType);
                offset++;
            } else {
                val valueType = function.getParametersType().getChildren().get(i - offset);
                values.add(ContractCodec.encodeValue(parameterValue, valueType));
            }
        }

        return new Web3Request(address, client.getAddress(), function, value, struct(values), List.of());
    }

    private Object getResponse(Web3Request request) {
        var response = client.send(request);
        if (response == null) {
            return null;
        }

        if (response.getError() != null) {
            throw response.getError();
        }

        return ContractCodec.decodeResponse(response);
    }

    private Function parseFunction(Method method) {
        if (functionCache.containsKey(method)) {
            return functionCache.get(method);
        }

        val view = method.isAnnotationPresent(View.class);
        val transaction = method.isAnnotationPresent(Transaction.class);
        require(
                view || transaction,
                "No @View or @Transaction annotation present on {}#{} contract function.",
                method.getDeclaringClass().getSimpleName(), method.getName()
        );

        var name = view
                ? method.getAnnotation(View.class).value()
                : method.getAnnotation(Transaction.class).value();
        if (name.isBlank()) {
            name = method.getName();
        }

        val returnTypeToken = TypeToken.of(method.getGenericReturnType());
        val parameters = Arrays.stream(method.getParameters())
                .filter(parameter -> !parameter.isAnnotationPresent(Cost.class))
                .toList();

        val parameterType = ContractCodec.parseParametersType(parameters);
        val signature = name + parameterType.getAbiType().toString();
        log().debug("Function signature: {}", signature);

        val returnType = ContractCodec.parseValueType(
                returnTypeToken,
                method.getAnnotation(Type.class),
                method.isAnnotationPresent(Event.Indexed.class)
        );

        val hash = "0x" + Keccak.hash(signature).substring(0, 8).toLowerCase();
        val function = new Function(name, hash, view, parameterType, returnType);
        functionCache.put(method, function);
        return function;
    }


}
