// Copied from FastUtil
// Drastically cut-down by ~5k lines

/*
 * Copyright (C) 2002-2017 Sebastiano Vigna
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gmail.woodyc40.calamity.util;

import java.util.*;
import java.util.function.Consumer;

import static com.gmail.woodyc40.calamity.util.HashCommon.arraySize;
import static com.gmail.woodyc40.calamity.util.HashCommon.maxFill;

/**
 * A function mapping keys into values.
 * <p>Instances of this class represent functions: the main difference with {@link Map}
 * is that functions do not in principle allow enumeration of their domain or range. The need for
 * this interface lies in the existence of several highly optimized implementations of
 * functions (e.g., minimal perfect hashes) which do not actually store their domain or range explicitly.
 * In case the domain is known, {@link #containsKey(Object)} can be used to perform membership queries.
 * <p>The choice of naming all methods exactly as in {@link Map} makes it possible
 * that the number of keys is not available (e.g., in the case of a string hash function).
 * can also set its default return value.
 * <h2>Relationship with {@link java.util.function.Function}</h2>
 * <p>This interface predates Java 8's {@link java.util.function.Function} and it was conceived with
 * a different purpose. To ease interoperability, we extend {@link java.util.function.Function} and
 * implement a default method for {@link #apply(Object)} that delegates to {@link #get(Object)}. However,
 * while the argument of a {@link java.util.function.Function} with keys of type {@code T} is of type
 * {@code T}, the argument of {@link #get(Object)} is unparameterized (see the example below).
 * <p>No attempt will be made at creating type-specific versions of {@link java.util.function.Function} as
 * <h2>Default methods and lambda expressions</h2>
 * <p>All optional operations have default methods throwing an {@link UnsupportedOperationException}, except
 * for {@link #containsKey(Object)}, which returns true, and {@link #size()}, which return -1.
 * Thus, it is possible to define an instance of this class using a lambda expression that will specify
 * {@link #get(Object)}. Note that the type signature of {@link #get(Object)} might lead to slightly
 * counter-intuitive behaviour. For example, to define the identity function on {@link Integer} objects
 * you need to write
 * <pre>
 *     it.unimi.dsi.fastutil.Function&lt;Integer, Integer&gt; f = (x) -&gt; (Integer)x;
 * </pre>
 * as the argument to {@link #get(Object)} is unparameterized.
 * <p><strong>Warning</strong>: Equality of functions is <em>not specified</em>
 * by contract, and it will usually be <em>by reference</em>, as there is no way to enumerate the keys
 * and establish whether two functions represent the same mathematical entity.
 *
 * @see Map
 * @see java.util.function.Function
 */
@FunctionalInterface
interface Function<K, V> extends java.util.function.Function<K, V> {
    /**
     * {@inheritDoc} This is equivalent to calling {@link #get(Object)}.
     *
     * @param key {@inheritDoc}
     * @return {@inheritDoc}
     * @see java.util.function.Function#apply(Object)
     * @see #get(Object)
     * @since 8.0.0
     */

    @Override
    default V apply(final K key) {
        return get(key);
    }

    /**
     * Returns the value associated by this function to the specified key.
     *
     * @param key the key.
     * @return the corresponding value, or {@code null} if no value was present for the given key.
     * @see Map#get(Object)
     */

    V get(Object key);

    /**
     * Returns true if this function contains a mapping for the specified key.
     * <p>Note that for some kind of functions (e.g., hashes) this method
     * will always return true. This default implementation, in particular,
     * always return true.
     *
     * @param key the key.
     * @return true if this function associates a value to {@code key}.
     * @see Map#containsKey(Object)
     */

    default boolean containsKey(Object key) {
        return true;
    }

    /**
     * Removes this key and the associated value from this function if it is present (optional operation).
     *
     * @param key the key.
     * @return the old value, or {@code null} if no value was present for the given key.
     * @see Map#remove(Object)
     */

    default V remove(final Object key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the intended number of keys in this function, or -1 if no such number exists.
     * <p>Most function implementations will have some knowledge of the intended number of keys
     * in their domain. In some cases, however, this might not be possible. This default
     * implementation, in particular, returns -1.
     *
     * @return the intended number of keys in this function, or -1 if that number is not available.
     */

    default int size() {
        return -1;
    }

    /**
     * Removes all associations from this function (optional operation).
     *
     * @see Map#clear()
     */

    default void clear() {
        throw new UnsupportedOperationException();
    }
}

/**
 * A type-specific {@link Function}; provides some additional methods that use polymorphism to avoid (un)boxing.
 * <p>Type-specific versions of {@code get()}, {@code put()} and
 * {@code remove()} cannot rely on {@code null} to denote absence of
 * a key. Rather, they return a {@linkplain #defaultReturnValue() default
 * return value}, which is set to 0/false at creation, but can be changed using
 * the {@code defaultReturnValue()} method.
 * <p>For uniformity reasons, even functions returning objects implement the default
 * return value (of course, in this case the default return value is
 * initialized to {@code null}).
 * <p>The default implementation of optional operations just throw an {@link
 * UnsupportedOperationException}, except for the type-specific {@code
 * containsKey()}, which return true. Generic versions of accessors delegate to
 * the corresponding type-specific counterparts following the interface rules.
 * <p><strong>Warning:</strong> to fall in line as much as possible with the
 * {@linkplain Map standard map interface}, it is required that
 * standard versions of {@code get()}, {@code put()} and
 * {@code remove()} for maps with primitive-type keys or values <em>return
 * {@code null} to denote missing keys </em> rather than wrap the default
 * return value in an object. In case both keys and values are reference
 * types, the default return value must be returned instead, thus violating
 * the {@linkplain Map standard map interface} when the default
 * return value is not {@code null}.
 *
 * @see Function
 */
@FunctionalInterface
interface Object2IntFunction<K> extends Function<K, Integer>, java.util.function.ToIntFunction<K> {
    /**
     * {@inheritDoc}
     *
     * @since 8.0.0
     */
    @Override
    default int applyAsInt(K operand) {
        return getInt(operand);
    }

    /**
     * Adds a pair to the map (optional operation).
     *
     * @param key the key.
     * @param value the value.
     * @return the old value, or the {@linkplain #defaultReturnValue() default return value} if no value was present for the given key.
     */
    default int put(final K key, final int value) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the value to which the given key is mapped.
     *
     * @param key the key.
     * @return the corresponding value, or the {@linkplain #defaultReturnValue() default return value} if no value was present for the given key.
     * @see Function#get(Object)
     */
    int getInt(Object key);

    /**
     * Removes the mapping with the given key (optional operation).
     *
     * @param key the key.
     * @return the old value, or the {@linkplain #defaultReturnValue() default return value} if no value was present for the given key.
     * @see Function#remove(Object)
     */
    default int removeInt(final Object key) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    default Integer put(final K key, final Integer value) {
        final K k = (key);
        final boolean containsKey = containsKey(k);
        final int v = put(k, (value).intValue());
        return containsKey ? Integer.valueOf(v) : null;
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    @SuppressWarnings("FunctionalInterfaceMethodChanged")
    default Integer get(final Object key) {
        final Object k = (key);
        final int v = getInt(k);
        return (v != defaultReturnValue() || containsKey(k)) ? Integer.valueOf(v) : null;
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    default Integer remove(final Object key) {
        final Object k = (key);
        return containsKey(k) ? Integer.valueOf(removeInt(k)) : null;
    }

    /**
     * Sets the default return value (optional operation).
     * This value must be returned by type-specific versions of
     * {@code get()}, {@code put()} and {@code remove()} to
     * denote that the map does not contain the specified key. It must be
     * 0/{@code false}/{@code null} by default.
     *
     * @param rv the new default return value.
     * @see #defaultReturnValue()
     */
    default void defaultReturnValue(int rv) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the default return value.
     * <p>This default implementation just return the default null value
     * of the type ({@code null} for objects, 0 for scalars, false for Booleans).
     *
     * @return the current default return value.
     */
    default int defaultReturnValue() {
        return (0);
    }
}

/**
 * A type-specific {@link Map}; provides some additional methods that use polymorphism to avoid (un)boxing, and handling of a default return value.
 * {@link #keySet()} and {@link #values()}. Moreover, a number of methods, such as {@link #size()}, {@link #defaultReturnValue()}, etc., are un-defaulted
 * as their function default do not make sense for a map.
 * Maps returning entry sets of type {@link FastEntrySet} support also fast iteration.
 * <p>A submap or subset may or may not have an
 * independent default return value (which however must be initialized to the
 * default return value of the originator).
 *
 * @see Map
 */
interface Object2IntMap<K> extends Object2IntFunction<K>, Map<K, Integer> {
    /**
     * An entry set providing fast iteration.
     * <p>In some cases (e.g., hash-based classes) iteration over an entry set requires the creation
     * of a large number of {@link Map.Entry} objects. Some {@code fastutil}
     * maps might return {@linkplain Map#entrySet() entry set} objects of type {@code FastEntrySet}: in this case, {@link #fastIterator() fastIterator()}
     * will return an iterator that is guaranteed not to create a large number of objects, <em>possibly
     * by returning always the same entry</em> (of course, mutated), and {@link #fastForEach(Consumer)} will apply
     * the provided consumer to all elements of the entry set, <em>which might be represented
     * always by the same entry</em> (of course, mutated).
     */
    interface FastEntrySet<K> extends ObjectSet<Entry<K>> {
        /**
         * Returns a fast iterator over this entry set; the iterator might return always the same entry instance, suitably mutated.
         *
         * @return a fast iterator over this entry set; the iterator might return always the same {@link Map.Entry} instance, suitably mutated.
         */
        ObjectIterator<Entry<K>> fastIterator();

        /**
         * Iterates quickly over this entry set; the iteration might happen always on the same entry instance, suitably mutated.
         * <p>This default implementation just delegates to {@link #forEach(Consumer)}.
         *
         * @param consumer a consumer that will by applied to the entries of this set; the entries might be represented
         * by the same entry instance, suitably mutated.
         * @since 8.1.0
         */
        default void fastForEach(final Consumer<? super Entry<K>> consumer) {
            forEach(consumer);
        }
    }

    /**
     * Returns the number of key/value mappings in this map.  If the
     * map contains more than {@link Integer#MAX_VALUE} elements, returns {@link Integer#MAX_VALUE}.
     *
     * @return the number of key-value mappings in this map.
     */
    @Override
    int size();

    /**
     * Removes all of the mappings from this map (optional operation).
     * The map will be empty after this call returns.
     *
     * @throws UnsupportedOperationException if the <tt>clear</tt> operation is not supported by this map
     */
    @Override
    default void clear() {
        throw new UnsupportedOperationException();
    }

    /**
     * Sets the default return value (optional operation).
     * This value must be returned by type-specific versions of {@code get()},
     * {@code put()} and {@code remove()} to denote that the map does not contain
     * the specified key. It must be 0/{@code false} by default.
     *
     * @param rv the new default return value.
     * @see #defaultReturnValue()
     */
    @Override
    void defaultReturnValue(int rv);

    /**
     * Gets the default return value.
     *
     * @return the current default return value.
     */
    @Override
    int defaultReturnValue();

    /**
     * Returns a type-specific set view of the mappings contained in this map.
     * <p>This method is necessary because there is no inheritance along
     * type parameters: it is thus impossible to strengthen {@link Map#entrySet()}
     * of type-specific entries (the latter makes it possible to
     * access keys and values with type-specific methods).
     *
     * @return a type-specific set view of the mappings contained in this map.
     * @see Map#entrySet()
     */
    ObjectSet<Entry<K>> object2IntEntrySet();

    /**
     * Returns a set view of the mappings contained in this map.
     * <p>Note that this specification strengthens the one given in {@link Map#entrySet()}.
     *
     * @return a set view of the mappings contained in this map.
     * @see Map#entrySet()
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    default ObjectSet<Map.Entry<K, Integer>> entrySet() {
        return (ObjectSet) object2IntEntrySet();
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    default Integer put(final K key, final Integer value) {
        return Object2IntFunction.super.put(key, value);
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    default Integer get(final Object key) {
        return Object2IntFunction.super.get(key);
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    default Integer remove(final Object key) {
        return Object2IntFunction.super.remove(key);
    }

    /**
     * {@inheritDoc}
     * <p>Note that this specification strengthens the one given in {@link Map#keySet()}.
     *
     * @return a set view of the keys contained in this map.
     * @see Map#keySet()
     */
    @Override
    ObjectSet<K> keySet();

    /**
     * {@inheritDoc}
     * <p>Note that this specification strengthens the one given in {@link Map#values()}.
     *
     * @return a set view of the values contained in this map.
     * @see Map#values()
     */
    @Override
    IntCollection values();

    /**
     * Returns true if this function contains a mapping for the specified key.
     *
     * @param key the key.
     * @return true if this function associates a value to {@code key}.
     * @see Map#containsKey(Object)
     */
    @Override
    boolean containsKey(Object key);

    /**
     * Returns {@code true} if this map maps one or more keys to the specified value.
     *
     * @see Map#containsValue(Object)
     */
    boolean containsValue(int value);

    /**
     * {@inheritDoc}
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    default boolean containsValue(final Object value) {
        return value == null ? false : containsValue(((Integer) (value)).intValue());
    }

    /**
     * {@inheritDoc}
     * <p>This default implementation just delegates to the corresponding {@link Map} method.
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    default Integer getOrDefault(final Object key, final Integer defaultValue) {
        return Map.super.getOrDefault(key, defaultValue);
    }

    /**
     * {@inheritDoc}
     * <p>This default implementation just delegates to the corresponding {@link Map} method.
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    default Integer putIfAbsent(final K key, final Integer value) {
        return Map.super.putIfAbsent(key, value);
    }

    /**
     * {@inheritDoc}
     * <p>This default implementation just delegates to the corresponding {@link Map} method.
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    default boolean remove(final Object key, final Object value) {
        return Map.super.remove(key, value);
    }

    /**
     * {@inheritDoc}
     * <p>This default implementation just delegates to the corresponding {@link Map} method.
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    default boolean replace(final K key, final Integer oldValue, final Integer newValue) {
        return Map.super.replace(key, oldValue, newValue);
    }

    /**
     * {@inheritDoc}
     * <p>This default implementation just delegates to the corresponding {@link Map} method.
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    default Integer replace(final K key, final Integer value) {
        return Map.super.replace(key, value);
    }

    /**
     * {@inheritDoc}
     * <p>This default implementation just delegates to the corresponding {@link Map} method.
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    default Integer merge(final K key, final Integer value, final java.util.function.BiFunction<? super Integer, ? super Integer, ? extends Integer> remappingFunction) {
        return Map.super.merge(key, value, remappingFunction);
    }

    /**
     * A type-specific {@link Map.Entry}; provides some additional methods
     * that use polymorphism to avoid (un)boxing.
     *
     * @see Map.Entry
     */
    interface Entry<K> extends Map.Entry<K, Integer> {
        /**
         * Returns the value corresponding to this entry.
         *
         * @see Map.Entry#getValue()
         */
        int getIntValue();

        /**
         * Replaces the value corresponding to this entry with the specified value (optional operation).
         *
         * @see Map.Entry#setValue(Object)
         */
        int setValue(final int value);

        /**
         * {@inheritDoc}
         *
         * @deprecated Please use the corresponding type-specific method instead.
         */
        @Deprecated
        @Override
        default Integer getValue() {
            return Integer.valueOf(getIntValue());
        }

        /**
         * {@inheritDoc}
         *
         * @deprecated Please use the corresponding type-specific method instead.
         */
        @Deprecated
        @Override
        default Integer setValue(final Integer value) {
            return Integer.valueOf(setValue((value).intValue()));
        }
    }
}

/**
 * A type-specific bidirectional iterator that is also a {@link ListIterator}.
 * <p>This interface merges the methods provided by a {@link ListIterator} and
 * type-specific versions of {@link ListIterator#add(Object) add()}
 * and {@link ListIterator#set(Object) set()}.
 *
 * @see java.util.ListIterator
 */
interface ObjectListIterator<K> extends ObjectBidirectionalIterator<K>, ListIterator<K> {
    /**
     * Replaces the last element returned by {@link #next} or
     * {@link #previous} with the specified element (optional operation).
     *
     * @param k the element used to replace the last element returned.
     * <p>This default implementation just throws an {@link UnsupportedOperationException}.
     * @see ListIterator#set(Object)
     */
    @Override
    default void set(final K k) {
        throw new UnsupportedOperationException();
    }

    /**
     * Inserts the specified element into the list (optional operation).
     * <p>This default implementation just throws an {@link UnsupportedOperationException}.
     *
     * @param k the element to insert.
     * @see ListIterator#add(Object)
     */
    @Override
    default void add(final K k) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes from the underlying collection the last element returned
     * by this iterator (optional operation).
     * <p>This default implementation just throws an {@link UnsupportedOperationException}.
     *
     * @see ListIterator#remove()
     */
    @Override
    default void remove() {
        throw new UnsupportedOperationException();
    }
}

/**
 * An abstract class providing basic methods for collections implementing a type-specific interface.
 * <p>In particular, this class provide {@link #iterator()}, {@code add()}, {@link #remove(Object)} and
 * {@link #contains(Object)} methods that just call the type-specific counterpart.
 * <p><strong>Warning</strong>: Because of a name clash between the list and collection interfaces
 * the type-specific deletion method of a type-specific abstract
 * collection is {@code rem()}, rather then {@code remove()}. A
 * subclass must thus override {@code rem()}, rather than
 * {@code remove()}, to make all inherited methods work properly.
 */
abstract class AbstractIntCollection extends AbstractCollection<Integer> implements IntCollection {
    protected AbstractIntCollection() {
    }

    @Override
    public abstract IntIterator iterator();

    /**
     * {@inheritDoc}
     * <p>This implementation always throws an {@link UnsupportedOperationException}.
     */
    @Override
    public boolean add(final int k) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     * <p>This implementation iterates over the elements in the collection,
     * looking for the specified element.
     */
    @Override
    public boolean contains(final int k) {
        final IntIterator iterator = iterator();
        while (iterator.hasNext())
            if (k == iterator.nextInt()) return true;
        return false;
    }

    /**
     * {@inheritDoc}
     * <p>This implementation iterates over the elements in the collection,
     * looking for the specified element and tries to remove it.
     */
    @Override
    public boolean rem(final int k) {
        final IntIterator iterator = iterator();
        while (iterator.hasNext())
            if (k == iterator.nextInt()) {
                iterator.remove();
                return true;
            }
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @SuppressWarnings("deprecation")
    @Deprecated
    @Override
    public boolean add(final Integer key) {
        return IntCollection.super.add(key);
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    public boolean contains(final Object key) {
        return IntCollection.super.contains(key);
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    public boolean remove(final Object key) {
        return IntCollection.super.remove(key);
    }

    @Override
    public int[] toArray(int a[]) {
        if (a == null || a.length < size())
            a = new int[size()];
        IntIterators.unwrap(iterator(), a);
        return a;
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Please use {@code toArray()} instead&mdash;this method is redundant and will be removed in the future.
     */
    @Deprecated
    @Override
    public int[] toIntArray(final int a[]) {
        return toArray(a);
    }

    @Override
    public boolean addAll(final IntCollection c) {
        boolean retVal = false;
        for (final IntIterator i = c.iterator(); i.hasNext(); )
            if (add(i.nextInt())) retVal = true;
        return retVal;
    }

    @Override
    public boolean containsAll(final IntCollection c) {
        for (final IntIterator i = c.iterator(); i.hasNext(); )
            if (!contains(i.nextInt())) return false;
        return true;
    }

    @Override
    public boolean removeAll(final IntCollection c) {
        boolean retVal = false;
        for (final IntIterator i = c.iterator(); i.hasNext(); )
            if (rem(i.nextInt())) retVal = true;
        return retVal;
    }

    @Override
    public boolean retainAll(final IntCollection c) {
        boolean retVal = false;
        for (final IntIterator i = iterator(); i.hasNext(); )
            if (!c.contains(i.nextInt())) {
                i.remove();
                retVal = true;
            }
        return retVal;
    }

    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final IntIterator i = iterator();
        int n = size();
        int k;
        boolean first = true;
        s.append("{");
        while (n-- != 0) {
            if (first) first = false;
            else s.append(", ");
            k = i.nextInt();
            s.append(String.valueOf(k));
        }
        s.append("}");
        return s.toString();
    }
}

/**
 * A type-specific {@link List}; provides some additional methods that use polymorphism to avoid (un)boxing.
 * <p>Note that this type-specific interface extends {@link Comparable}: it is expected that implementing
 * classes perform a lexicographical comparison using the standard operator "less then" for primitive types,
 * and the usual {@link Comparable#compareTo(Object) compareTo()} method for objects.
 * <p>Additionally, this interface strengthens {@link #listIterator()},
 * {@link #listIterator(int)} and {@link #subList(int, int)}.
 * <p>Besides polymorphic methods, this interfaces specifies methods to copy into an array or remove contiguous
 * sublists. Although the abstract implementation of this interface provides simple, one-by-one implementations
 * of these methods, it is expected that concrete implementation override them with optimized versions.
 *
 * @see List
 */
interface ObjectList<K> extends List<K>, Comparable<List<? extends K>>, ObjectCollection<K> {
    /**
     * Returns a type-specific iterator on the elements of this list.
     * <p>Note that this specification strengthens the one given in {@link List#iterator()}.
     * It would not be normally necessary, but {@link java.lang.Iterable#iterator()} is bizarrily re-specified
     * in {@link List}.
     *
     * @return an iterator on the elements of this list.
     */
    @Override
    ObjectListIterator<K> iterator();

    /**
     * Returns a type-specific list iterator on the list.
     *
     * @see List#listIterator()
     */
    @Override
    ObjectListIterator<K> listIterator();

    /**
     * Returns a type-specific list iterator on the list starting at a given index.
     *
     * @see List#listIterator(int)
     */
    @Override
    ObjectListIterator<K> listIterator(int index);

    /**
     * Returns a type-specific view of the portion of this list from the index {@code from}, inclusive, to the index {@code to}, exclusive.
     * <p>Note that this specification strengthens the one given in {@link List#subList(int, int)}.
     *
     * @see List#subList(int, int)
     */
    @Override
    ObjectList<K> subList(int from, int to);

    /**
     * Copies (hopefully quickly) elements of this type-specific list into the given array.
     *
     * @param from the start index (inclusive).
     * @param a the destination array.
     * @param offset the offset into the destination array where to store the first element copied.
     * @param length the number of elements to be copied.
     */
    void getElements(int from, Object a[], int offset, int length);

    /**
     * Removes (hopefully quickly) elements of this type-specific list.
     *
     * @param from the start index (inclusive).
     * @param to the end index (exclusive).
     */
    void removeElements(int from, int to);

    /**
     * Add (hopefully quickly) elements to this type-specific list.
     *
     * @param index the index at which to add elements.
     * @param a the array containing the elements.
     * @param offset the offset of the first element to add.
     * @param length the number of elements to add.
     */
    void addElements(int index, K a[], int offset, int length);
}

/**
 * A stack.
 * or even a more powerful {@link #peek(int)} method that provides
 * access to all elements on the stack (indexed from the top, which
 * has index 0).
 */
interface Stack<K> {
    /**
     * Peeks at an element on the stack (optional operation).
     * <p>This default implementation just throws an {@link UnsupportedOperationException}.
     *
     * @param i an index from the stop of the stack (0 represents the top).
     * @return the {@code i}-th element on the stack.
     * @throws IndexOutOfBoundsException if the designated element does not exist..
     */
    default K peek(int i) {
        throw new UnsupportedOperationException();
    }
}

/**
 * An abstract class providing basic methods for lists implementing a type-specific list interface.
 * <p>As an additional bonus, this class implements on top of the list operations a type-specific stack.
 */
abstract class AbstractObjectList<K> extends AbstractObjectCollection<K> implements ObjectList<K>, Stack<K> {
    protected AbstractObjectList() {
    }

    /**
     * Ensures that the given index is nonnegative and not greater than the list size.
     *
     * @param index an index.
     * @throws IndexOutOfBoundsException if the given index is negative or greater than the list size.
     */
    protected void ensureIndex(final int index) {
        if (index < 0)
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        if (index > size())
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than list size (" + (size()) + ")");
    }

    /**
     * Ensures that the given index is nonnegative and smaller than the list size.
     *
     * @param index an index.
     * @throws IndexOutOfBoundsException if the given index is negative or not smaller than the list size.
     */
    protected void ensureRestrictedIndex(final int index) {
        if (index < 0)
            throw new IndexOutOfBoundsException("Index (" + index + ") is negative");
        if (index >= size())
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + (size()) + ")");
    }

    /**
     * {@inheritDoc}
     * <p>This implementation always throws an {@link UnsupportedOperationException}.
     */
    @Override
    public void add(final int index, final K k) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     * <p>This implementation delegates to the type-specific version of {@link List#add(int, Object)}.
     */
    @Override
    public boolean add(final K k) {
        add(size(), k);
        return true;
    }

    /**
     * {@inheritDoc}
     * <p>This implementation always throws an {@link UnsupportedOperationException}.
     */
    @Override
    public K remove(final int i) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     * <p>This implementation always throws an {@link UnsupportedOperationException}.
     */
    @Override
    public K set(final int index, final K k) {
        throw new UnsupportedOperationException();
    }

    /** Adds all of the elements in the specified collection to this list (optional operation). */
    @Override
    public boolean addAll(int index, final Collection<? extends K> c) {
        ensureIndex(index);
        final Iterator<? extends K> i = c.iterator();
        final boolean retVal = i.hasNext();
        while (i.hasNext()) add(index++, (i.next()));
        return retVal;
    }

    /**
     * {@inheritDoc}
     * <p>This implementation delegates to the type-specific version of {@link List#addAll(int, Collection)}.
     */
    @Override
    public boolean addAll(final Collection<? extends K> c) {
        return addAll(size(), c);
    }

    /**
     * {@inheritDoc}
     * <p>This implementation delegates to {@link #listIterator()}.
     */
    @Override
    public ObjectListIterator<K> iterator() {
        return listIterator();
    }

    /**
     * {@inheritDoc}
     * <p>This implementation delegates to {@link #listIterator(int) listIterator(0)}.
     */
    @Override
    public ObjectListIterator<K> listIterator() {
        return listIterator(0);
    }

    /**
     * {@inheritDoc}
     * <p>This implementation is based on the random-access methods.
     */
    @Override
    public ObjectListIterator<K> listIterator(final int index) {
        ensureIndex(index);
        return new ObjectListIterator<K>() {
            int pos = index, last = -1;

            @Override
            public boolean hasNext() {
                return pos < AbstractObjectList.this.size();
            }

            @Override
            public boolean hasPrevious() {
                return pos > 0;
            }

            @Override
            public K next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                return AbstractObjectList.this.get(last = pos++);
            }

            @Override
            public K previous() {
                if (!hasPrevious())
                    throw new NoSuchElementException();
                return AbstractObjectList.this.get(last = --pos);
            }

            @Override
            public int nextIndex() {
                return pos;
            }

            @Override
            public int previousIndex() {
                return pos - 1;
            }

            @Override
            public void add(final K k) {
                AbstractObjectList.this.add(pos++, k);
                last = -1;
            }

            @Override
            public void set(final K k) {
                if (last == -1)
                    throw new IllegalStateException();
                AbstractObjectList.this.set(last, k);
            }

            @Override
            public void remove() {
                if (last == -1)
                    throw new IllegalStateException();
                AbstractObjectList.this.remove(last);
                /* If the last operation was a next(), we are removing an element *before* us, and we must decrease pos correspondingly. */
                if (last < pos) pos--;
                last = -1;
            }
        };
    }

    /**
     * Returns true if this list contains the specified element.
     * <p>This implementation delegates to {@code indexOf()}.
     *
     * @see List#contains(Object)
     */
    @Override
    public boolean contains(final Object k) {
        return indexOf(k) >= 0;
    }

    @Override
    public int indexOf(final Object k) {
        final ObjectListIterator<K> i = listIterator();
        K e;
        while (i.hasNext()) {
            e = i.next();
            if (java.util.Objects.equals(k, e))
                return i.previousIndex();
        }
        return -1;
    }

    @Override
    public int lastIndexOf(final Object k) {
        ObjectListIterator<K> i = listIterator(size());
        K e;
        while (i.hasPrevious()) {
            e = i.previous();
            if (java.util.Objects.equals(k, e))
                return i.nextIndex();
        }
        return -1;
    }

    public void size(final int size) {
        int i = size();
        if (size > i) while (i++ < size) add((null));
        else while (i-- != size) remove(i);
    }

    @Override
    public ObjectList<K> subList(final int from, final int to) {
        ensureIndex(from);
        ensureIndex(to);
        if (from > to)
            throw new IndexOutOfBoundsException("Start index (" + from + ") is greater than end index (" + to + ")");
        return new ObjectSubList<>(this, from, to);
    }

    /**
     * {@inheritDoc}
     * <p>This is a trivial iterator-based implementation. It is expected that
     * implementations will override this method with a more optimized version.
     */
    @Override
    public void removeElements(final int from, final int to) {
        ensureIndex(to);
        ObjectListIterator<K> i = listIterator(from);
        int n = to - from;
        if (n < 0)
            throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        while (n-- != 0) {
            i.next();
            i.remove();
        }
    }

    /**
     * {@inheritDoc}
     * <p>This is a trivial iterator-based implementation. It is expected that
     * implementations will override this method with a more optimized version.
     */
    @Override
    public void addElements(int index, final K a[], int offset, int length) {
        ensureIndex(index);
        if (offset < 0)
            throw new ArrayIndexOutOfBoundsException("Offset (" + offset + ") is negative");
        if (offset + length > a.length)
            throw new ArrayIndexOutOfBoundsException("End index (" + (offset + length) + ") is greater than array length (" + a.length + ")");
        while (length-- != 0) add(index++, a[offset++]);
    }

    /**
     * {@inheritDoc}
     * <p>This is a trivial iterator-based implementation. It is expected that
     * implementations will override this method with a more optimized version.
     */
    @Override
    public void getElements(final int from, final Object a[], int offset, int length) {
        ObjectListIterator<K> i = listIterator(from);
        if (offset < 0)
            throw new ArrayIndexOutOfBoundsException("Offset (" + offset + ") is negative");
        if (offset + length > a.length)
            throw new ArrayIndexOutOfBoundsException("End index (" + (offset + length) + ") is greater than array length (" + a.length + ")");
        if (from + length > size())
            throw new IndexOutOfBoundsException("End index (" + (from + length) + ") is greater than list size (" + size() + ")");
        while (length-- != 0) a[offset++] = i.next();
    }

    /**
     * {@inheritDoc}
     * <p>This implementation delegates to {@link #removeElements(int, int)}.
     */
    @Override
    public void clear() {
        removeElements(0, size());
    }

    private boolean valEquals(final Object a, final Object b) {
        return a == null ? b == null : a.equals(b);
    }

    /**
     * Returns the hash code for this list, which is identical to {@link java.util.List#hashCode()}.
     *
     * @return the hash code for this list.
     */
    @Override
    public int hashCode() {
        ObjectIterator<K> i = iterator();
        int h = 1, s = size();
        while (s-- != 0) {
            K k = i.next();
            h = 31 * h + ((k) == null ? 0 : (k).hashCode());
        }
        return h;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof List)) return false;
        final List<?> l = (List<?>) o;
        int s = size();
        if (s != l.size()) return false;
        final ListIterator<?> i1 = listIterator(), i2 = l.listIterator();
        while (s-- != 0)
            if (!valEquals(i1.next(), i2.next()))
                return false;
        return true;
    }

    /**
     * Compares this list to another object. If the
     * argument is a {@link java.util.List}, this method performs a lexicographical comparison; otherwise,
     * it throws a {@code ClassCastException}.
     *
     * @param l a list.
     * @return if the argument is a {@link java.util.List}, a negative integer,
     * zero, or a positive integer as this list is lexicographically less than, equal
     * to, or greater than the argument.
     * @throws ClassCastException if the argument is not a list.
     */
    @SuppressWarnings("unchecked")
    @Override
    public int compareTo(final List<? extends K> l) {
        if (l == this) return 0;
        if (l instanceof ObjectList) {
            final ObjectListIterator<K> i1 = listIterator(), i2 = ((ObjectList<K>) l).listIterator();
            int r;
            K e1, e2;
            while (i1.hasNext() && i2.hasNext()) {
                e1 = i1.next();
                e2 = i2.next();
                if ((r = (((Comparable<K>) (e1)).compareTo(e2))) != 0)
                    return r;
            }
            return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
        }
        ListIterator<? extends K> i1 = listIterator(), i2 = l.listIterator();
        int r;
        while (i1.hasNext() && i2.hasNext()) {
            if ((r = ((Comparable<? super K>) i1.next()).compareTo(i2.next())) != 0)
                return r;
        }
        return i2.hasNext() ? -1 : (i1.hasNext() ? 1 : 0);
    }

    @Override
    public K peek(final int i) {
        return get(size() - 1 - i);
    }

    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<K> i = iterator();
        int n = size();
        K k;
        boolean first = true;
        s.append("[");
        while (n-- != 0) {
            if (first) first = false;
            else s.append(", ");
            k = i.next();
            if (this == k) s.append("(this list)");
            else
                s.append(String.valueOf(k));
        }
        s.append("]");
        return s.toString();
    }

    /** A class implementing a sublist view. */
    public static class ObjectSubList<K> extends AbstractObjectList<K> implements java.io.Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        /** The list this sublist restricts. */
        protected final ObjectList<K> l;
        /** Initial (inclusive) index of this sublist. */
        protected final int from;
        /** Final (exclusive) index of this sublist. */
        protected int to;

        public ObjectSubList(final ObjectList<K> l, final int from, final int to) {
            this.l = l;
            this.from = from;
            this.to = to;
        }

        private boolean assertRange() {
            assert from <= l.size();
            assert to <= l.size();
            assert to >= from;
            return true;
        }

        @Override
        public boolean add(final K k) {
            l.add(to, k);
            to++;
            assert assertRange();
            return true;
        }

        @Override
        public void add(final int index, final K k) {
            ensureIndex(index);
            l.add(from + index, k);
            to++;
            assert assertRange();
        }

        @Override
        public boolean addAll(final int index, final Collection<? extends K> c) {
            ensureIndex(index);
            to += c.size();
            return l.addAll(from + index, c);
        }

        @Override
        public K get(final int index) {
            ensureRestrictedIndex(index);
            return l.get(from + index);
        }

        @Override
        public K remove(final int index) {
            ensureRestrictedIndex(index);
            to--;
            return l.remove(from + index);
        }

        @Override
        public K set(final int index, final K k) {
            ensureRestrictedIndex(index);
            return l.set(from + index, k);
        }

        @Override
        public int size() {
            return to - from;
        }

        @Override
        public void getElements(final int from, final Object[] a, final int offset, final int length) {
            ensureIndex(from);
            if (from + length > size())
                throw new IndexOutOfBoundsException("End index (" + from + length + ") is greater than list size (" + size() + ")");
            l.getElements(this.from + from, a, offset, length);
        }

        @Override
        public void removeElements(final int from, final int to) {
            ensureIndex(from);
            ensureIndex(to);
            l.removeElements(this.from + from, this.from + to);
            this.to -= (to - from);
            assert assertRange();
        }

        @Override
        public void addElements(int index, final K a[], int offset, int length) {
            ensureIndex(index);
            l.addElements(this.from + index, a, offset, length);
            this.to += length;
            assert assertRange();
        }

        @Override
        public ObjectListIterator<K> listIterator(final int index) {
            ensureIndex(index);
            return new ObjectListIterator<K>() {
                int pos = index, last = -1;

                @Override
                public boolean hasNext() {
                    return pos < size();
                }

                @Override
                public boolean hasPrevious() {
                    return pos > 0;
                }

                @Override
                public K next() {
                    if (!hasNext())
                        throw new NoSuchElementException();
                    return l.get(from + (last = pos++));
                }

                @Override
                public K previous() {
                    if (!hasPrevious())
                        throw new NoSuchElementException();
                    return l.get(from + (last = --pos));
                }

                @Override
                public int nextIndex() {
                    return pos;
                }

                @Override
                public int previousIndex() {
                    return pos - 1;
                }

                @Override
                public void add(K k) {
                    if (last == -1)
                        throw new IllegalStateException();
                    ObjectSubList.this.add(pos++, k);
                    last = -1;
                    assert assertRange();
                }

                @Override
                public void set(K k) {
                    if (last == -1)
                        throw new IllegalStateException();
                    ObjectSubList.this.set(last, k);
                }

                @Override
                public void remove() {
                    if (last == -1)
                        throw new IllegalStateException();
                    ObjectSubList.this.remove(last);
                    /* If the last operation was a next(), we are removing an element *before* us, and we must decrease pos correspondingly. */
                    if (last < pos) pos--;
                    last = -1;
                    assert assertRange();
                }
            };
        }

        @Override
        public ObjectList<K> subList(final int from, final int to) {
            ensureIndex(from);
            ensureIndex(to);
            if (from > to)
                throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
            return new ObjectSubList<>(this, from, to);
        }
    }
}

/** A class providing static methods and objects that do useful things with arrays.
 *
 * @see Arrays
 */

class Arrays {
    private Arrays() {}

    /** This is a safe value used by {@link ArrayList} (as of Java 7) to avoid
     *  throwing {@link OutOfMemoryError} on some JVMs. We adopt the same value. */
    public static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /** Ensures that a range given by its first (inclusive) and last (exclusive) elements fits an array of given length.
     *
     * <p>This method may be used whenever an array range check is needed.
     *
     * @param arrayLength an array length.
     * @param from a start index (inclusive).
     * @param to an end index (inclusive).
     * @throws IllegalArgumentException if {@code from} is greater than {@code to}.
     * @throws ArrayIndexOutOfBoundsException if {@code from} or {@code to} are greater than {@code arrayLength} or negative.
     */
    public static void ensureFromTo(final int arrayLength, final int from, final int to) {
        if (from < 0) throw new ArrayIndexOutOfBoundsException("Start index (" + from + ") is negative");
        if (from > to) throw new IllegalArgumentException("Start index (" + from + ") is greater than end index (" + to + ")");
        if (to > arrayLength) throw new ArrayIndexOutOfBoundsException("End index (" + to + ") is greater than array length (" + arrayLength + ")");
    }

    /** Ensures that a range given by an offset and a length fits an array of given length.
     *
     * <p>This method may be used whenever an array range check is needed.
     *
     * @param arrayLength an array length.
     * @param offset a start index for the fragment
     * @param length a length (the number of elements in the fragment).
     * @throws IllegalArgumentException if {@code length} is negative.
     * @throws ArrayIndexOutOfBoundsException if {@code offset} is negative or {@code offset}+{@code length} is greater than {@code arrayLength}.
     */
    public static void ensureOffsetLength(final int arrayLength, final int offset, final int length) {
        if (offset < 0) throw new ArrayIndexOutOfBoundsException("Offset (" + offset + ") is negative");
        if (length < 0) throw new IllegalArgumentException("Length (" + length + ") is negative");
        if (offset + length > arrayLength) throw new ArrayIndexOutOfBoundsException("Last index (" + (offset + length) + ") is greater than array length (" + arrayLength + ")");
    }
}

/**
 * A class providing static methods and objects that do useful things with type-specific arrays.
 * In particular, the {@code ensureCapacity()}, {@code grow()},
 * {@code trim()} and {@code setLength()} methods allow to handle
 * arrays much like array lists. This can be very useful when efficiency (or
 * syntactic simplicity) reasons make array lists unsuitable.
 * <p><strong>Warning:</strong> if your array is not of type {@code Object[]},
 * {@link #ensureCapacity(Object[], int, int)} and {@link #grow(Object[], int, int)}
 * will use {@linkplain java.lang.reflect.Array#newInstance(Class, int) reflection}
 * to preserve your array type. Reflection is <em>significantly slower</em> than using {@code new}.
 * This phenomenon is particularly
 * evident in the first growth phases of an array reallocated with doubling (or similar) logic.
 * <h2>Sorting</h2>
 * <p>There are several sorting methods available. The main theme is that of letting you choose
 * the sorting algorithm you prefer (i.e., trading stability of mergesort for no memory allocation in quicksort).
 * Several algorithms provide a parallel version, that will use the {@linkplain Runtime#availableProcessors() number of cores available}.
 * <p>All comparison-based algorithm have an implementation based on a type-specific comparator.
 * <p>If you are fine with not knowing exactly which algorithm will be run (in particular, not knowing exactly whether a support array will be allocated),
 * the dual-pivot parallel sorts in {@link java.util.Arrays}
 * are about 50% faster than the classical single-pivot implementation used here.
 * <p>In any case, if sorting time is important I suggest that you benchmark your sorting load
 * with your data distribution and on your architecture.
 *
 * @see java.util.Arrays
 */
final class ObjectArrays {
    private ObjectArrays() {
    }

    /** A static, final, empty array. */
    public static final Object[] EMPTY_ARRAY = {};

    /**
     * Creates a new array using a the given one as prototype.
     * <p>This method returns a new array of the given length whose element
     * are of the same class as of those of {@code prototype}. In case
     * of an empty array, it tries to return {@link #EMPTY_ARRAY}, if possible.
     *
     * @param prototype an array that will be used to type the new one.
     * @param length the length of the new array.
     * @return a new array of given type and length.
     */
    @SuppressWarnings("unchecked")
    private static <K> K[] newArray(final K[] prototype, final int length) {
        final Class<?> klass = prototype.getClass();
        if (klass == Object[].class)
            return (K[]) (length == 0 ? EMPTY_ARRAY : new Object[length]);
        return (K[]) java.lang.reflect.Array.newInstance(klass.getComponentType(), length);
    }

    /**
     * Ensures that an array can contain the given number of entries, preserving just a part of the array.
     *
     * @param array an array.
     * @param length the new minimum length for this array.
     * @param preserve the number of elements of the array that must be preserved in case a new allocation is necessary.
     * @return {@code array}, if it can contain {@code length} entries or more; otherwise,
     * an array with {@code length} entries whose first {@code preserve}
     * entries are the same as those of {@code array}.
     */
    public static <K> K[] ensureCapacity(final K[] array, final int length, final int preserve) {
        if (length > array.length) {
            final K t[] =
                    newArray(array, length);
            System.arraycopy(array, 0, t, 0, preserve);
            return t;
        }
        return array;
    }

    /**
     * Grows the given array to the maximum between the given length and
     * the current length multiplied by two, provided that the given
     * length is larger than the current length, preserving just a part of the array.
     * <p>If you want complete control on the array growth, you
     * should probably use {@code ensureCapacity()} instead.
     *
     * @param array an array.
     * @param length the new minimum length for this array.
     * @param preserve the number of elements of the array that must be preserved in case a new allocation is necessary.
     * @return {@code array}, if it can contain {@code length}
     * entries; otherwise, an array with
     * max({@code length},{@code array.length}/&phi;) entries whose first
     * {@code preserve} entries are the same as those of {@code array}.
     */
    public static <K> K[] grow(final K[] array, final int length, final int preserve) {
        if (length > array.length) {
            final int newLength = (int) Math.max(Math.min(2L * array.length, Arrays.MAX_ARRAY_SIZE), length);
            final K t[] =
                    newArray(array, newLength);
            System.arraycopy(array, 0, t, 0, preserve);
            return t;
        }
        return array;
    }

    /**
     * Fills the given array with the given value.
     *
     * @param array an array.
     * @param value the new value for all elements of the array.
     * @deprecated Please use the corresponding {@link java.util.Arrays} method.
     */
    @Deprecated
    public static <K> void fill(final K[] array, final K value) {
        int i = array.length;
        while (i-- != 0) array[i] = value;
    }

    /**
     * Fills a portion of the given array with the given value.
     *
     * @param array an array.
     * @param from the starting index of the portion to fill (inclusive).
     * @param to the end index of the portion to fill (exclusive).
     * @param value the new value for all elements of the specified portion of the array.
     * @deprecated Please use the corresponding {@link java.util.Arrays} method.
     */
    @Deprecated
    public static <K> void fill(final K[] array, final int from, int to, final K value) {
        ensureFromTo(array, from, to);
        if (from == 0) while (to-- != 0) array[to] = value;
        else for (int i = from; i < to; i++)
            array[i] = value;
    }

    /**
     * Returns true if the two arrays are elementwise equal.
     *
     * @param a1 an array.
     * @param a2 another array.
     * @return true if the two arrays are of the same length, and their elements are equal.
     * @deprecated Please use the corresponding {@link java.util.Arrays} method, which is intrinsified in recent JVMs.
     */
    @Deprecated
    public static <K> boolean equals(final K[] a1, final K a2[]) {
        int i = a1.length;
        if (i != a2.length) return false;
        while (i-- != 0)
            if (!java.util.Objects.equals(a1[i], a2[i]))
                return false;
        return true;
    }

    /**
     * Ensures that a range given by its first (inclusive) and last (exclusive) elements fits an array.
     * <p>This method may be used whenever an array range check is needed.
     *
     * @param a an array.
     * @param from a start index (inclusive).
     * @param to an end index (exclusive).
     * @throws IllegalArgumentException if {@code from} is greater than {@code to}.
     * @throws ArrayIndexOutOfBoundsException if {@code from} or {@code to} are greater than the array length or negative.
     */
    public static <K> void ensureFromTo(final K[] a, final int from, final int to) {
        Arrays.ensureFromTo(a.length, from, to);
    }

    /**
     * Ensures that a range given by an offset and a length fits an array.
     * <p>This method may be used whenever an array range check is needed.
     *
     * @param a an array.
     * @param offset a start index.
     * @param length a length (the number of elements in the range).
     * @throws IllegalArgumentException if {@code length} is negative.
     * @throws ArrayIndexOutOfBoundsException if {@code offset} is negative or {@code offset}+{@code length} is greater than the array length.
     */
    public static <K> void ensureOffsetLength(final K[] a, final int offset, final int length) {
        Arrays.ensureOffsetLength(a.length, offset, length);
    }
}

class ObjectArrayList<K> extends AbstractObjectList<K> implements RandomAccess, Cloneable, java.io.Serializable {
    private static final long serialVersionUID = -7046029254386353131L;
    /**
     * Whether the backing array was passed to {@code wrap()}. In
     * this case, we must reallocate with the same type of array.
     */
    protected final boolean wrapped;
    /** The backing array. */
    protected transient K a[];
    /** The current actual size of the list (never greater than the backing-array length). */
    protected int size;
    private static final boolean ASSERTS = false;

    /**
     * Creates a new array list with given capacity.
     *
     * @param capacity the initial capacity of the array list (may be 0).
     */
    @SuppressWarnings("unchecked")
    public ObjectArrayList(final int capacity) {
        if (capacity < 0)
            throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
        a = (K[]) new Object[capacity];
        wrapped = false;
    }

    /**
     * Ensures that this array list can contain the given number of entries without resizing.
     *
     * @param capacity the new minimum capacity for this array list.
     */
    @SuppressWarnings("unchecked")
    public void ensureCapacity(final int capacity) {
        if (wrapped)
            a = ObjectArrays.ensureCapacity(a, capacity, size);
        else {
            if (capacity > a.length) {
                final Object t[] = new Object[capacity];
                System.arraycopy(a, 0, t, 0, size);
                a = (K[]) t;
            }
        }
        if (ASSERTS) assert size <= a.length;
    }

    /**
     * Grows this array list, ensuring that it can contain the given number of entries without resizing,
     * and in case enlarging it at least by a factor of two.
     *
     * @param capacity the new minimum capacity for this array list.
     */
    @SuppressWarnings("unchecked")
    private void grow(final int capacity) {
        if (wrapped)
            a = ObjectArrays.grow(a, capacity, size);
        else {
            if (capacity > a.length) {
                final int newLength = (int) Math.max(Math.min(2L * a.length, Arrays.MAX_ARRAY_SIZE), capacity);
                final Object t[] = new Object[newLength];
                System.arraycopy(a, 0, t, 0, size);
                a = (K[]) t;
            }
        }
        if (ASSERTS) assert size <= a.length;
    }

    @Override
    public void add(final int index, final K k) {
        ensureIndex(index);
        grow(size + 1);
        if (index != size)
            System.arraycopy(a, index, a, index + 1, size - index);
        a[index] = k;
        size++;
        if (ASSERTS) assert size <= a.length;
    }

    @Override
    public boolean add(final K k) {
        grow(size + 1);
        a[size++] = k;
        if (ASSERTS) assert size <= a.length;
        return true;
    }

    @Override
    public K get(final int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + size + ")");
        return a[index];
    }

    @Override
    public int indexOf(final Object k) {
        for (int i = 0; i < size; i++)
            if (Objects.equals(k, a[i])) return i;
        return -1;
    }

    @Override
    public int lastIndexOf(final Object k) {
        for (int i = size; i-- != 0; )
            if (Objects.equals(k, a[i])) return i;
        return -1;
    }

    @Override
    public K remove(final int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + size + ")");
        final K old = a[index];
        size--;
        if (index != size)
            System.arraycopy(a, index + 1, a, index, size - index);
        a[size] = null;
        if (ASSERTS) assert size <= a.length;
        return old;
    }

    @Override
    public boolean remove(final Object k) {
        int index = indexOf(k);
        if (index == -1) return false;
        remove(index);
        if (ASSERTS) assert size <= a.length;
        return true;
    }

    @Override
    public K set(final int index, final K k) {
        if (index >= size)
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + size + ")");
        K old = a[index];
        a[index] = k;
        return old;
    }

    @Override
    public void clear() {
        java.util.Arrays.fill(a, 0, size, null);
        size = 0;
        if (ASSERTS) assert size <= a.length;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void size(final int size) {
        if (size > a.length) ensureCapacity(size);
        if (size > this.size)
            java.util.Arrays.fill(a, this.size, size, (null));
        else java.util.Arrays.fill(a, size, this.size, (null));
        this.size = size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Copies element of this type-specific list into the given array using optimized system calls.
     *
     * @param from the start index (inclusive).
     * @param a the destination array.
     * @param offset the offset into the destination array where to store the first element copied.
     * @param length the number of elements to be copied.
     */
    @Override
    public void getElements(final int from, final Object[] a, final int offset, final int length) {
        ObjectArrays.ensureOffsetLength(a, offset, length);
        System.arraycopy(this.a, from, a, offset, length);
    }

    /**
     * Removes elements of this type-specific list using optimized system calls.
     *
     * @param from the start index (inclusive).
     * @param to the end index (exclusive).
     */
    @Override
    public void removeElements(final int from, final int to) {
        Arrays.ensureFromTo(size, from, to);
        System.arraycopy(a, to, a, from, size - to);
        size -= (to - from);
        int i = to - from;
        while (i-- != 0) a[size + i] = null;
    }

    /**
     * Adds elements to this type-specific list using optimized system calls.
     *
     * @param index the index at which to add elements.
     * @param a the array containing the elements.
     * @param offset the offset of the first element to add.
     * @param length the number of elements to add.
     */
    @Override
    public void addElements(final int index, final K a[], final int offset, final int length) {
        ensureIndex(index);
        ObjectArrays.ensureOffsetLength(a, offset, length);
        grow(size + length);
        System.arraycopy(this.a, index, this.a, index + length, size - index);
        System.arraycopy(a, offset, this.a, index, length);
        size += length;
    }

    @Override
    public boolean removeAll(final Collection<?> c) {
        final Object[] a = this.a;
        int j = 0;
        for (int i = 0; i < size; i++)
            if (!c.contains((a[i]))) a[j++] = a[i];
        java.util.Arrays.fill(a, j, size, null);
        final boolean modified = size != j;
        size = j;
        return modified;
    }

    @Override
    public ObjectListIterator<K> listIterator(final int index) {
        ensureIndex(index);
        return new ObjectListIterator<K>() {
            int pos = index, last = -1;

            @Override
            public boolean hasNext() {
                return pos < size;
            }

            @Override
            public boolean hasPrevious() {
                return pos > 0;
            }

            @Override
            public K next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                return a[last = pos++];
            }

            @Override
            public K previous() {
                if (!hasPrevious())
                    throw new NoSuchElementException();
                return a[last = --pos];
            }

            @Override
            public int nextIndex() {
                return pos;
            }

            @Override
            public int previousIndex() {
                return pos - 1;
            }

            @Override
            public void add(K k) {
                ObjectArrayList.this.add(pos++, k);
                last = -1;
            }

            @Override
            public void set(K k) {
                if (last == -1)
                    throw new IllegalStateException();
                ObjectArrayList.this.set(last, k);
            }

            @Override
            public void remove() {
                if (last == -1)
                    throw new IllegalStateException();
                ObjectArrayList.this.remove(last);
                /* If the last operation was a next(), we are removing an element *before* us, and we must decrease pos correspondingly. */
                if (last < pos) pos--;
                last = -1;
            }
        };
    }

    @Override
    public ObjectArrayList<K> clone() {
        ObjectArrayList<K> c = new ObjectArrayList<>(size);
        System.arraycopy(a, 0, c.a, 0, size);
        c.size = size;
        return c;
    }
}

/**
 * A type-specific bidirectional iterator; provides an additional method to avoid (un)boxing,
 * and the possibility to skip elements backwards.
 *
 * @see BidirectionalIterator
 */
interface ObjectBidirectionalIterator<K> extends ObjectIterator<K>, BidirectionalIterator<K> {
    /**
     * Moves back for the given number of elements.
     * <p>The effect of this call is exactly the same as that of
     * calling {@link #previous()} for {@code n} times (possibly stopping
     * if {@link #hasPrevious()} becomes false).
     *
     * @param n the number of elements to skip back.
     * @return the number of elements actually skipped.
     * @see #previous()
     */
    default int back(final int n) {
        int i = n;
        while (i-- != 0 && hasPrevious()) previous();
        return n - i - 1;
    }

    /** {@inheritDoc} */
    @Override
    default int skip(final int n) {
        return ObjectIterator.super.skip(n);
    }
}

/**
 * A type-specific {@link Collection}; provides some additional methods
 * that use polymorphism to avoid (un)boxing.
 * <p>Additionally, this class defines strengthens (again) {@link #iterator()}.
 *
 * @see Collection
 */
interface ObjectCollection<K> extends Collection<K>, ObjectIterable<K> {
    /**
     * Returns a type-specific iterator on the elements of this collection.
     * <p>Note that this specification strengthens the one given in
     * {@link Iterable#iterator()}, which was already
     * strengthened in the corresponding type-specific class,
     * but was weakened by the fact that this interface extends {@link Collection}.
     *
     * @return a type-specific iterator on the elements of this collection.
     */
    @Override
    ObjectIterator<K> iterator();
}

/**
 * A type-specific {@link Iterable} that strengthens that specification of {@link #iterator()}.
 *
 * @see Iterable
 */
interface ObjectIterable<K> extends Iterable<K> {
    /**
     * Returns a type-specific iterator.
     * <p>Note that this specification strengthens the one given in {@link Iterable#iterator()}.
     *
     * @return a type-specific iterator.
     * @see Iterable#iterator()
     */
    @Override
    ObjectIterator<K> iterator();
}

/**
 * A type-specific {@link Iterator}; provides an additional method to avoid (un)boxing, and
 * the possibility to skip elements.
 *
 * @see Iterator
 */
interface ObjectIterator<K> extends Iterator<K> {
    /**
     * Skips the given number of elements.
     * <p>The effect of this call is exactly the same as that of calling {@link #next()} for {@code n} times (possibly stopping if {@link #hasNext()} becomes false).
     *
     * @param n the number of elements to skip.
     * @return the number of elements actually skipped.
     * @see Iterator#next()
     */
    default int skip(final int n) {
        if (n < 0)
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
        int i = n;
        while (i-- != 0 && hasNext()) next();
        return n - i - 1;
    }
}

/**
 * A type-specific {@link Set}; provides some additional methods that use polymorphism to avoid (un)boxing.
 * <p>Additionally, this interface strengthens (again) {@link #iterator()}.
 *
 * @see Set
 */
interface ObjectSet<K> extends ObjectCollection<K>, Set<K> {
    /**
     * Returns a type-specific iterator on the elements of this set.
     * <p>Note that this specification strengthens the one given in {@link Iterable#iterator()},
     * which was already strengthened in the corresponding type-specific class,
     * but was weakened by the fact that this interface extends {@link Set}.
     *
     * @return a type-specific iterator on the elements of this set.
     */
    @Override
    ObjectIterator<K> iterator();
}

/**
 * A class providing static methods and objects that do useful things with type-specific maps.
 *
 * @see Collections
 */
final class Object2IntMaps {
    private Object2IntMaps() {
    }

    /**
     * @param map a map from which we will try to extract a (fast) iterator on the entry set.
     * @return an iterator on the entry set of the given map that will be fast, if possible.
     * @since 8.0.0
     */
    @SuppressWarnings("unchecked")
    public static <K> ObjectIterator<Object2IntMap.Entry<K>> fastIterator(Object2IntMap<K> map) {
        final ObjectSet<Object2IntMap.Entry<K>> entries = map.object2IntEntrySet();
        return entries instanceof Object2IntMap.FastEntrySet ? ((Object2IntMap.FastEntrySet<K>) entries).fastIterator() : entries.iterator();
    }

    /**
     * @param map a map from which we will try to extract an iterable yielding a (fast) iterator on the entry set.
     * @return an iterable  yielding an iterator on the entry set of the given map that will be
     * fast, if possible.
     * @since 8.0.0
     */
    @SuppressWarnings("unchecked")
    public static <K> ObjectIterable<Object2IntMap.Entry<K>> fastIterable(Object2IntMap<K> map) {
        final ObjectSet<Object2IntMap.Entry<K>> entries = map.object2IntEntrySet();
        return entries instanceof Object2IntMap.FastEntrySet ? new ObjectIterable<Object2IntMap.Entry<K>>() {
            public ObjectIterator<Object2IntMap.Entry<K>> iterator() {
                return ((Object2IntMap.FastEntrySet<K>) entries).fastIterator();
            }

            public void forEach(final Consumer<? super Object2IntMap.Entry<K>> consumer) {
                ((Object2IntMap.FastEntrySet<K>) entries).fastForEach(consumer);
            }
        } : entries;
    }
}

/**
 * A type-specific bidirectional iterator that is also a {@link ListIterator}.
 * <p>This interface merges the methods provided by a {@link ListIterator} and
 * type-specific versions of {@link ListIterator#add(Object) add()}
 * and {@link ListIterator#set(Object) set()}.
 *
 * @see ListIterator
 */
interface IntListIterator extends IntBidirectionalIterator, ListIterator<Integer> {
    /**
     * Replaces the last element returned by {@link #next} or
     * {@link #previous} with the specified element (optional operation).
     *
     * @param k the element used to replace the last element returned.
     * <p>This default implementation just throws an {@link UnsupportedOperationException}.
     * @see ListIterator#set(Object)
     */
    default void set(final int k) {
        throw new UnsupportedOperationException();
    }

    /**
     * Inserts the specified element into the list (optional operation).
     * <p>This default implementation just throws an {@link UnsupportedOperationException}.
     *
     * @param k the element to insert.
     * @see ListIterator#add(Object)
     */
    default void add(final int k) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes from the underlying collection the last element returned
     * by this iterator (optional operation).
     * <p>This default implementation just throws an {@link UnsupportedOperationException}.
     *
     * @see ListIterator#remove()
     */
    @Override
    default void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    default void set(final Integer k) {
        set(k.intValue());
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    default void add(final Integer k) {
        add(k.intValue());
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    default Integer next() {
        return IntBidirectionalIterator.super.next();
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    default Integer previous() {
        return IntBidirectionalIterator.super.previous();
    }
}

/**
 * A class providing static methods and objects that do useful things with type-specific iterators.
 *
 * @see Iterator
 */
final class IntIterators {
    private IntIterators() {
    }

    /**
     * A class returning no elements and a type-specific iterator interface.
     * <p>This class may be useful to implement your own in case you subclass
     * a type-specific iterator.
     */
    public static class EmptyIterator implements IntListIterator, java.io.Serializable, Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptyIterator() {
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }

        @Override
        public int nextInt() {
            throw new NoSuchElementException();
        }

        @Override
        public int previousInt() {
            throw new NoSuchElementException();
        }

        @Override
        public int nextIndex() {
            return 0;
        }

        @Override
        public int previousIndex() {
            return -1;
        }

        @Override
        public int skip(int n) {
            return 0;
        }

        ;

        @Override
        public int back(int n) {
            return 0;
        }

        ;

        @Override
        public Object clone() {
            return EMPTY_ITERATOR;
        }

        private Object readResolve() {
            return EMPTY_ITERATOR;
        }
    }

    /**
     * An empty iterator. It is serializable and cloneable.
     * <p>The class of this objects represent an abstract empty iterator
     * that can iterate as a type-specific (list) iterator.
     */

    public static final EmptyIterator EMPTY_ITERATOR = new EmptyIterator();

    /** A class to wrap arrays in iterators. */
    private static class ArrayIterator implements IntListIterator {
        private final int[] array;
        private final int offset, length;
        private int curr;

        public ArrayIterator(final int[] array, final int offset, final int length) {
            this.array = array;
            this.offset = offset;
            this.length = length;
        }

        @Override
        public boolean hasNext() {
            return curr < length;
        }

        @Override
        public boolean hasPrevious() {
            return curr > 0;
        }

        @Override
        public int nextInt() {
            if (!hasNext())
                throw new NoSuchElementException();
            return array[offset + curr++];
        }

        @Override
        public int previousInt() {
            if (!hasPrevious())
                throw new NoSuchElementException();
            return array[offset + --curr];
        }

        @Override
        public int skip(int n) {
            if (n <= length - curr) {
                curr += n;
                return n;
            }
            n = length - curr;
            curr = length;
            return n;
        }

        @Override
        public int back(int n) {
            if (n <= curr) {
                curr -= n;
                return n;
            }
            n = curr;
            curr = 0;
            return n;
        }

        @Override
        public int nextIndex() {
            return curr;
        }

        @Override
        public int previousIndex() {
            return curr - 1;
        }
    }

    /**
     * Unwraps an iterator into an array starting at a given offset for a given number of elements.
     * <p>This method iterates over the given type-specific iterator and stores the elements
     * returned, up to a maximum of {@code length}, in the given array starting at {@code offset}.
     * The number of actually unwrapped elements is returned (it may be less than {@code max} if
     * the iterator emits less than {@code max} elements).
     *
     * @param i a type-specific iterator.
     * @param array an array to contain the output of the iterator.
     * @param offset the first element of the array to be returned.
     * @param max the maximum number of elements to unwrap.
     * @return the number of elements unwrapped.
     */
    public static int unwrap(final IntIterator i, final int array[], int offset, final int max) {
        if (max < 0)
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        if (offset < 0 || offset + max > array.length)
            throw new IllegalArgumentException();
        int j = max;
        while (j-- != 0 && i.hasNext())
            array[offset++] = i.nextInt();
        return max - j - 1;
    }

    /**
     * Unwraps an iterator into an array.
     * <p>This method iterates over the given type-specific iterator and stores the
     * elements returned in the given array. The iteration will stop when the
     * iterator has no more elements or when the end of the array has been reached.
     *
     * @param i a type-specific iterator.
     * @param array an array to contain the output of the iterator.
     * @return the number of elements unwrapped.
     */
    public static int unwrap(final IntIterator i, final int array[]) {
        return unwrap(i, array, 0, array.length);
    }

    private static class IteratorWrapper implements IntIterator {
        final Iterator<Integer> i;

        public IteratorWrapper(final Iterator<Integer> i) {
            this.i = i;
        }

        @Override
        public boolean hasNext() {
            return i.hasNext();
        }

        @Override
        public void remove() {
            i.remove();
        }

        @Override
        public int nextInt() {
            return (i.next()).intValue();
        }
    }

    private static class IntervalIterator implements IntListIterator {
        private final int from, to;
        int curr;

        public IntervalIterator(final int from, final int to) {
            this.from = this.curr = from;
            this.to = to;
        }

        @Override
        public boolean hasNext() {
            return curr < to;
        }

        @Override
        public boolean hasPrevious() {
            return curr > from;
        }

        @Override
        public int nextInt() {
            if (!hasNext())
                throw new NoSuchElementException();
            return curr++;
        }

        @Override
        public int previousInt() {
            if (!hasPrevious())
                throw new NoSuchElementException();
            return --curr;
        }

        @Override
        public int nextIndex() {
            return curr - from;
        }

        @Override
        public int previousIndex() {
            return curr - from - 1;
        }

        @Override
        public int skip(int n) {
            if (curr + n <= to) {
                curr += n;
                return n;
            }
            n = to - curr;
            curr = to;
            return n;
        }

        @Override
        public int back(int n) {
            if (curr - n >= from) {
                curr -= n;
                return n;
            }
            n = curr - from;
            curr = from;
            return n;
        }
    }

    private static class IteratorConcatenator implements IntIterator {
        final IntIterator a[];
        int offset, length, lastOffset = -1;

        public IteratorConcatenator(final IntIterator a[], int offset, int length) {
            this.a = a;
            this.offset = offset;
            this.length = length;
            advance();
        }

        private void advance() {
            while (length != 0) {
                if (a[offset].hasNext()) break;
                length--;
                offset++;
            }
            return;
        }

        @Override
        public boolean hasNext() {
            return length > 0;
        }

        @Override
        public int nextInt() {
            if (!hasNext())
                throw new NoSuchElementException();
            int next = a[lastOffset = offset].nextInt();
            advance();
            return next;
        }

        @Override
        public void remove() {
            if (lastOffset == -1)
                throw new IllegalStateException();
            a[lastOffset].remove();
        }

        @Override
        public int skip(int n) {
            lastOffset = -1;
            int skipped = 0;
            while (skipped < n && length != 0) {
                skipped += a[offset].skip(n - skipped);
                if (a[offset].hasNext()) break;
                length--;
                offset++;
            }
            return skipped;
        }
    }
}

/**
 * A type-specific {@link Iterator}; provides an additional method to avoid (un)boxing, and
 * the possibility to skip elements.
 *
 * @see Iterator
 */
interface IntIterator extends PrimitiveIterator.OfInt {
    /**
     * Returns the next element as a primitive type.
     *
     * @return the next element in the iteration.
     * @see Iterator#next()
     */
    @Override
    int nextInt();

    /**
     * {@inheritDoc}
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    default Integer next() {
        return Integer.valueOf(nextInt());
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    default void forEachRemaining(final Consumer<? super Integer> action) {
        forEachRemaining((java.util.function.IntConsumer) action::accept);
    }

    /**
     * Skips the given number of elements.
     * <p>The effect of this call is exactly the same as that of calling {@link #next()} for {@code n} times (possibly stopping if {@link #hasNext()} becomes false).
     *
     * @param n the number of elements to skip.
     * @return the number of elements actually skipped.
     * @see Iterator#next()
     */
    default int skip(final int n) {
        if (n < 0)
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
        int i = n;
        while (i-- != 0 && hasNext()) nextInt();
        return n - i - 1;
    }
}

/**
 * A type-specific bidirectional iterator; provides an additional method to avoid (un)boxing,
 * and the possibility to skip elements backwards.
 *
 * @see BidirectionalIterator
 */
interface IntBidirectionalIterator extends IntIterator, ObjectBidirectionalIterator<Integer> {
    /**
     * Returns the previous element as a primitive type.
     *
     * @return the previous element in the iteration.
     * @see ListIterator#previous()
     */
    int previousInt();

    /**
     * {@inheritDoc}
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    default Integer previous() {
        return Integer.valueOf(previousInt());
    }

    /**
     * Moves back for the given number of elements.
     * <p>The effect of this call is exactly the same as that of
     * calling {@link #previous()} for {@code n} times (possibly stopping
     * if {@link #hasPrevious()} becomes false).
     *
     * @param n the number of elements to skip back.
     * @return the number of elements actually skipped.
     * @see #previous()
     */
    @Override
    default int back(final int n) {
        int i = n;
        while (i-- != 0 && hasPrevious()) previousInt();
        return n - i - 1;
    }

    /** {@inheritDoc} */
    @Override
    default int skip(final int n) {
        return IntIterator.super.skip(n);
    }
}

/**
 * A type-specific {@link Collection}; provides some additional methods
 * that use polymorphism to avoid (un)boxing.
 * <p>Additionally, this class defines strengthens (again) {@link #iterator()}.
 *
 * @see Collection
 */
interface IntCollection extends Collection<Integer>, IntIterable {
    /**
     * Returns a type-specific iterator on the elements of this collection.
     * <p>Note that this specification strengthens the one given in
     * {@link Iterable#iterator()}, which was already
     * strengthened in the corresponding type-specific class,
     * but was weakened by the fact that this interface extends {@link Collection}.
     *
     * @return a type-specific iterator on the elements of this collection.
     */
    @Override
    IntIterator iterator();

    /**
     * Ensures that this collection contains the specified element (optional operation).
     *
     * @see Collection#add(Object)
     */
    boolean add(int key);

    /**
     * Returns {@code true} if this collection contains the specified element.
     *
     * @see Collection#contains(Object)
     */
    boolean contains(int key);

    /**
     * Removes a single instance of the specified element from this
     * collection, if it is present (optional operation).
     * <p>Note that this method should be called {@link Collection#remove(Object) remove()}, but the clash
     * with the similarly named index-based method in the {@link List} interface
     * forces us to use a distinguished name. For simplicity, the set interfaces reinstates
     * {@code remove()}.
     *
     * @see Collection#remove(Object)
     */
    boolean rem(int key);

    /**
     * {@inheritDoc}
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    default boolean add(final Integer key) {
        return add((key).intValue());
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    default boolean contains(final Object key) {
        if (key == null) return false;
        return contains(((Integer) (key)).intValue());
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Please use (and implement) the {@code rem()} method instead.
     */
    @Deprecated
    @Override
    default boolean remove(final Object key) {
        if (key == null) return false;
        return rem(((Integer) (key)).intValue());
    }


    /**
     * Returns a primitive type array containing the items of this collection.
     * <p>Note that, contrarily to {@link Collection#toArray(Object[])}, this
     * methods just writes all elements of this collection: no special
     * value will be added after the last one.
     *
     * @param a if this array is big enough, it will be used to store this collection.
     * @return a primitive type array containing the items of this collection.
     * @see Collection#toArray(Object[])
     * @deprecated Please use {@code toArray()} instead&mdash;this method is redundant and will be removed in the future.
     */
    @Deprecated
    int[] toIntArray(int a[]);

    /**
     * Returns an array containing all of the elements in this collection; the runtime type of the returned array is that of the specified array.
     * <p>Note that, contrarily to {@link Collection#toArray(Object[])}, this
     * methods just writes all elements of this collection: no special
     * value will be added after the last one.
     *
     * @param a if this array is big enough, it will be used to store this collection.
     * @return a primitive type array containing the items of this collection.
     * @see Collection#toArray(Object[])
     */
    int[] toArray(int a[]);

    /**
     * Adds all elements of the given type-specific collection to this collection.
     *
     * @param c a type-specific collection.
     * @return {@code true} if this collection changed as a result of the call.
     * @see Collection#addAll(Collection)
     */
    boolean addAll(IntCollection c);

    /**
     * Checks whether this collection contains all elements from the given type-specific collection.
     *
     * @param c a type-specific collection.
     * @return {@code true} if this collection contains all elements of the argument.
     * @see Collection#containsAll(Collection)
     */
    boolean containsAll(IntCollection c);

    /**
     * Remove from this collection all elements in the given type-specific collection.
     *
     * @param c a type-specific collection.
     * @return {@code true} if this collection changed as a result of the call.
     * @see Collection#removeAll(Collection)
     */
    boolean removeAll(IntCollection c);

    /**
     * {@inheritDoc}
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    default boolean removeIf(final java.util.function.Predicate<? super Integer> filter) {
        return removeIf((java.util.function.IntPredicate) key -> filter.test(Integer.valueOf(key)));
    }

    /**
     * Remove from this collection all elements which satisfy the given predicate.
     *
     * @param filter a predicate which returns {@code true} for elements to be
     * removed.
     * @return {@code true} if any elements were removed.
     * @see Collection#removeIf(java.util.function.Predicate)
     */
    @SuppressWarnings("overloads")
    default boolean removeIf(final java.util.function.IntPredicate filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        final IntIterator each = iterator();
        while (each.hasNext()) {
            if (filter.test(each.nextInt())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }

    /**
     * Retains in this collection only elements from the given type-specific collection.
     *
     * @param c a type-specific collection.
     * @return {@code true} if this collection changed as a result of the call.
     * @see Collection#retainAll(Collection)
     */
    boolean retainAll(IntCollection c);
}

/**
 * A type-specific {@link Iterable} that strengthens that specification of {@link #iterator()} and {@link #forEach(Consumer)}.
 * <p>Note that whenever there exist a primitive consumer in {@link java.util.function} (e.g., {@link java.util.function.IntConsumer}),
 * trying to access any version of {@link #forEach(Consumer)} using a lambda expression with untyped arguments
 * will generate an ambiguous method error. This can be easily solved by specifying the type of the argument, as in
 * <pre>
 *    intIterable.forEach((int x) -&gt; { // Do something with x });
 * </pre>
 * <p>The same problem plagues, for example, {@link PrimitiveIterator.OfInt#forEachRemaining(java.util.function.IntConsumer)}.
 * <p><strong>Warning</strong>: Java will let you write &ldquo;colon&rdquo; {@code for} statements with primitive-type
 * loop variables; however, what is (unfortunately) really happening is that at each iteration an
 * unboxing (and, in the case of {@code fastutil} type-specific data structures, a boxing) will be performed. Watch out.
 *
 * @see Iterable
 */
interface IntIterable extends Iterable<Integer> {
    /**
     * Returns a type-specific iterator.
     * <p>Note that this specification strengthens the one given in {@link Iterable#iterator()}.
     *
     * @return a type-specific iterator.
     * @see Iterable#iterator()
     */
    @Override
    IntIterator iterator();

    /**
     * Performs the given action for each element of this type-specific {@link Iterable}
     * until all elements have been processed or the action throws an
     * exception.
     *
     * @param action the action to be performed for each element.
     * @see Iterable#forEach(Consumer)
     * @since 8.0.0
     */
    @SuppressWarnings("overloads")
    default void forEach(final java.util.function.IntConsumer action) {
        Objects.requireNonNull(action);
        for (final IntIterator iterator = iterator(); iterator.hasNext(); )
            action.accept(iterator.nextInt());
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated Please use the corresponding type-specific method instead.
     */
    @Deprecated
    @Override
    default void forEach(final Consumer<? super Integer> action) {
        forEach((java.util.function.IntConsumer) action::accept);
    }
}

/**
 * An abstract class providing basic methods for maps implementing a type-specific interface.
 * <p>Optional operations just throw an {@link
 * UnsupportedOperationException}. Generic versions of accessors delegate to
 * the corresponding type-specific counterparts following the interface rules
 * (they take care of returning {@code null} on a missing key).
 * <p>As a further help, this class provides a {@link BasicEntry BasicEntry} inner class
 * that implements a type-specific version of {@link Map.Entry}; it
 * is particularly useful for those classes that do not implement their own
 * entries (e.g., most immutable maps).
 */
abstract class AbstractObject2IntMap<K> extends AbstractObject2IntFunction<K> implements Object2IntMap<K>, java.io.Serializable {
    private static final long serialVersionUID = -4940583368468432370L;

    protected AbstractObject2IntMap() {
    }

    @Override
    public boolean containsValue(final int v) {
        return values().contains(v);
    }

    @Override
    public boolean containsKey(final Object k) {
        final ObjectIterator<Entry<K>> i = object2IntEntrySet().iterator();
        while (i.hasNext())
            if (i.next().getKey() == k)
                return true;
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * This class provides a basic but complete type-specific entry class for all those maps implementations
     * that do not have entries on their own (e.g., most immutable maps).
     * <p>This class does not implement {@link Map.Entry#setValue(Object) setValue()}, as the modification
     * would not be reflected in the base map.
     */
    public static class BasicEntry<K> implements Entry<K> {
        protected K key;
        protected int value;

        public BasicEntry() {
        }

        public BasicEntry(final K key, final int value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public int getIntValue() {
            return value;
        }

        @Override
        public int setValue(final int value) {
            throw new UnsupportedOperationException();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) return false;
            if (o instanceof Object2IntMap.Entry) {
                final Entry<K> e = (Entry<K>) o;
                return java.util.Objects.equals(key, e.getKey()) && ((value) == (e.getIntValue()));
            }
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
            final Object key = e.getKey();
            final Object value = e.getValue();
            if (value == null || !(value instanceof Integer))
                return false;
            return java.util.Objects.equals(this.key, (key)) && ((this.value) == (((Integer) (value)).intValue()));
        }

        @Override
        public int hashCode() {
            return ((key) == null ? 0 : (key).hashCode()) ^ (value);
        }

        @Override
        public String toString() {
            return key + "->" + value;
        }
    }

    /**
     * Returns a type-specific-set view of the keys of this map.
     * <p>The view is backed by the set returned by {@link Map#entrySet()}. Note that
     * <em>no attempt is made at caching the result of this method</em>, as this would
     * require adding some attributes that lightweight implementations would
     * not need. Subclasses may easily override this policy by calling
     * this method and caching the result, but implementors are encouraged to
     * write more efficient ad-hoc implementations.
     *
     * @return a set view of the keys of this map; it may be safely cast to a type-specific interface.
     */
    @Override
    public ObjectSet<K> keySet() {
        return new AbstractObjectSet<K>() {
            @Override
            public boolean contains(final Object k) {
                return containsKey(k);
            }

            @Override
            public int size() {
                return AbstractObject2IntMap.this.size();
            }

            @Override
            public void clear() {
                AbstractObject2IntMap.this.clear();
            }

            @Override
            public ObjectIterator<K> iterator() {
                return new ObjectIterator<K>() {
                    private final ObjectIterator<Entry<K>> i = Object2IntMaps.fastIterator(AbstractObject2IntMap.this);

                    @Override
                    public K next() {
                        return i.next().getKey();
                    }

                    ;

                    @Override
                    public boolean hasNext() {
                        return i.hasNext();
                    }

                    @Override
                    public void remove() {
                        i.remove();
                    }
                };
            }
        };
    }

    /**
     * Returns a type-specific-set view of the values of this map.
     * <p>The view is backed by the set returned by {@link Map#entrySet()}. Note that
     * <em>no attempt is made at caching the result of this method</em>, as this would
     * require adding some attributes that lightweight implementations would
     * not need. Subclasses may easily override this policy by calling
     * this method and caching the result, but implementors are encouraged to
     * write more efficient ad-hoc implementations.
     *
     * @return a set view of the values of this map; it may be safely cast to a type-specific interface.
     */
    @Override
    public IntCollection values() {
        return new AbstractIntCollection() {
            @Override
            public boolean contains(final int k) {
                return containsValue(k);
            }

            @Override
            public int size() {
                return AbstractObject2IntMap.this.size();
            }

            @Override
            public void clear() {
                AbstractObject2IntMap.this.clear();
            }

            @Override
            public IntIterator iterator() {
                return new IntIterator() {
                    private final ObjectIterator<Entry<K>> i = Object2IntMaps.fastIterator(AbstractObject2IntMap.this);

                    @Override
                    public int nextInt() {
                        return i.next().getIntValue();
                    }

                    @Override
                    public boolean hasNext() {
                        return i.hasNext();
                    }
                };
            }
        };
    }

    /** {@inheritDoc} */
    @SuppressWarnings({ "unchecked", "deprecation" })
    @Override
    public void putAll(final Map<? extends K, ? extends Integer> m) {
        if (m instanceof Object2IntMap) {
            ObjectIterator<Entry<K>> i = Object2IntMaps.fastIterator((Object2IntMap<K>) m);
            while (i.hasNext()) {
                final Entry<? extends K> e = i.next();
                put(e.getKey(), e.getIntValue());
            }
        } else {
            int n = m.size();
            final Iterator<? extends Map.Entry<? extends K, ? extends Integer>> i = m.entrySet().iterator();
            Map.Entry<? extends K, ? extends Integer> e;
            while (n-- != 0) {
                e = i.next();
                put(e.getKey(), e.getValue());
            }
        }
    }

    /**
     * Returns a hash code for this map.
     * The hash code of a map is computed by summing the hash codes of its entries.
     *
     * @return a hash code for this map.
     */
    @Override
    public int hashCode() {
        int h = 0, n = size();
        final ObjectIterator<Entry<K>> i = Object2IntMaps.fastIterator(this);
        while (n-- != 0) h += i.next().hashCode();
        return h;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Map)) return false;
        final Map<?, ?> m = (Map<?, ?>) o;
        if (m.size() != size()) return false;
        return object2IntEntrySet().containsAll(m.entrySet());
    }

    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<Entry<K>> i = Object2IntMaps.fastIterator(this);
        int n = size();
        Entry<K> e;
        boolean first = true;
        s.append("{");
        while (n-- != 0) {
            if (first) first = false;
            else s.append(", ");
            e = i.next();
            if (this == e.getKey()) s.append("(this map)");
            else
                s.append(String.valueOf(e.getKey()));
            s.append("=>");
            s.append(String.valueOf(e.getIntValue()));
        }
        s.append("}");
        return s.toString();
    }
}

/**
 * An abstract class providing basic methods for functions implementing a type-specific interface.
 * <p>This class handles directly a default return
 * value (including {@linkplain #defaultReturnValue() methods to access
 * it}). Instances of classes inheriting from this class have just to return
 * {@code defRetValue} to denote lack of a key in type-specific methods. The value
 * is serialized.
 * <p>Implementing subclasses have just to provide type-specific {@code get()},
 * type-specific {@code containsKey()}, and {@code size()} methods.
 */
abstract class AbstractObject2IntFunction<K> implements Object2IntFunction<K>, java.io.Serializable {
    private static final long serialVersionUID = -4940583368468432370L;

    protected AbstractObject2IntFunction() {
    }

    /**
     * The default return value for {@code get()}, {@code put()} and
     * {@code remove()}.
     */
    protected int defRetValue;

    @Override
    public void defaultReturnValue(final int rv) {
        defRetValue = rv;
    }

    @Override
    public int defaultReturnValue() {
        return defRetValue;
    }
}


/**
 * An abstract class providing basic methods for collections implementing a type-specific interface.
 * <p>In particular, this class provide {@link #iterator()}, {@code add()}, {@link #remove(Object)} and
 * {@link #contains(Object)} methods that just call the type-specific counterpart.
 * <p><strong>Warning</strong>: Because of a name clash between the list and collection interfaces
 * the type-specific deletion method of a type-specific abstract
 * collection is {@code rem()}, rather then {@code remove()}. A
 * subclass must thus override {@code rem()}, rather than
 * {@code remove()}, to make all inherited methods work properly.
 */
abstract class AbstractObjectCollection<K> extends AbstractCollection<K> implements ObjectCollection<K> {
    protected AbstractObjectCollection() {
    }

    @Override
    public abstract ObjectIterator<K> iterator();

    @Override
    public String toString() {
        final StringBuilder s = new StringBuilder();
        final ObjectIterator<K> i = iterator();
        int n = size();
        Object k;
        boolean first = true;
        s.append("{");
        while (n-- != 0) {
            if (first) first = false;
            else s.append(", ");
            k = i.next();
            if (this == k)
                s.append("(this collection)");
            else
                s.append(String.valueOf(k));
        }
        s.append("}");
        return s.toString();
    }
}

/**
 * An abstract class providing basic methods for sets implementing a type-specific interface.
 * <p>Note that the type-specific {@link Set} interface adds a type-specific {@code remove()}
 * method, as it is no longer harmful for subclasses. Thus, concrete subclasses of this class
 * must implement {@code remove()} (the {@code rem()} implementation of this
 * class just delegates to {@code remove()}).
 */
abstract class AbstractObjectSet<K> extends AbstractObjectCollection<K> implements Cloneable, ObjectSet<K> {
    protected AbstractObjectSet() {
    }

    @Override
    public abstract ObjectIterator<K> iterator();

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Set)) return false;
        Set<?> s = (Set<?>) o;
        if (s.size() != size()) return false;
        return containsAll(s);
    }

    /**
     * Returns a hash code for this set.
     * The hash code of a set is computed by summing the hash codes of
     * its elements.
     *
     * @return a hash code for this set.
     */
    @Override
    public int hashCode() {
        int h = 0, n = size();
        ObjectIterator<K> i = iterator();
        K k;
        while (n-- != 0) {
            k = i.next(); // We need k because KEY2JAVAHASH() is a macro with repeated evaluation.
            h += ((k) == null ? 0 : (k).hashCode());
        }
        return h;
    }
}

/**
 * A bidirectional {@link Iterator}.
 * <p>This kind of iterator is essentially a {@link ListIterator} that
 * does not support {@link ListIterator#previousIndex()} and {@link
 * ListIterator#nextIndex()}. It is useful for those maps that can easily
 * provide bidirectional iteration, but provide no index.
 * <p>Note that iterators returned by {@code fastutil} classes are more
 * specific, and support skipping. This class serves the purpose of organising
 * in a cleaner way the relationships between various iterators.
 *
 * @see Iterator
 * @see ListIterator
 */

interface BidirectionalIterator<K> extends Iterator<K> {

    /**
     * Returns the previous element from the collection.
     *
     * @return the previous element from the collection.
     * @see ListIterator#previous()
     */

    K previous();

    /**
     * Returns whether there is a previous element.
     *
     * @return whether there is a previous element.
     * @see ListIterator#hasPrevious()
     */

    boolean hasPrevious();
}


/**
 * Basic data for all hash-based classes.
 * <h2>Historical note</h2>
 * <p><strong>Warning:</strong> the following comments are here for historical reasons,
 * and apply just to the <em>double hash</em> classes that can be optionally generated.
 * The standard {@code fastutil} distribution since 6.1.0 uses linear-probing hash
 * tables, and tables are always sized as powers of two.
 * <p>The classes in {@code fastutil} are built around open-addressing hashing
 * implemented <em>via</em> double hashing. Following Knuth's suggestions in the third volume of <em>The Art of Computer
 * Programming</em>, we use for the table size a prime <var>p</var> such that
 * <var>p</var>-2 is also prime. In this way hashing is implemented with modulo <var>p</var>,
 * and secondary hashing with modulo <var>p</var>-2.
 * <p>Entries in a table can be in three states: {@link #FREE}, {@link #OCCUPIED} or {@link #REMOVED}.
 * The naive handling of removed entries requires that you search for a free entry as if they were occupied. However,
 * {@code fastutil} implements two useful optimizations, based on the following invariant:
 * <blockquote>
 * Let <var>i</var><sub>0</sub>, <var>i</var><sub>1</sub>, &hellip;, <var>i</var><sub><var>p</var>-1</sub> be
 * the permutation of the table indices induced by the key <var>k</var>, that is, <var>i</var><sub>0</sub> is the hash
 * of <var>k</var> and the following indices are obtained by adding (modulo <var>p</var>) the secondary hash plus one.
 * If there is a {@link #OCCUPIED} entry with key <var>k</var>, its index in the sequence above comes <em>before</em>
 * the indices of any {@link #REMOVED} entries with key <var>k</var>.
 * </blockquote>
 * <p>When we search for the key <var>k</var> we scan the entries in the
 * sequence <var>i</var><sub>0</sub>, <var>i</var><sub>1</sub>, &hellip;,
 * <var>i</var><sub><var>p</var>-1</sub> and stop when <var>k</var> is found,
 * when we finished the sequence or when we find a {@link #FREE} entry. Note
 * that the correctness of this procedure it is not completely trivial. Indeed,
 * when we stop at a {@link #REMOVED} entry with key <var>k</var> we must rely
 * on the invariant to be sure that no {@link #OCCUPIED} entry with the same
 * key can appear later. If we insert and remove frequently the same entries,
 * this optimization can be very effective (note, however, that when using
 * objects as keys or values deleted entries are set to a special fixed value to
 * optimize garbage collection).
 * <p>Moreover, during the probe we keep the index of the first {@link #REMOVED} entry we meet.
 * If we actually have to insert a new element, we use that
 * entry if we can, thus avoiding to pollute another {@link #FREE} entry. Since this position comes
 * <i>a fortiori</i> before any {@link #REMOVED} entries with the same key, we are also keeping the invariant true.
 */

interface Hash {

    /** The initial default size of a hash table. */
    int DEFAULT_INITIAL_SIZE = 16;
    /** The default load factor of a hash table. */
    float DEFAULT_LOAD_FACTOR = .75f;

    /**
     * A generic hash strategy.
     * one has to compare objects for equality consistently with the chosen
     * function. A <em>hash strategy</em>, thus, specifies an {@linkplain
     * #equals(Object, Object) equality method} and a {@linkplain
     * #hashCode(Object) hash function}, with the obvious property that
     * equal objects must have the same hash code.
     * <p>Note that the {@link #equals(Object, Object) equals()} method of a strategy must
     * be able to handle {@code null}, too.
     */

    /** The default growth factor of a hash table. */
    @Deprecated
    int DEFAULT_GROWTH_FACTOR = 16;
    /** The state of a free hash table entry. */
    @Deprecated
    byte FREE = 0;
    /** The state of a occupied hash table entry. */
    @Deprecated
    byte OCCUPIED = -1;
    /** The state of a hash table entry freed by a deletion. */
    @Deprecated
    byte REMOVED = 1;

    /**
     * A list of primes to be used as table sizes. The <var>i</var>-th element is
     * the largest prime <var>p</var> smaller than 2<sup>(<var>i</var>+28)/16</sup>
     * and such that <var>p</var>-2 is also prime (or 1, for the first few entries).
     */

    @Deprecated
    int PRIMES[] = { 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 5, 5, 5, 5, 5, 7, 7, 7,
            7, 7, 7, 7, 7, 7, 7, 7, 13, 13, 13, 13, 13, 13, 13, 13, 19, 19, 19, 19, 19,
            19, 19, 19, 19, 19, 19, 19, 31, 31, 31, 31, 31, 31, 31, 43, 43, 43, 43, 43,
            43, 43, 43, 61, 61, 61, 61, 61, 73, 73, 73, 73, 73, 73, 73, 103, 103, 109,
            109, 109, 109, 109, 139, 139, 151, 151, 151, 151, 181, 181, 193, 199, 199,
            199, 229, 241, 241, 241, 271, 283, 283, 313, 313, 313, 349, 349, 349, 349,
            421, 433, 463, 463, 463, 523, 523, 571, 601, 619, 661, 661, 661, 661, 661,
            823, 859, 883, 883, 883, 1021, 1063, 1093, 1153, 1153, 1231, 1321, 1321,
            1429, 1489, 1489, 1621, 1699, 1789, 1873, 1951, 2029, 2131, 2143, 2311,
            2383, 2383, 2593, 2731, 2803, 3001, 3121, 3259, 3391, 3583, 3673, 3919,
            4093, 4273, 4423, 4651, 4801, 5023, 5281, 5521, 5743, 5881, 6301, 6571,
            6871, 7129, 7489, 7759, 8089, 8539, 8863, 9283, 9721, 10141, 10531, 11071,
            11551, 12073, 12613, 13009, 13759, 14323, 14869, 15649, 16363, 17029,
            17839, 18541, 19471, 20233, 21193, 22159, 23059, 24181, 25171, 26263,
            27541, 28753, 30013, 31321, 32719, 34213, 35731, 37309, 38923, 40639,
            42463, 44281, 46309, 48313, 50461, 52711, 55051, 57529, 60091, 62299,
            65521, 68281, 71413, 74611, 77713, 81373, 84979, 88663, 92671, 96739,
            100801, 105529, 109849, 115021, 120079, 125509, 131011, 136861, 142873,
            149251, 155863, 162751, 169891, 177433, 185071, 193381, 202129, 211063,
            220021, 229981, 240349, 250969, 262111, 273643, 285841, 298411, 311713,
            325543, 339841, 355009, 370663, 386989, 404269, 422113, 440809, 460081,
            480463, 501829, 524221, 547399, 571603, 596929, 623353, 651019, 679909,
            709741, 741343, 774133, 808441, 844201, 881539, 920743, 961531, 1004119,
            1048573, 1094923, 1143283, 1193911, 1246963, 1302181, 1359733, 1420039,
            1482853, 1548541, 1616899, 1688413, 1763431, 1841293, 1922773, 2008081,
            2097133, 2189989, 2286883, 2388163, 2493853, 2604013, 2719669, 2840041,
            2965603, 3097123, 3234241, 3377191, 3526933, 3682363, 3845983, 4016041,
            4193803, 4379719, 4573873, 4776223, 4987891, 5208523, 5439223, 5680153,
            5931313, 6194191, 6468463, 6754879, 7053331, 7366069, 7692343, 8032639,
            8388451, 8759953, 9147661, 9552733, 9975193, 10417291, 10878619, 11360203,
            11863153, 12387841, 12936529, 13509343, 14107801, 14732413, 15384673,
            16065559, 16777141, 17519893, 18295633, 19105483, 19951231, 20834689,
            21757291, 22720591, 23726449, 24776953, 25873963, 27018853, 28215619,
            29464579, 30769093, 32131711, 33554011, 35039911, 36591211, 38211163,
            39903121, 41669479, 43514521, 45441199, 47452879, 49553941, 51747991,
            54039079, 56431513, 58930021, 61539091, 64263571, 67108669, 70079959,
            73182409, 76422793, 79806229, 83339383, 87029053, 90881083, 94906249,
            99108043, 103495879, 108077731, 112863013, 117860053, 123078019, 128526943,
            134217439, 140159911, 146365159, 152845393, 159612601, 166679173,
            174058849, 181765093, 189812341, 198216103, 206991601, 216156043,
            225726379, 235720159, 246156271, 257054491, 268435009, 280319203,
            292730833, 305691181, 319225021, 333358513, 348117151, 363529759,
            379624279, 396432481, 413983771, 432312511, 451452613, 471440161,
            492312523, 514109251, 536870839, 560640001, 585461743, 611382451,
            638450569, 666717199, 696235363, 727060069, 759249643, 792864871,
            827967631, 864625033, 902905501, 942880663, 984625531, 1028218189,
            1073741719, 1121280091, 1170923713, 1222764841, 1276901371, 1333434301,
            1392470281, 1454120779, 1518500173, 1585729993, 1655935399, 1729249999,
            1805811253, 1885761133, 1969251079, 2056437379, 2147482951 };
}

/** Common code for all hash-based classes. */

class HashCommon {

    protected HashCommon() {
    }

    /** 2<sup>32</sup> &middot; &phi;, &phi; = (&#x221A;5 &minus; 1)/2. */
    private static final int INT_PHI = 0x9E3779B9;

    /**
     * Quickly mixes the bits of an integer.
     * <p>This method mixes the bits of the argument by multiplying by the golden ratio and
     * xorshifting the result. It is borrowed from <a href="https://github.com/OpenHFT/Koloboke">Koloboke</a>, and
     * is slightly larger), but it's much faster.
     *
     * @param x an integer.
     * @return a hash value obtained by mixing the bits of {@code x}.
     */
    public static int mix(final int x) {
        final int h = x * INT_PHI;
        return h ^ (h >>> 16);
    }

    /**
     * Returns the least power of two greater than or equal to the specified value.
     * <p>Note that this function will return 1 when the argument is 0.
     *
     * @param x an integer smaller than or equal to 2<sup>30</sup>.
     * @return the least power of two greater than or equal to the specified value.
     */
    public static int nextPowerOfTwo(int x) {
        if (x == 0) return 1;
        x--;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        return (x | x >> 16) + 1;
    }

    /**
     * Returns the least power of two greater than or equal to the specified value.
     * <p>Note that this function will return 1 when the argument is 0.
     *
     * @param x a long integer smaller than or equal to 2<sup>62</sup>.
     * @return the least power of two greater than or equal to the specified value.
     */
    public static long nextPowerOfTwo(long x) {
        if (x == 0) return 1;
        x--;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return (x | x >> 32) + 1;
    }


    /**
     * Returns the maximum number of entries that can be filled before rehashing.
     *
     * @param n the size of the backing array.
     * @param f the load factor.
     * @return the maximum number of entries before rehashing.
     */
    public static int maxFill(final int n, final float f) {
        /* We must guarantee that there is always at least
         * one free entry (even with pathological load factors). */
        return Math.min((int) Math.ceil(n * f), n - 1);
    }

    /**
     * Returns the least power of two smaller than or equal to 2<sup>30</sup> and larger than or equal to {@code Math.ceil(expected / f)}.
     *
     * @param expected the expected number of elements in a hash table.
     * @param f the load factor.
     * @return the minimum possible size for a backing array.
     * @throws IllegalArgumentException if the necessary size is larger than 2<sup>30</sup>.
     */
    public static int arraySize(final int expected, final float f) {
        final long s = Math.max(2, nextPowerOfTwo((long) Math.ceil(expected / f)));
        if (s > (1 << 30))
            throw new IllegalArgumentException("Too large (" + expected + " expected elements with load factor " + f + ")");
        return (int) s;
    }
}

/**
 * A type-specific hash map with a fast, small-footprint implementation.
 * <p>Instances of this class use a hash table to represent a map. The table is
 * filled up to a specified <em>load factor</em>, and then doubled in size to
 * accommodate new entries. If the table is emptied below <em>one fourth</em>
 * of the load factor, it is halved in size; however, the table is never reduced to a
 * size smaller than that at creation time: this approach makes it
 * possible to create maps with a large capacity in which insertions and
 * deletions do not cause immediately rehashing. Moreover, halving is
 * not performed when deleting entries from an iterator, as it would interfere
 * with the iteration process.
 * <p>Note that {@link #clear()} does not modify the hash table size.
 * methods} lets you control the size of the table; this is particularly useful
 * if you reuse instances of this class.
 */
public class Object2IntOpenHashMap<K> extends AbstractObject2IntMap<K> implements java.io.Serializable, Cloneable, Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    /** The array of keys. */
    protected transient K[] key;
    /** The array of values. */
    protected transient int[] value;
    /** The mask for wrapping a position counter. */
    protected transient int mask;
    /** Whether this map contains the key zero. */
    protected transient boolean containsNullKey;
    /** The current table size. */
    protected transient int n;
    /** Threshold after which we rehash. It must be the table size times {@link #f}. */
    protected transient int maxFill;
    /** We never resize below this threshold, which is the construction-time {#n}. */
    protected final transient int minN;
    /** Number of entries in the set (including the key zero, if present). */
    protected int size;
    /** The acceptable load factor. */
    protected final float f;
    /** Cached set of entries. */
    protected transient FastEntrySet<K> entries;
    /** Cached set of keys. */
    protected transient ObjectSet<K> keys;
    /** Cached collection of values. */
    protected transient IntCollection values;

    /**
     * Creates a new hash map.
     * <p>The actual table size will be the least power of two greater than {@code expected}/{@code f}.
     *
     * @param expected the expected number of elements in the hash map.
     * @param f the load factor.
     */
    @SuppressWarnings("unchecked")
    public Object2IntOpenHashMap(final int expected, final float f) {
        if (f <= 0 || f > 1)
            throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1");
        if (expected < 0)
            throw new IllegalArgumentException("The expected number of elements must be nonnegative");
        this.f = f;
        minN = n = arraySize(expected, f);
        mask = n - 1;
        maxFill = maxFill(n, f);
        key = (K[]) new Object[n + 1];
        value = new int[n + 1];
    }

    /**
     * Creates a new hash map with initial expected {@link Hash#DEFAULT_INITIAL_SIZE} entries
     * and {@link Hash#DEFAULT_LOAD_FACTOR} as load factor.
     */
    public Object2IntOpenHashMap() {
        this(DEFAULT_INITIAL_SIZE, DEFAULT_LOAD_FACTOR);
    }

    private int realSize() {
        return containsNullKey ? size - 1 : size;
    }

    private void ensureCapacity(final int capacity) {
        final int needed = arraySize(capacity, f);
        if (needed > n) rehash(needed);
    }

    private void tryCapacity(final long capacity) {
        final int needed = (int) Math.min(1 << 30, Math.max(2, HashCommon.nextPowerOfTwo((long) Math.ceil(capacity / f))));
        if (needed > n) rehash(needed);
    }

    private int removeEntry(final int pos) {
        final int oldValue = value[pos];
        size--;
        shiftKeys(pos);
        if (n > minN && size < maxFill / 4 && n > DEFAULT_INITIAL_SIZE)
            rehash(n / 2);
        return oldValue;
    }

    private int removeNullEntry() {
        containsNullKey = false;
        key[n] = null;
        final int oldValue = value[n];
        size--;
        if (n > minN && size < maxFill / 4 && n > DEFAULT_INITIAL_SIZE)
            rehash(n / 2);
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends K, ? extends Integer> m) {
        if (f <= .5)
            ensureCapacity(m.size()); // The resulting map will be sized for m.size() elements
        else
            tryCapacity(size() + m.size()); // The resulting map will be tentatively sized for size() + m.size() elements
        super.putAll(m);
    }

    @SuppressWarnings("unchecked")
    private int find(final K k) {
        if (((k) == null))
            return containsNullKey ? n : -(n + 1);
        K curr;
        final K[] key = this.key;
        int pos;
        // The starting point.
        if (((curr = key[pos = (HashCommon.mix((k).hashCode())) & mask]) == null))
            return -(pos + 1);
        if (((k).equals(curr))) return pos;
        // There's always an unused entry.
        while (true) {
            if (((curr = key[pos = (pos + 1) & mask]) == null))
                return -(pos + 1);
            if (((k).equals(curr))) return pos;
        }
    }

    private void insert(final int pos, final K k, final int v) {
        if (pos == n) containsNullKey = true;
        key[pos] = k;
        value[pos] = v;
        if (size++ >= maxFill)
            rehash(arraySize(size + 1, f));
        if (ASSERTS) checkTable();
    }

    @Override
    public int put(final K k, final int v) {
        final int pos = find(k);
        if (pos < 0) {
            insert(-pos - 1, k, v);
            return defRetValue;
        }
        final int oldValue = value[pos];
        value[pos] = v;
        return oldValue;
    }

    private int addToValue(final int pos, final int incr) {
        final int oldValue = value[pos];
        value[pos] = oldValue + incr;
        return oldValue;
    }

    /**
     * Shifts left entries with the specified hash code, starting at the specified position,
     * and empties the resulting free entry.
     *
     * @param pos a starting position.
     */
    protected final void shiftKeys(int pos) {
        // Shift entries with the same hash.
        int last, slot;
        K curr;
        final K[] key = this.key;
        for (; ; ) {
            pos = ((last = pos) + 1) & mask;
            for (; ; ) {
                if (((curr = key[pos]) == null)) {
                    key[last] = (null);
                    return;
                }
                slot = (HashCommon.mix((curr).hashCode())) & mask;
                if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos)
                    break;
                pos = (pos + 1) & mask;
            }
            key[last] = curr;
            value[last] = value[pos];
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public int removeInt(final Object k) {
        if ((((K) k) == null)) {
            if (containsNullKey) return removeNullEntry();
            return defRetValue;
        }
        K curr;
        final K[] key = this.key;
        int pos;
        // The starting point.
        if (((curr = key[pos = (HashCommon.mix((k).hashCode())) & mask]) == null))
            return defRetValue;
        if (((k).equals(curr))) return removeEntry(pos);
        while (true) {
            if (((curr = key[pos = (pos + 1) & mask]) == null))
                return defRetValue;
            if (((k).equals(curr))) return removeEntry(pos);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public int getInt(final Object k) {
        if ((((K) k) == null))
            return containsNullKey ? value[n] : defRetValue;
        K curr;
        final K[] key = this.key;
        int pos;
        // The starting point.
        if (((curr = key[pos = (HashCommon.mix((k).hashCode())) & mask]) == null))
            return defRetValue;
        if (((k).equals(curr))) return value[pos];
        // There's always an unused entry.
        while (true) {
            if (((curr = key[pos = (pos + 1) & mask]) == null))
                return defRetValue;
            if (((k).equals(curr))) return value[pos];
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean containsKey(final Object k) {
        if ((((K) k) == null)) return containsNullKey;
        K curr;
        final K[] key = this.key;
        int pos;
        // The starting point.
        if (((curr = key[pos = (HashCommon.mix((k).hashCode())) & mask]) == null))
            return false;
        if (((k).equals(curr))) return true;
        // There's always an unused entry.
        while (true) {
            if (((curr = key[pos = (pos + 1) & mask]) == null))
                return false;
            if (((k).equals(curr))) return true;
        }
    }

    @Override
    public boolean containsValue(final int v) {
        final int value[] = this.value;
        final K key[] = this.key;
        if (containsNullKey && ((value[n]) == (v)))
            return true;
        for (int i = n; i-- != 0; )
            if (!((key[i]) == null) && ((value[i]) == (v)))
                return true;
        return false;
    }

    /* Removes all elements from this map.
     *
     * <p>To increase object reuse, this method does not change the table size.
     * If you want to reduce the table size, you must use {@link #trim()}.
     *
     */
    @Override
    public void clear() {
        if (size == 0) return;
        size = 0;
        containsNullKey = false;
        java.util.Arrays.fill(key, (null));
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * The entry class for a hash map does not record key and value, but
     * rather the position in the hash table of the corresponding entry. This
     * is necessary so that calls to {@link Map.Entry#setValue(Object)} are reflected in
     * the map
     */
    final class MapEntry implements Object2IntMap.Entry<K>, Map.Entry<K, Integer> {
        // The table index this entry refers to, or -1 if this entry has been deleted.
        int index;

        MapEntry(final int index) {
            this.index = index;
        }

        MapEntry() {
        }

        @Override
        public K getKey() {
            return key[index];
        }

        @Override
        public int getIntValue() {
            return value[index];
        }

        @Override
        public int setValue(final int v) {
            final int oldValue = value[index];
            value[index] = v;
            return oldValue;
        }

        /**
         * {@inheritDoc}
         *
         * @deprecated Please use the corresponding type-specific method instead.
         */
        @Deprecated
        @Override
        public Integer getValue() {
            return Integer.valueOf(value[index]);
        }

        /**
         * {@inheritDoc}
         *
         * @deprecated Please use the corresponding type-specific method instead.
         */
        @Deprecated
        @Override
        public Integer setValue(final Integer v) {
            return Integer.valueOf(setValue((v).intValue()));
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean equals(final Object o) {
            if (!(o instanceof Map.Entry)) return false;
            Map.Entry<K, Integer> e = (Map.Entry<K, Integer>) o;
            return java.util.Objects.equals(key[index], (e.getKey())) && ((value[index]) == ((e.getValue()).intValue()));
        }

        @Override
        public int hashCode() {
            return ((key[index]) == null ? 0 : (key[index]).hashCode()) ^ (value[index]);
        }

        @Override
        public String toString() {
            return key[index] + "=>" + value[index];
        }
    }

    /** An iterator over a hash map. */
    private class MapIterator {
        /**
         * The index of the last entry returned, if positive or zero; initially, {@link #n}. If negative, the last
         * entry returned was that of the key of index {@code - pos - 1} from the {@link #wrapped} list.
         */
        int pos = n;
        /**
         * The index of the last entry that has been returned (more precisely, the value of {@link #pos} if {@link #pos} is positive,
         * or {@link Integer#MIN_VALUE} if {@link #pos} is negative). It is -1 if either
         * we did not return an entry yet, or the last returned entry has been removed.
         */
        int last = -1;
        /** A downward counter measuring how many entries must still be returned. */
        int c = size;
        /** A boolean telling us whether we should return the entry with the null key. */
        boolean mustReturnNullKey = Object2IntOpenHashMap.this.containsNullKey;
        /** A lazily allocated list containing keys of entries that have wrapped around the table because of removals. */
        ObjectArrayList<K> wrapped;

        public boolean hasNext() {
            return c != 0;
        }

        public int nextEntry() {
            if (!hasNext())
                throw new NoSuchElementException();
            c--;
            if (mustReturnNullKey) {
                mustReturnNullKey = false;
                return last = n;
            }
            final K key[] = Object2IntOpenHashMap.this.key;
            for (; ; ) {
                if (--pos < 0) {
                    // We are just enumerating elements from the wrapped list.
                    last = Integer.MIN_VALUE;
                    final K k = wrapped.get(-pos - 1);
                    int p = (HashCommon.mix((k).hashCode())) & mask;
                    while (!((k).equals(key[p])))
                        p = (p + 1) & mask;
                    return p;
                }
                if (!((key[pos]) == null))
                    return last = pos;
            }
        }

        /**
         * Shifts left entries with the specified hash code, starting at the specified position,
         * and empties the resulting free entry.
         *
         * @param pos a starting position.
         */
        private void shiftKeys(int pos) {
            // Shift entries with the same hash.
            int last, slot;
            K curr;
            final K[] key = Object2IntOpenHashMap.this.key;
            for (; ; ) {
                pos = ((last = pos) + 1) & mask;
                for (; ; ) {
                    if (((curr = key[pos]) == null)) {
                        key[last] = (null);
                        return;
                    }
                    slot = (HashCommon.mix((curr).hashCode())) & mask;
                    if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos)
                        break;
                    pos = (pos + 1) & mask;
                }
                if (pos < last) { // Wrapped entry.
                    if (wrapped == null)
                        wrapped = new ObjectArrayList<>(2);
                    wrapped.add(key[pos]);
                }
                key[last] = curr;
                value[last] = value[pos];
            }
        }

        public void remove() {
            if (last == -1)
                throw new IllegalStateException();
            if (last == n) {
                containsNullKey = false;
                key[n] = null;
            } else if (pos >= 0) shiftKeys(last);
            else {
                // We're removing wrapped entries.
                Object2IntOpenHashMap.this.removeInt(wrapped.set(-pos - 1, null));
                last = -1; // Note that we must not decrement size
                return;
            }
            size--;
            last = -1; // You can no longer remove this entry.
            if (ASSERTS) checkTable();
        }

        public int skip(final int n) {
            int i = n;
            while (i-- != 0 && hasNext()) nextEntry();
            return n - i - 1;
        }
    }

    private class EntryIterator extends MapIterator implements ObjectIterator<Object2IntMap.Entry<K>> {
        private MapEntry entry;

        @Override
        public MapEntry next() {
            return entry = new MapEntry(nextEntry());
        }

        @Override
        public void remove() {
            super.remove();
            entry.index = -1; // You cannot use a deleted entry.
        }
    }

    private class FastEntryIterator extends MapIterator implements ObjectIterator<Object2IntMap.Entry<K>> {
        private final MapEntry entry = new MapEntry();

        @Override
        public MapEntry next() {
            entry.index = nextEntry();
            return entry;
        }
    }

    private final class MapEntrySet extends AbstractObjectSet<Object2IntMap.Entry<K>> implements FastEntrySet<K> {
        @Override
        public ObjectIterator<Object2IntMap.Entry<K>> iterator() {
            return new EntryIterator();
        }

        @Override
        public ObjectIterator<Object2IntMap.Entry<K>> fastIterator() {
            return new FastEntryIterator();
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) return false;
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
            if (e.getValue() == null || !(e.getValue() instanceof Integer))
                return false;
            final K k = ((K) e.getKey());
            final int v = ((Integer) (e.getValue())).intValue();
            if (((k) == null))
                return Object2IntOpenHashMap.this.containsNullKey && ((value[n]) == (v));
            K curr;
            final K[] key = Object2IntOpenHashMap.this.key;
            int pos;
            // The starting point.
            if (((curr = key[pos = (HashCommon.mix((k).hashCode())) & mask]) == null))
                return false;
            if (((k).equals(curr)))
                return ((value[pos]) == (v));
            // There's always an unused entry.
            while (true) {
                if (((curr = key[pos = (pos + 1) & mask]) == null))
                    return false;
                if (((k).equals(curr)))
                    return ((value[pos]) == (v));
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean remove(final Object o) {
            if (!(o instanceof Map.Entry)) return false;
            final Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
            if (e.getValue() == null || !(e.getValue() instanceof Integer))
                return false;
            final K k = ((K) e.getKey());
            final int v = ((Integer) (e.getValue())).intValue();
            if (((k) == null)) {
                if (containsNullKey && ((value[n]) == (v))) {
                    removeNullEntry();
                    return true;
                }
                return false;
            }
            K curr;
            final K[] key = Object2IntOpenHashMap.this.key;
            int pos;
            // The starting point.
            if (((curr = key[pos = (HashCommon.mix((k).hashCode())) & mask]) == null))
                return false;
            if (((curr).equals(k))) {
                if (((value[pos]) == (v))) {
                    removeEntry(pos);
                    return true;
                }
                return false;
            }
            while (true) {
                if (((curr = key[pos = (pos + 1) & mask]) == null))
                    return false;
                if (((curr).equals(k))) {
                    if (((value[pos]) == (v))) {
                        removeEntry(pos);
                        return true;
                    }
                }
            }
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public void clear() {
            Object2IntOpenHashMap.this.clear();
        }

        /** {@inheritDoc} */
        @Override
        public void forEach(final Consumer<? super Entry<K>> consumer) {
            if (containsNullKey)
                consumer.accept(new AbstractObject2IntMap.BasicEntry<K>(key[n], value[n]));
            for (int pos = n; pos-- != 0; )
                if (!((key[pos]) == null))
                    consumer.accept(new AbstractObject2IntMap.BasicEntry<K>(key[pos], value[pos]));
        }

        /** {@inheritDoc} */
        @Override
        public void fastForEach(final Consumer<? super Entry<K>> consumer) {
            final AbstractObject2IntMap.BasicEntry<K> entry = new AbstractObject2IntMap.BasicEntry<>();
            if (containsNullKey) {
                entry.key = key[n];
                entry.value = value[n];
                consumer.accept(entry);
            }
            for (int pos = n; pos-- != 0; )
                if (!((key[pos]) == null)) {
                    entry.key = key[pos];
                    entry.value = value[pos];
                    consumer.accept(entry);
                }
        }
    }

    @Override
    public FastEntrySet<K> object2IntEntrySet() {
        if (entries == null) entries = new MapEntrySet();
        return entries;
    }

    /**
     * An iterator on keys.
     * <p>We simply override the {@link java.util.ListIterator#next()}/{@link java.util.ListIterator#previous()} methods
     * (and possibly their type-specific counterparts) so that they return keys
     * instead of entries.
     */
    private final class KeyIterator extends MapIterator implements ObjectIterator<K> {
        public KeyIterator() {
        }

        @Override
        public K next() {
            return key[nextEntry()];
        }
    }

    private final class KeySet extends AbstractObjectSet<K> {
        @Override
        public ObjectIterator<K> iterator() {
            return new KeyIterator();
        }

        /** {@inheritDoc} */
        @Override
        public void forEach(final Consumer<? super K> consumer) {
            if (containsNullKey) consumer.accept(key[n]);
            for (int pos = n; pos-- != 0; ) {
                final K k = key[pos];
                if (!((k) == null)) consumer.accept(k);
            }
        }

        @Override
        public int size() {
            return size;
        }

        @Override
        public boolean contains(Object k) {
            return containsKey(k);
        }

        @Override
        public boolean remove(Object k) {
            final int oldSize = size;
            Object2IntOpenHashMap.this.removeInt(k);
            return size != oldSize;
        }

        @Override
        public void clear() {
            Object2IntOpenHashMap.this.clear();
        }
    }

    @Override
    public ObjectSet<K> keySet() {
        if (keys == null) keys = new KeySet();
        return keys;
    }

    /**
     * An iterator on values.
     * <p>We simply override the {@link java.util.ListIterator#next()}/{@link java.util.ListIterator#previous()} methods
     * (and possibly their type-specific counterparts) so that they return values
     * instead of entries.
     */
    private final class ValueIterator extends MapIterator implements IntIterator {
        public ValueIterator() {
        }

        @Override
        public int nextInt() {
            return value[nextEntry()];
        }
    }

    @Override
    public IntCollection values() {
        if (values == null)
            values = new AbstractIntCollection() {
                @Override
                public IntIterator iterator() {
                    return new ValueIterator();
                }

                @Override
                public int size() {
                    return size;
                }

                @Override
                public boolean contains(int v) {
                    return containsValue(v);
                }

                @Override
                public void clear() {
                    Object2IntOpenHashMap.this.clear();
                }

                /** {@inheritDoc} */
                @Override
                public void forEach(final java.util.function.IntConsumer consumer) {
                    if (containsNullKey)
                        consumer.accept(value[n]);
                    for (int pos = n; pos-- != 0; )
                        if (!((key[pos]) == null))
                            consumer.accept(value[pos]);
                }
            };
        return values;
    }

    /**
     * Rehashes this map if the table is too large.
     * <p>Let <var>N</var> be the smallest table size that can hold
     * <code>max(n,{@link #size()})</code> entries, still satisfying the load factor. If the current
     * table size is smaller than or equal to <var>N</var>, this method does
     * nothing. Otherwise, it rehashes this map in a table of size
     * <var>N</var>.
     * <p>This method is useful when reusing maps.  {@linkplain #clear() Clearing a
     * map} leaves the table size untouched. If you are reusing a map
     * many times, you can call this method with a typical
     * size to avoid keeping around a very large table just
     * because of a few large transient maps.
     *
     * @param n the threshold for the trimming.
     * @return true if there was enough memory to trim the map.
     */
    public boolean trim(final int n) {
        final int l = HashCommon.nextPowerOfTwo((int) Math.ceil(n / f));
        if (l >= n || size > maxFill(l, f)) return true;
        try {
            rehash(l);
        } catch (OutOfMemoryError cantDoIt) {
            return false;
        }
        return true;
    }

    /**
     * Rehashes the map.
     * <p>This method implements the basic rehashing strategy, and may be
     * overridden by subclasses implementing different rehashing strategies (e.g.,
     * disk-based rehashing). However, you should not override this method
     * unless you understand the internal workings of this class.
     *
     * @param newN the new size
     */
    @SuppressWarnings("unchecked")
    protected void rehash(final int newN) {
        final K key[] = this.key;
        final int value[] = this.value;
        final int mask = newN - 1; // Note that this is used by the hashing macro
        final K newKey[] = (K[]) new Object[newN + 1];
        final int newValue[] = new int[newN + 1];
        int i = n, pos;
        for (int j = realSize(); j-- != 0; ) {
            while (((key[--i]) == null)) ;
            if (!((newKey[pos = (HashCommon.mix((key[i]).hashCode())) & mask]) == null))
                while (!((newKey[pos = (pos + 1) & mask]) == null))
                    ;
            newKey[pos] = key[i];
            newValue[pos] = value[i];
        }
        newValue[newN] = value[n];
        n = newN;
        this.mask = mask;
        maxFill = maxFill(n, f);
        this.key = newKey;
        this.value = newValue;
    }

    /**
     * Returns a deep copy of this map.
     * <p>This method performs a deep copy of this hash map; the data stored in the
     * map, however, is not cloned. Note that this makes a difference only for object keys.
     *
     * @return a deep copy of this map.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object2IntOpenHashMap<K> clone() {
        Object2IntOpenHashMap<K> c;
        try {
            c = (Object2IntOpenHashMap<K>) super.clone();
        } catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.keys = null;
        c.values = null;
        c.entries = null;
        c.containsNullKey = containsNullKey;
        c.key = key.clone();
        c.value = value.clone();
        return c;
    }

    /**
     * Returns a hash code for this map.
     * This method overrides the generic method provided by the superclass.
     * Since {@code equals()} is not overriden, it is important
     * that the value returned by this method is the same value as
     * the one returned by the overriden method.
     *
     * @return a hash code for this map.
     */
    @Override
    public int hashCode() {
        int h = 0;
        for (int j = realSize(), i = 0, t = 0; j-- != 0; ) {
            while (((key[i]) == null)) i++;
            if (this != key[i])
                t = ((key[i]).hashCode());
            t ^= (value[i]);
            h += t;
            i++;
        }
        // Zero / null keys have hash zero.
        if (containsNullKey) h += (value[n]);
        return h;
    }

    private void checkTable() {
    }
}