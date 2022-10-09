package dev.klepto.kweb3.util;

import lombok.val;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Collection utility methods.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Collections {

    public static <T> T[] array(T... elements) {
        return elements;
    }

    public static <T> List<T> list(T... elements) {
        return Arrays.stream(elements).toList();
    }

    public static <T> T[] cast(Object[] array, Class<T> componentType) {
        val newArray = Array.newInstance(componentType, array.length);
        System.arraycopy(array, 0, newArray, 0, array.length);
        return (T[]) newArray;
    }

    public static <T> T[] remove(T[] array, int index) {
        if (index < 0 || index >= array.length) {
            return array;
        }

        val componentType = array.getClass().getComponentType();
        val newArray = Array.newInstance(componentType, array.length - 1);
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(array, index + 1, newArray, index, array.length - index - 1);
        return (T[]) newArray;
    }

    public static IntStream until(int limit) {
        return until(0, limit);
    }

    public static IntStream until(int start, int limit) {
        return IntStream.range(start, limit);
    }

}
