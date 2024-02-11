package dev.klepto.kweb3.core.util;

import com.google.common.collect.ObjectArrays;
import lombok.val;
import org.jetbrains.annotations.NotNull;

/**
 * Various collection utilities.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public final class Collections {

    private Collections() {
    }

    /**
     * Unsafely casts given {@link Object} array to specified component type.
     *
     * @param array         the object array
     * @param componentType the  desired component type
     * @return the array of given component type
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public static <T> T[] arrayCast(@NotNull Object[] array, @NotNull Class<T> componentType) {
        val newArray = ObjectArrays.newArray(componentType, array.length);
        System.arraycopy(array, 0, newArray, 0, array.length);
        return newArray;
    }

    /**
     * Returns a copy of a given array without element at specified index. Successful use of this method always shrinks
     * array length by 1.
     *
     * @param array the object array
     * @param index the index to be removed, any non-positive value will return original array
     * @return a new array not containing the element at specified index
     */
    @NotNull
    public static <T> T[] arrayRemove(@NotNull T[] array, int index) {
        if (index < 0) {
            return array;
        }

        val newArray = ObjectArrays.newArray(array, array.length - 1);
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(array, index + 1, newArray, index, newArray.length - index);
        return newArray;
    }

}
