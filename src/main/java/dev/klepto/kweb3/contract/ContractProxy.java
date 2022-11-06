package dev.klepto.kweb3.contract;

import com.google.common.reflect.TypeToken;
import dev.klepto.kweb3.Web3Client;
import dev.klepto.kweb3.Web3Request;
import dev.klepto.kweb3.Web3Response;
import dev.klepto.kweb3.contract.codec.ContractCodec;
import dev.klepto.kweb3.contract.codec.ContractDecoder;
import dev.klepto.kweb3.contract.codec.ContractEncoder;
import dev.klepto.kweb3.contract.codec.ContractEvents;
import dev.klepto.kweb3.type.*;
import dev.klepto.kweb3.type.sized.Uint256;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;

import java.lang.reflect.*;
import java.util.*;

import static dev.klepto.kweb3.Web3Error.require;

/**
 * Generates web3 request from a contract Java interface call. Encodes request into solidity types, which later get passed
 * on to web3 client implementation, results are returned in solidity types which later get decoded into desired return
 * types based on the contract interface implementation and it's annotations.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@RequiredArgsConstructor
public class ContractProxy implements InvocationHandler {

    private final Web3Client client;
    private final Address address;
    private final ContractCache cache = new ContractCache();

    @Override
    @SneakyThrows
    public Object invoke(Object proxy, Method method, Object[] args) {
        if (method.isDefault()) {
            return InvocationHandler.invokeDefault(proxy, method, args);
        }

        switch (method.getName()) {
            case "equals":
                return address.equals(args[0]);
            case "toString":
                return address.toString();
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
            return cache.get(key, () -> {
                val result = getResponse(method, request);
                return new ContractCache.CacheItem(result, System.currentTimeMillis(), duration);
            });

        }

        return getResponse(method, request);
    }

    private Web3Request parseRequest(Method method, Object[] args) {
        val function = parseFunction(method);
        var value = Uint256.ZERO;

        val parameters = new ArrayList<>();
        val methodParameters = method.getParameters();
        for (var i = 0; i < methodParameters.length; i++) {
            val parameter = methodParameters[i];
            val parameterValue = args[i];
            val parameterType = TypeToken.of(parameter.getParameterizedType());
            val parameterAnnotation = parameter.getAnnotation(Type.class);

            if (parameter.isAnnotationPresent(Value.class)) {
                value = (Uint256) ContractEncoder.encodeParameterValue(
                        parameterValue,
                        parameterType,
                        ContractCodec.UINT256_TYPE
                );
            } else {
                val encodedParameter = ContractEncoder.encodeParameterValue(
                        parameterValue,
                        parameterType,
                        parameterAnnotation
                );
                parameters.add(encodedParameter);
            }
        }
        return new Web3Request(address, client.getAddress(), function, parameters, value, ContractEvents.EVENTS);
    }

    private Object getResponse(Method method, Web3Request request) {
        val response = client.send(request);

        if (response != null && response.getError() != null) {
            throw response.getError();
        }

        if (response == null || response.getResult() == null) {
            return null;
        }

        if (method.getReturnType() == Web3Response.class) {
            return response;
        }

        if (method.getReturnType() == ContractResponse.class) {
            return createContractResponse(response, method.getDeclaringClass());
        }

        val returnType = TypeToken.of(method.getGenericReturnType());
        return ContractDecoder.decodeReturnValues(response.getResult(), returnType);
    }

    private Function parseFunction(Method method) {
        val view = method.isAnnotationPresent(View.class);
        val transaction = method.isAnnotationPresent(Transaction.class);
        require(
                view || transaction,
                "No @View or @Transaction annotation present on {}#{} contract function.",
                method.getDeclaringClass().getSimpleName(), method.getName()
        );

        var name = view ? method.getAnnotation(View.class).value() : method.getAnnotation(Transaction.class).value();
        if (name.isBlank()) {
            name = method.getName();
        }

        val parameterTypes = new ArrayList<>();
        for (val parameter : method.getParameters()) {
            if (parameter.isAnnotationPresent(Value.class)) {
                continue;
            }

            val parameterType = TypeToken.of(parameter.getParameterizedType());
            val parameterAnnotation = parameter.getAnnotation(Type.class);
            parameterTypes.add(ContractEncoder.encodeParameterType(parameterType, parameterAnnotation));
        }

        val returnType = TypeToken.of(method.getGenericReturnType());
        val returnAnnotation = method.getAnnotation(Type.class);
        val returnTypes = ContractEncoder.encodeReturnTypes(returnType, returnAnnotation);
        return new Function(name, view, parameterTypes, returnTypes);
    }

    @SneakyThrows
    private ContractResponse createContractResponse(Web3Response web3Response, Class<?> contractClass) {
        val request = web3Response.getRequest();
        val transactionHash = web3Response.getTransactionHash();
        val error = web3Response.getError();
        val result = web3Response.getResult();
        val events = ContractEvents.decodeEvents(web3Response);
        val eventTypes = events.stream().<Class<?>>map(Object::getClass).distinct().toList();
        return new ContractResponse(contractClass, request, transactionHash, error, result, events, eventTypes);
    }

}
