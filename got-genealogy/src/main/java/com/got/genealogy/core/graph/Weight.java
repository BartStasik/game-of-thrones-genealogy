package com.got.genealogy.core.graph;

import java.util.Arrays;

public class Weight<T> {
    private T weight;

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
