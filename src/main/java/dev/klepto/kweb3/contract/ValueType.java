package dev.klepto.kweb3.contract;

import com.google.common.reflect.TypeToken;
import dev.klepto.kweb3.abi.type.AbiType;
import dev.klepto.kweb3.abi.type.Tuple;
import lombok.Value;
import lombok.With;

import java.util.List;

import static com.google.common.base.Preconditions.checkState;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Value
@With
public class ValueType {

    TypeToken<?> type;
    List<ValueType> children;
    AbiType abiType;
    boolean indexed;

    public ValueType getComponentType() {
        return withType(ContractCodec.getComponentType(type))
                .withIndexed(false);
    }

    public boolean isArray() {
        return getAbiType().isArray();
    }

    public boolean isTupleType() {
        return getAbiType().getType() == Tuple.class && !getAbiType().isArray();
    }

    public ValueType wrapTuple() {
        return withType(TypeToken.of(Tuple.class))
                .withChildren(List.of(this))
                .withAbiType(abiType.wrapTuple())
                .withIndexed(false);
    }

    public ValueType unwrapTuple() {
        checkState(
                getChildren().size() == 1 && getChildren().get(0).getAbiType().getType() == Tuple.class,
                "Value type is not a tuple wrapper."
        );
        return getChildren().get(0);
    }

}
