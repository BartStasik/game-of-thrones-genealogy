package com.got.genealogy.core.graph.property;

public class Weight<T> {

    private T weight;

    public Weight() {
    }

    public Weight(T weight) {
        this.weight = weight;
    }

    public T getWeight() {
        return weight;
    }

    public void setWeight(T weight) {
        this.weight = weight;
    }
}
