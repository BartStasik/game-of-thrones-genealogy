package com.got.genealogy.core.graph.collection;

import com.got.genealogy.core.graph.property.Edge;
import com.got.genealogy.core.graph.property.Vertex;
import com.got.genealogy.core.graph.property.WeightedVertex;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AdjacencyList<Vert extends Vertex, Arc extends Edge> {

    private Map<Vert, Set<WeightedVertex<Vert, Arc>>> list;

    public AdjacencyList() {
        list = new HashMap<>();
    }

    public Map<Vert, Set<WeightedVertex<Vert, Arc>>> getList() {
        return list;
    }

    public void setList(Map<Vert, Set<WeightedVertex<Vert, Arc>>> list) {
        this.list = list;
    }

    public Set<WeightedVertex<Vert, Arc>> get(Vert keyVertex) {
        return list.get(keyVertex);
    }

    public void put(Vert vertex, Set<WeightedVertex<Vert, Arc>> adjacentVertices) {
        list.put(vertex, adjacentVertices);
    }

    public WeightedVertex<Vert, Arc> getWeightedVertex(Vert keyVertex, Vert adjacentVertex) {
        for (WeightedVertex<Vert, Arc> vertex : this.list.get(keyVertex)) {
            if (vertex.getKey().equals(adjacentVertex)) {
                return vertex;
            }
        }
        return null;
    }

    public void setWeightedVertex(Vert keyVertex, Vert adjacentVertex, Arc edge) {
        WeightedVertex<Vert, Arc> weightedVertex = getWeightedVertex(keyVertex, adjacentVertex);
        if (weightedVertex != null) {
            removeWeightedVertex(keyVertex, adjacentVertex);
            addWeightedVertex(keyVertex, adjacentVertex, edge);
        }
    }

    public void addWeightedVertex(Vert keyVertex, Vert adjacentVertex, Arc edge) {
        list.get(keyVertex).add(
                new WeightedVertex<>(adjacentVertex, edge));
    }

    public void removeWeightedVertex(Vert keyVertex, Vert adjacentVertex) {
        WeightedVertex<Vert, Arc> weightedVertex = getWeightedVertex(keyVertex, adjacentVertex);
        if (weightedVertex != null) {
            list.get(keyVertex)
                    .remove(weightedVertex);
        }
    }
}