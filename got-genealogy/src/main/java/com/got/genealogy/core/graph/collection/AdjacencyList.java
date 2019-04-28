package com.got.genealogy.core.graph.collection;

import com.got.genealogy.core.graph.property.Edge;
import com.got.genealogy.core.graph.property.Vertex;
import com.got.genealogy.core.graph.property.WeightedVertex;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * AdjacencyList object, mainly used for exporting
 * and converting from other collections, rather
 * than actual graph storage.
 *
 * @param <V> of type Vertex.
 * @param <E> of type Edge.
 */
public class AdjacencyList<V extends Vertex, E extends Edge> {

    private Map<V, Set<WeightedVertex<V, E>>> list;

    /**
     * Constructor, only used for list initialisation.
     */
    public AdjacencyList() {
        list = new HashMap<>();
    }

    /**
     * Returns the adjacency list map object.
     * <p>
     * Every WeightedVertex is used to map the weight
     * of the edge between the key Vertex and every
     * Vertex object in the set.
     *
     * @return map of vertices, pointing to sets of
     * weighted vertices.
     */
    public Map<V, Set<WeightedVertex<V, E>>> getList() {
        return list;
    }

    /**
     * Replace the adjacency list map object.
     * <p>
     * Every WeightedVertex is used to map the weight
     * of the edge between the key Vertex and every
     * Vertex object in the set.
     *
     * @param list new map of vertices, pointing to
     *             sets of weighted vertices.
     */
    public void setList(Map<V, Set<WeightedVertex<V, E>>> list) {
        this.list = list;
    }

    /**
     * Gets a set of vertices, adjacent to the key
     * Vertex in the adjacency list.
     *
     * @param keyVertex is the key in the adjacency
     *                  list map.
     * @return map of vertices, pointing to sets of
     * weighted vertices.
     */
    public Set<WeightedVertex<V, E>> get(V keyVertex) {
        return list.get(keyVertex);
    }

    /**
     * Replace the set under a specific key.
     *
     * @param vertex           is the key in the
     *                         adjacency list map.
     * @param adjacentVertices is the set of weighted
     *                         vertices to the key
     *                         vertex.
     */
    public void put(V vertex, Set<WeightedVertex<V, E>> adjacentVertices) {
        list.put(vertex, adjacentVertices);
    }

    /**
     * Gets the WeightedVertex object between two
     * vertices in the adjacency list.
     *
     * @param keyVertex      is the key in the
     *                       adjacency list map.
     * @param adjacentVertex is the vertex, adjacent
     *                       to the key.
     * @return the Vertex object, paired with the
     * weight between keyVertex and adjacentVertex.
     */
    public WeightedVertex<V, E> getWeightedVertex(V keyVertex, V adjacentVertex) {
        for (WeightedVertex<V, E> vertex : this.list.get(keyVertex)) {
            if (vertex.getKey().equals(adjacentVertex)) {
                return vertex;
            }
        }
        return null;
    }

    /**
     * Replaces the WeightedVertex object in the set
     * of vertices, adjacent to keyVertex.
     *
     * @param keyVertex      is the key in the
     *                       adjacency list map.
     * @param adjacentVertex is the vertex, adjacent
     *                       to the key.
     * @param edge           is the Edge between the
     *                       two vertices.
     */
    public void setWeightedVertex(V keyVertex, V adjacentVertex, E edge) {
        WeightedVertex<V, E> weightedVertex = getWeightedVertex(keyVertex, adjacentVertex);
        if (weightedVertex != null) {
            list.get(keyVertex)
                    .remove(weightedVertex);
            addWeightedVertex(keyVertex, adjacentVertex, edge);
        }
    }

    /**
     * Adds a new WeightedVertex object in the set
     * of vertices adjacent to keyVertex.
     *
     * @param keyVertex      is the key in the
     *                       adjacency list map.
     * @param adjacentVertex is the vertex, adjacent
     *                       to the key.
     * @param edge           is the Edge between the
     *                       two vertices.
     */
    public void addWeightedVertex(V keyVertex, V adjacentVertex, E edge) {
        list.get(keyVertex).add(
                new WeightedVertex<>(adjacentVertex, edge));
    }

    /**
     * Removes an existing WeightedVertex object in
     * the set of vertices adjacent to keyVertex.
     *
     * @param keyVertex      is the key in the
     *                       adjacency list map.
     * @param adjacentVertex is the vertex, adjacent
     *                       to the key.
     */
    public void removeWeightedVertex(V keyVertex, V adjacentVertex) {
        WeightedVertex<V, E> weightedVertex = getWeightedVertex(keyVertex, adjacentVertex);
        if (weightedVertex != null) {
            list.get(keyVertex)
                    .remove(weightedVertex);
        }
    }
}