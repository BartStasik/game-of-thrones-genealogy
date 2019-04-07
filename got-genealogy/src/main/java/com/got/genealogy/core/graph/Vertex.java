package com.got.genealogy.core.graph;

import java.util.Objects;

public class Vertex {
    private boolean visited;
    private String label;

    public Vertex(String label) {
        this.label = label;
        this.visited = false;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }
}
