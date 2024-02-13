package dev.klepto.kweb3.core.contract.type;

/**
 * Marks any JVM class as a struct container. A struct container is a class that <b>only</b> consists of fields
 * dedicated to encoding and/or decoding ethereum contract parameters and return values. Suggested patterns is
 * implementing this interface in a <i>java record</i>, lombok's <i>@Value</i> class or kotlin's <i>data  class</i>.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthStructContainer extends EthTupleContainer {
}
