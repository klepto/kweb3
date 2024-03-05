package dev.klepto.kweb3.core.contract.type;

import dev.klepto.kweb3.core.type.EthValue;

/**
 * Marks any JVM class as a tuple container. A tuple container is a class that <b>only</b> consists of fields dedicated
 * to decoding ethereum contract return values. Suggested patterns is implementing this interface in a <i>java
 * record</i>, lombok's <i>@Value</i> class or kotlin's <i>data  class</i>.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthTupleContainer extends EthValue {
}
