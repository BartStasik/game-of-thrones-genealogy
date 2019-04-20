package com.got.genealogy.core.family.person;

import com.got.genealogy.core.graph.property.Edge;

public class Relation extends Edge {

    public Relation(String label) {
        super(label);
    }

    public Relation(Relationship label) {
        this(label.toString());
    }
}
