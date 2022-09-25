package dev.klepto.kweb3.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Abstract class for all solidity types, excluding some primitives such as <i>string</i> and <i>bool</i>.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
@RequiredArgsConstructor
public abstract class SolidityType {

    private final String encodedName;

}