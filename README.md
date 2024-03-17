<div align="center">

![kweb3](logo.png)

Ethereum interface for Kotlin and Java

![Maven Central](https://img.shields.io/maven-central/v/dev.klepto.unreflect/unreflect.svg?color=7f52ff) ![javadoc](https://javadoc.io/badge2/dev.klepto.unreflect/unreflect/javadoc.svg?color=7f52ff)
![License](https://img.shields.io/github/license/klepto/kweb3?color=d83f84)
</div>

## Getting Started

In this example, we are obtaining WETH balance of a wallet address:

```java
var client = new Web3Client(PublicNode.ETHEREUM);
var contract = client.contract(Erc20.class, "0xC02aaA39b223FE8D0A0e5C4F27eAD9083C756Cc2");
var result = contract.balanceOf(walletAddress).get();
System.out.println(result);
```

## 

Or in **Kotlin** using `kweb3-kotlin` dependency:

```kotlin
val client = CoroutineWeb3Client(PublicNode.ETHEREUM)
val contract = client.contract<Erc20>("0xC02aaA39b223FE8D0A0e5C4F27eAD9083C756Cc2")
runBlocking {
    val result = contract.balanceOf(walletAddress)
    println(result)
}
```
<br>

## Smart Contracts

Unlike competing libraries, `kweb3` does not generate code from ABI json.<br>Instead, in-order to interact with smart contracts,simply define their function signatures:

**Java**
```java
public interface Erc20 extends Web3Contract {
    @View
    Web3Result<EthUint> balanceOf(EthAddress account);
}
```

## 

**Kotlin (for coroutine support)**
```kotlin
interface Erc20 : Web3Contract {
    @View
    suspend fun balanceOf(account: EthAddress): EthUint
}
```
<br>

## Note

This README serves as a placeholder and does not contain working hyperlinks yet.
