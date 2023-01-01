package dev.klepto.kweb3.contract;

import com.google.common.reflect.TypeToken;
import dev.klepto.kweb3.abi.type.AbiType;
import dev.klepto.kweb3.abi.type.Struct;
import lombok.Value;
import lombok.With;

import java.util.List;

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

    public boolean isStructType() {
        return getAbiType().getType() == Struct.class && !getAbiType().isArray();
    }

    public ValueType toStructType() {
        if (isStructType()) {
            return this;
        }

        return withType(TypeToken.of(Struct.class))
                .withChildren(List.of(this))
                .withAbiType(abiType.toStructType())
                .withIndexed(false);
    }

}
