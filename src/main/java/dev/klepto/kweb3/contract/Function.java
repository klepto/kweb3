package dev.klepto.kweb3.contract;

import lombok.Value;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Value
public class Function {

    String name;
    String hash;
    boolean view;
    boolean struct;
    ValueType parametersType;
    ValueType returnType;

}
