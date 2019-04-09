package com.got.genealogy.core.graph.property;

public class Edge {
    private String label;

    public Edge() {
    }

    public Edge(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
