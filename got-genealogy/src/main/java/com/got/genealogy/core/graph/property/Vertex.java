package com.got.genealogy.core.graph.property;

/**
 * Vertex object, used as a node in a Graph
 * object.
 */
public class Vertex implements Comparable<Vertex> {

    private String label;

    private boolean visited;

    /**
     * Constructor, forcing the specification
     * of a label, used to identify the Vertex.
     *
     * @param label of the Vertex.
     */
    public Vertex(String label) {
        this.label = label;
        this.visited = false;
    }

    /**
     * Returns the label of the Vertex.
     *
     * @return the label of the Vertex.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Replaces the label of the Vertex.
     *
     * @param label new label.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Returns <tt>true</tt> if the Vertex was
     * visited, during traversal.
     *
     * @return <tt>true</tt> if the Vertex was
     * vistied, during traversal.
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * Replaces the boolean value of the visited
     * variable.
     *
     * @param visited new boolean value for the
     *                visited variable.
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * Compares another vertex object, using their
     * label. Sorts in ascending order,
     * alphabetically.
     *
     * @param vertex Vertex object to compare this
     *               object's label to.
     * @return the recursive result, after comparing
     * the two Vertex objects.
     */
    @Override
    public int compareTo(Vertex vertex) {
        String label = vertex.getLabel();
        return getLabel().compareTo(label);
    }
}
