package com.got.genealogy.core.family;

import com.got.genealogy.core.graph.property.Edge;

public class Relation extends Edge {

    private String label;

    public Relation() {
        super("");
    }

    public Relation(String label) {
        super(label);
    }
}
