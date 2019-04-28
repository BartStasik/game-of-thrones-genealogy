package com.got.genealogy.core.graph.property;

import java.util.Map;

/**
 * Entry, where the key is a Vertex and the
 * value is an Edge.
 *
 * This is directly used by AdjacencyList,
 * where any Vertex, adjacent to another, has
 * an Edge.
 *
 * @param <K> of type Vertex.
 * @param <V> of type Edge.
 */
public class WeightedVertex<K extends Vertex, V extends Edge> implements Map.Entry<K, V> {

    private final K key;
    private V value;

    /**
     * Constructor, initialising the key and
     * value of this Entry.
     *
     * @param key of type Vertex.
     * @param value of type Edge.
     */
    public WeightedVertex(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Returns the key of the Entry.
     *
     * @return the key of the Entry.
     */
    @Override
    public K getKey() {
        return key;
    }

    /**
     * Returns the value of the Entry.
     *
     * @return the value of the Entry.
     */
    @Override
    public V getValue() {
        return value;
    }

    /**
     * Replaces the value of the Entry
     * and returns the old one.
     *
     * @param value the new Entry value.
     * @return the old Entry value.
     */
    @Override
    public V setValue(V value) {
        V oldValue = this.value;
        this.value = value;
        return oldValue;
    }
}
