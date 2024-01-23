package dev.klepto.kweb3.util;

import lombok.val;

import java.lang.reflect.Array;

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
    public static <T> T[] arrayCast(Object[] array, Class<T> componentType) {
        val newArray = Array.newInstance(componentType, array.length);
        System.arraycopy(array, 0, newArray, 0, array.length);
        return (T[]) newArray;
    }

}
