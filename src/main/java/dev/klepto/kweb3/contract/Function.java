package dev.klepto.kweb3.contract;

import lombok.Value;

import java.util.List;

/**
 * Contains information about contract function.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Value
public class Function {

    String name;
    boolean view;
    List<Object> parameterTypes;
    List<Object> returnTypes;

}
