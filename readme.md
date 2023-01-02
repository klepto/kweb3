# kweb3

Annotation-driven web3 client for JVM with a focus on API simplicity and ease-of-use.

In Solidity, in-order to interact with a smart contract,
all you need to do is define an interface and bind it to an address.
This library enables you to do exactly that but in JVM environment.

We simply define an interface for any smart contract:
```java
public interface Erc20 extends Contract {
    @View
    Uint balanceOf(Address account);
}
```
And now we are ready to interact with it:
```java
var client = Web3Client.create(chain, privateKey);
var usdt = client.contract(Erc20.class, "0x7ef95a0fee0dd31b22626fa2e10ee6a223f8a684"); // USDT on BSC testnet
var balance = usdt.balanceOf(client.getAddress());  // uint256 - USDT balance of our wallet
```

As simple as that.

Comparison with boilerplate that comes with competing libraries, such as Web3j:
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
var balance = values.get(0); // uint256
```

# Types
Currently supported solidity types are: `Address, Bytes, Int, Uint, Struct` and their respective arrays. 
Solidity types of `string` and `bool` are supported via Java primitive types.

Types are accessible through static initializers for simplicity:
```java
address("0x7ef95a0fee0dd31b22626fa2e10ee6a223f8a684"); // dev.klepto.kweb3.Address
uint8(123); // dev.klepto.kweb3.Uint(size=8)
uint256(123); // dev.klepto.kweb3.Uint(size=256)
// and so on
```

Available static initializers are: `address, uint8-256, int8-256, bytes, bytes1-32, struct `

Majority of value types are interchangeable to aid with custom contract calldata encoding. 
All of the following types:
`byte, short, int, long, float, double, BigInteger, BigDecimal, Decimal, Int, Uint, Bytes, byte[], String (hex)`,
can be seamlessly converted to one another either through use of
static initializers or by calling `value#to()` methods:

```java
value = uint256(123);

// is also
value = uint256("0x7b");

// is also
value = uint256(new BigInteger("123"));
```

For contract interface encoding/decoding, value conversion is done automatically.
This can be achieved with `@Type` annotation. Some examples of valid `balanceOf` implementations.

```java
@View
Uint balanceOf(Address account);
```

```java
@View
@Type(Uint.class)
BigInteger balanceOf(Address account);
```

```java
@View
@Type(value = Uint.class, valueSize = 256)
BigDecimal balanceOf(@Type(Address.class) String account);
```

# Named Functions

Contract function names don't have to match your implementation. Match conventions of your codebase with named
functions.

```java
@View("balanceOf")
Uint getBalance(Address account);
```

# Transactions

Avoid unnecessary boilerplate when dealing with transactions. Private key is all you need to start transacting with
kweb3. By default, the average gas price of the latest block is used as a gas price.
```java
public interface Erc20 extends Contract {
    @Transaction
    Web3Response transfer(Address to, Uint amount);
}

var usdt = client.contract(Erc20.class, "0x7ef95a0fee0dd31b22626fa2e10ee6a223f8a684");
usdt.transfer(address, tokens(1, usdt.decimals())); // Sends 1 USDT to given address.
```

# Payable Functions

Payble amount can be defined as function parameter using `@Cost` annotation.

```java
@Transaction
Web3Response deposit(@Cost Uint amount);
```

# Events

Events are decoded based on their container classess. All fields of Event container class represent an event value,
container class must contain constructor with all it's fields. By default kweb3 scans entire classpath to discover event containers.

```java
interface Erc20 extends Contract {

    @Value
    @Event("Transfer")
    class TransferEvent {
        @Event.Address Address eventAddress;
        @Event.Indexed Address from;
        @Event.Indexed Address to;
        Uint value;
    }

    @Transaction
    Web3Response transfer(Address to, Uint amount);

}

var usdt = client.contract(Erc20.class, "0x7ef95a0fee0dd31b22626fa2e10ee6a223f8a684");
var response = usdt.transfer(address, tokens(1, usdt.decimals()));
var transferEvent = response.getEvents()
        .stream(Erc20.TransferEvent.class)
        .first();
```

Events contain utility features to make filtering by value(s) slightly more convenient:

```java
var transferEvent = response.getEvents()
        .stream(Erc20.TransferEvent.class)
        .with(Erc20.TransferEvent::getReceiver, client.getAddress())
        .first();
```

# Utilities

### Numeric values
Because of type interchangeability, numeric solidity types allow for math of any other type.
```java
uint256(10).mul(1.5); // uint256(15)
uint256(10).div("0x3"); // uint256(3)
```
That also means that all types are comparable to each other.
```java
uint256(10).equalTo(BigInteger.TEN); // true
uint256(10).lessThan(address(0)); // false
uint256(3).compareTo(10); // -1
// etc
```
To maintain interchangeability and all utility functions that solidity types support, 
it's generally encouraged to use custom `dev.klepto.kweb3.type.util.Decimal` 
type for arbitrary numbers instead of java's `BigInteger` and `BigDecimal`.

### Contract cache
Some contract calls might return constant values, or may only need to be updated periodically.
For this you can annotate your contract functions with `@Cache` 
annotation to avoid unnecessary requests.

```java
@View
@Type(value = Uint.class, valueSize = 8)
@Cache(INDEFINITE)
Uint decimals(); // Unlikely to ever change, cache indefinitely.

@View
@Cache(value = 24, timeUnit = TimeUnit.HOURS)
Uint totalSupply(); // Rarely changes, maybe we care about updating this only once every 24 hours.
```

### Logging requests

To generate web3 requests without executing them, you can use `client#getLogs(runnable)`.
This can be useful if you want to generate ABI from contract functions, or delay/re-use requests in the future.

```java
var usdt = client.contract(Erc20.class, "0x7ef95a0fee0dd31b22626fa2e10ee6a223f8a684");
var usdc = client.contract(Erc20.class, "0x64544969ed7EBf5f083679233325356EbE738930");

var requests = client.getLogs(() -> {
    usdt.balanceOf(client.getAddress());
    usdc.balanceOf(client.getAddress());
}); // java.util.List<Web3Request>

var usdtCalldata = client.abiEncode(requests.get(0)); // java.lang.String
```

### Multicall

Multicall is a widely used smart contract that batches multiple web3 calls into single request,
this allows you to minimize RPC requests and process large amounts of data very quickly. For this reason,
kweb3 provides multicall builder utility that allows you to very easily batch and deserialize multiple requests.


Since there are a lot of different implementations of this contract and they tend to be different on every chain,
you have to implement this generic interface in-order to use the builder:
```java
public interface MulticallContract extends Contract {
    Bytes[] execute(Uint gasLimit, Uint sizeLimit, List<Address> addresses, List<Bytes> data);
}
```
After implementing this interface with the multicall contract of your choice,
we can simply tell kweb3 client to use it:
```java
val multicallContract = client.contract(UniswapMulticall.class, "0x4445286592CaADEB59917f16826B964C3e7B2D36"); // Univ2 Multicall on Mumbai
client.setMulticallContract(multicallContract);
```
Which allows us to access multicall builder with this client instance:
```java
// Fetch token information in a single request!
client.multicall()
        .queue(usdt::symbol, symbol -> ...) // String
        .queue(usdt::decimals, decimals -> ... ) // uint8
        .queue(usdt::totalSupply, totalSupply -> ... ) // uint256
        .queue(() -> usdt.balanceOf(client.getAddress()), ourBalance -> ...) // uint256
        .execute();
```

# Contribute

This is a passion project solely developed by me on my own free time.
Feel free to contribute by opening issues or creating pull reqeusts.