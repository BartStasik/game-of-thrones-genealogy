package com.got.genealogy.core.family.person;

import com.got.genealogy.core.graph.property.Edge;

import java.util.HashSet;
import java.util.Set;

public class Relation extends Edge {

    private Set<String> extras = new HashSet<>();

    public Relation(String label) {
        super("");
        extras.add(label.toUpperCase());
    }

    public Relation(Relationship label) {
        super(label.toString());
    }

    public String getTopExtra() {
        return extras.stream()
                .findFirst()
                .orElse(null);
    }

    public Set<String> getExtras() {
        return extras;
    }

    public void addExtra(String extra) {
        extras.add(extra.toUpperCase());
    }
}
