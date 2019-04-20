package com.got.genealogy.core.graph.property;

import java.util.Map;

public class WeightedVertex<K extends Vertex, V extends Edge> implements Map.Entry<K, V> {

    private final K key;
    private V value;

    public WeightedVertex(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V oldValue = this.value;
        this.value = value;
        return oldValue;
    }
}
