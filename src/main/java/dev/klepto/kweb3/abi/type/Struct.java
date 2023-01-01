package dev.klepto.kweb3.abi.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import lombok.val;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
@RequiredArgsConstructor
public class Struct implements AbiValue, List<Object> {

    @Delegate
    private final List<Object> value;

    public Object getFirst() {
        return get(0);
    }

    public Object getLast() {
        return get(size() - 1);
    }

    @Override
    public AbiType getType() {
        val children = stream().map(child -> {
            val array = child.getClass().isArray();
            val element = array ? ((Object[]) child)[0] : child;
            if (element instanceof AbiValue abiValue) {
                return abiValue.getType().withArray(array);
            }
            return new AbiType(element.getClass(), List.of(), array, 0, 0);
        }).toList();
        return new AbiType(getClass()).withChildren(children);
    }

    @Override
    public String toString() {
        val values = stream()
                .map(element -> element.getClass().isArray() ? Arrays.toString((Object[]) element) : element.toString())
                .collect(Collectors.joining(","));
        return "struct(" + values + ")";
    }
}
