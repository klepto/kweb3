package dev.klepto.kweb3.core.abi.descriptor;

import com.google.common.collect.ImmutableList;
import dev.klepto.kweb3.core.type.EthTuple;
import dev.klepto.unreflect.UnreflectType;
import lombok.With;
import lombok.val;

import java.util.stream.Collectors;

/**
 * An ABI type descriptor for {@link EthTuple} ethereum data type.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 * @see dev.klepto.kweb3.core.type.EthTuple
 */
@With
public record EthTupleTypeDescriptor(ImmutableList<TypeDescriptor> children) implements TypeDescriptor {

    public EthTupleTypeDescriptor() {
        this(ImmutableList.of());
    }

    @Override
    public UnreflectType type() {
        return UnreflectType.of(EthTuple.class);
    }

    @Override
    public String toAbiDescriptor() {
        val childrenDescriptor = children.stream()
                .map(TypeDescriptor::toAbiDescriptor)
                .collect(Collectors.joining(","));
        return "(" + childrenDescriptor + ")";
    }

}
