package com.got.genealogy.core.graph.collection;

import com.got.genealogy.core.graph.property.Edge;
import com.got.genealogy.core.graph.property.Vertex;
import com.got.genealogy.core.graph.property.WeightedVertex;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AdjacencyList<V extends Vertex, E extends Edge> {

    private Map<V, Set<WeightedVertex<V, E>>> list;

    public AdjacencyList() {
        list = new HashMap<>();
    }

    public Map<V, Set<WeightedVertex<V, E>>> getList() {
        return list;
    }

    public void setList(Map<V, Set<WeightedVertex<V, E>>> list) {
        this.list = list;
    }

    public Set<WeightedVertex<V, E>> get(V keyVertex) {
        return list.get(keyVertex);
    }

    public void put(V vertex, Set<WeightedVertex<V, E>> adjacentVertices) {
        list.put(vertex, adjacentVertices);
    }

    public WeightedVertex<V, E> getWeightedVertex(V keyVertex, V adjacentVertex) {
        for (WeightedVertex<V, E> vertex : this.list.get(keyVertex)) {
            if (vertex.getKey().equals(adjacentVertex)) {
                return vertex;
            }
        }
        return null;
    }

    public void setWeightedVertex(V keyVertex, V adjacentVertex, E edge) {
        WeightedVertex<V, E> weightedVertex = getWeightedVertex(keyVertex, adjacentVertex);
        if (weightedVertex != null) {
            list.get(keyVertex)
                    .remove(weightedVertex);
            addWeightedVertex(keyVertex, adjacentVertex, edge);
        }
    }

    public void addWeightedVertex(V keyVertex, V adjacentVertex, E edge) {
        list.get(keyVertex).add(
                new WeightedVertex<>(adjacentVertex, edge));
    }

    public void removeWeightedVertex(V keyVertex, V adjacentVertex) {
        WeightedVertex<V, E> weightedVertex = getWeightedVertex(keyVertex, adjacentVertex);
        if (weightedVertex != null) {
            list.get(keyVertex)
                    .remove(weightedVertex);
        }
    }
}