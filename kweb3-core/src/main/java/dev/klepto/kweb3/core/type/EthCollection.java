package dev.klepto.kweb3.core.type;

import dev.klepto.kweb3.core.Web3Error;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Exposes delegate methods of {@link List} for ethereum types that store their values in a list, such as
 * {@link EthArray} and {@link EthTuple}.
 *
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface EthCollection<T extends EthType> extends List<T> {

    @NotNull
    List<T> values();

    @Override
    default int size() {
        return values().size();
    }

    @Override
    default boolean isEmpty() {
        return values().isEmpty();
    }

    @Override
    default boolean contains(Object object) {
        return values().contains(object);
    }

    @NotNull
    @Override
    default Iterator<T> iterator() {
        return values().iterator();
    }

    @Override
    default Object @NotNull [] toArray() {
        return values().toArray();
    }

    @Override
    default <T1> T1 @NotNull [] toArray(T1 @NotNull [] array) {
        return values().toArray(array);
    }

    @Override
    default boolean add(T value) {
        throw new Web3Error("Unsupported operation.");
    }

    @Override
    default boolean remove(Object o) {
        throw new Web3Error("Unsupported operation.");
    }

    @Override
    default boolean containsAll(@NotNull Collection<?> collection) {
        return new HashSet<>(values()).containsAll(collection);
    }

    @Override
    default boolean addAll(@NotNull Collection<? extends T> c) {
        throw new Web3Error("Unsupported operation.");
    }

    @Override
    default boolean addAll(int index, @NotNull Collection<? extends T> c) {
        throw new Web3Error("Unsupported operation.");
    }

    @Override
    default boolean removeAll(@NotNull Collection<?> c) {
        throw new Web3Error("Unsupported operation.");
    }

    @Override
    default boolean retainAll(@NotNull Collection<?> c) {
        throw new Web3Error("Unsupported operation.");
    }

    @Override
    default void clear() {
        throw new Web3Error("Unsupported operation.");
    }

    @Override
    default T get(int index) {
        return values().get(index);
    }

    @Override
    default T set(int index, T element) {
        throw new Web3Error("Unsupported operation.");
    }

    @Override
    default void add(int index, T element) {
        throw new Web3Error("Unsupported operation.");
    }

    @Override
    default T remove(int index) {
        throw new Web3Error("Unsupported operation.");
    }

    @Override
    default int indexOf(Object object) {
        return values().indexOf(object);
    }

    @Override
    default int lastIndexOf(Object object) {
        return values().lastIndexOf(object);
    }

    @NotNull
    @Override
    default ListIterator<T> listIterator() {
        return values().listIterator();
    }

    @NotNull
    @Override
    default ListIterator<T> listIterator(int index) {
        return values().listIterator(index);
    }

    @NotNull
    @Override
    default List<T> subList(int fromIndex, int toIndex) {
        return values().subList(fromIndex, toIndex);
    }

}
