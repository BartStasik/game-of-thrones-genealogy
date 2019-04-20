package com.got.genealogy.core.graph.property;

public class Vertex implements Comparable<Vertex> {

    private String annotation;
    private String label;

    private boolean visited;

    public Vertex(String label) {
        this.label = label;
        this.visited = false;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
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

    @Override
    public int compareTo(Vertex vertex) {
        String label = vertex.getLabel();
        return getLabel().compareTo(label);
    }
}
