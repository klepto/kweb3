package dev.klepto.kweb3.util.multicall;

import dev.klepto.kweb3.abi.type.Address;
import dev.klepto.kweb3.abi.type.Bytes;
import dev.klepto.kweb3.abi.type.Uint;
import dev.klepto.kweb3.contract.Contract;

import java.util.List;


/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface MulticallContract extends Contract {

    Bytes[] execute(Uint gasLimit, Uint sizeLimit, List<Address> addresses, List<Bytes> data);

}