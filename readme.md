# kWeb3

Web3j wrapper that enables simplified web3 contract function calls.
Supports annotation-driven contract interfaces in-order to avoid verbose definitions required by Web3j,
and/or need of having contract's code in-order to generate java wrappers for contracts.

# Sample code

Getting USDT balance on BSC Testnet using kWeb3:

```java
interface Erc20 extends Contract {
    @View
    Uint256 balanceOf(Address account);
}

var client = Web3Client.createClient(new Web3Wallet(privateKey), Chains.BSC_TESTNET);
var usdt = client.getContract(Erc20.class, "0x7ef95a0fee0dd31b22626fa2e10ee6a223f8a684");
var balance = usdt.balanceOf(client.getAddress());  // Uint256
```

Getting USDT balance on BSC Testnet using Web3j:

```java
var web3j = Web3j.build(new HttpService(rpcUrl));
var credentials = Credentials.create(privateKey);
var function = new Function(
        "balanceOf",
        List.of(new Address(credentials.getAddress())),
        List.of(new TypeReference<Uint256>(){})
);
var encodedFunction = FunctionEncoder.encode(function);
var transaction = Transaction.createEthCallTransaction(
        credentials.getAddress(),
        "0x7ef95a0fee0dd31b22626fa2e10ee6a223f8a684",
        encodedFunction
);
var result = web3j.ethCall(transaction,DefaultBlockParameter.valueOf("latest")).send().getResult();
var values = FunctionReturnDecoder.decode(function.getOutputParameters());
var balance = values.get(0); // Uint256
```

# Convertible Types

A lot of solidity types can be converted to Java types and vice-versa.
This can be achieved with `@Type` annotation. Some examples of valid `balanceOf` implementations.

```java
@View
@Type(Uint256.class)
BigInteger balanceOf(Address account);
```

```java
@View
@Type(Uint256.class)
BigDecimal balanceOf(@Type(Address.class) String account);
```

# Named Functions

Contract function names don't have to match your implementation. Match conventions of your codebase with named
functions.

```java
@View("balanceOf")
Uint256 getBalance(Address account);
```

# Transactions

Avoid unnecessary boilerplate when dealing with transactions. Private key is all you need to start transacting with
kWeb3.

```java
interface Erc20 extends Contract {
    @Transaction
    ContractResponse transfer(Address to, Uint256 amount);
}

var usdt = client.getContract(Erc20.class, "0x7ef95a0fee0dd31b22626fa2e10ee6a223f8a684");
usdt.transfer(address, ether(1)); // Sends 1 USDT to given address.
```

# Payable Functions

Payble amount can be defined as function parameter using `@Value` annotation.

```java
@Transaction
ContractResponse deposit(@Value Uint256 amount);
```

# Events

Events are decoded based on their container classess. All fields of Event container class represent an event value,
container class must contain constructor with all it's fields. Event containers must be defined inside contract
interface.

```java
interface Erc20 extends Contract {

    @lombok.Value
    @Event("Transfer")
    class TransferEvent {
        @Indexed Address from;
        @Indexed Address to;
        Uint256 value;
    }

    @Transaction
    ContractResponse transfer(Address to, Uint256 amount);

}

var usdt = client.getContract(Erc20.class, "0x7ef95a0fee0dd31b22626fa2e10ee6a223f8a684");
var response = usdt.transfer(address, ether(1));
var transferEvent = response.getEvent(Erc20.TransferEvent.class);
```

# Gas estimation & ABI encoding

In-case you need to figure out gas usage before you execute your transactions,
you can simply call your transactions as a lambda function:

```java
List<Uint256> gasUsed = client.estimateGas(() -> {
        weth.deposit(ether(1));
        usdt.transfer(address,ether(1));
});
```

Sometimes you need ABI encoding for contracts like Multicall,
in which case you can call your functions in ABI encoding lambda.

```java
List<String> data = client.abiEncode(() -> {
        weth.deposit(ether(1));
        usdt.transfer(address,ether(1));
});
```

# Solidity and Java types

Type representation and convertability.

| Solidity Type     | Java Types                                            |
|-------------------|-------------------------------------------------------|
| Address (uint160) | String, BigInteger, BigDecimal                        |
| uint              | String, BigInteger, BigDecimal                        |
| bytes             | byte[]                                                |
| dynamic array     | `Type[]` or `List<Type>` of any java or solidity type |
| <b>bool*</b>      | boolean                                               |
| <b>string*</b>    | String                                                |
| <b>struct**</b>   | java.lang.Class                                     |
| int               | <i>to be implemented</i>                              |
| fixed             | <i>to be implemented</i>                              |
| array\[size]      | <i>to be implemented</i>                              |
| bytes\<size>      | <i>to be implemented</i>                              |

<b>*</b> doesn't have solidity representation, only represented in Java primitive type

<b>**</b> can and should be represented by data classes
