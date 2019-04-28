package com.got.genealogy.core.graph.property;

/**
 * Wrapper class for Edge objects. Used directly
 * in a Graph, to differentiate between a null
 * edge and an existing one, with a null weight
 * value.
 *
 * @param <T> is of type of Edge.
 */
public class Weight<T extends Edge> {

    private T weight;

    /**
     * Empty constructor. Used to specify an
     * edge between two vertices in a Graph,
     * but without an actual weight.
     */
    public Weight() {
    }

    /**
     * Constructor, used to give the edge
     * between two vertices a value of type
     * Edge.
     *
     * @param weight new Edge value.
     */
    public Weight(T weight) {
        this.weight = weight;
    }

    /**
     * Returns the Edge this class is wrapping.
     *
     * @return the Edge this class is wrapping.
     */
    public T getWeight() {
        return weight;
    }

    /**
     * Replaces the Edge this class is wrapping.
     *
     * @param weight new weight of type Edge.
     */
    public void setWeight(T weight) {
        this.weight = weight;
    }
}
