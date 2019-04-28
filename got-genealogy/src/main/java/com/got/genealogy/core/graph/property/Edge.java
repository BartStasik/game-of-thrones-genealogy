package com.got.genealogy.core.graph.property;

/**
 * Edge object, used as a connection between
 * two Vertex objects, in a Graph object.
 */
public class Edge {

    private String label;

    /**
     * Constructor, forcing the specification
     * of the label, used to identify the
     * Edge.
     *
     * @param label of the edge.
     */
    public Edge(String label) {
        this.label = label;
    }

    /**
     * Gets the label of the Edge.
     *
     * @return the label of the Edge.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Replaces the label of the Edge.
     *
     * @param label new label.
     */
    public void setLabel(String label) {
        this.label = label;
    }
}
