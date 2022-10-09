package dev.klepto.kweb3.type;

import dev.klepto.kweb3.util.reflection.Creatable;
import dev.klepto.kweb3.util.number.Valuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Abstract class for all solidity types, excluding some primitives such as <i>string</i> and <i>bool</i>.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
@RequiredArgsConstructor
public abstract class SolidityType<T extends SolidityType<T, V>, V> implements Valuable<V>, Creatable<T> {

}