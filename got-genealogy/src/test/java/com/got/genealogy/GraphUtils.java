package com.got.genealogy;

import com.got.genealogy.core.graph.Graph;
import com.got.genealogy.core.graph.collection.AdjacencyList;
import com.got.genealogy.core.graph.collection.AdjacencyMatrix;
import com.got.genealogy.core.graph.property.Edge;
import com.got.genealogy.core.graph.property.Vertex;
import com.got.genealogy.core.graph.property.Weight;

import java.util.Map;
import java.util.function.Function;

class GraphUtils<V extends Vertex, E extends Edge> {
    void printList(AdjacencyList<V, E> list) {
        list.getList()
                .forEach((k, v) -> {
                    // Set<WeightedVertex>
                    System.out.print(k.getLabel() + " : { ");
                    v.forEach((e) -> {
                        // WeightedVertex
                        System.out.printf("[%s:%s], ",
                                e.getKey().getLabel(),
                                e.getValue().getLabel());
                    });
                    System.out.println(" }");
                });
    }

    void printGraph(Graph<V, E> graph) {
        // This will be moved out of Graph.
        // Currently, only being used for
        // testing.
        printGraph(graph, null);
    }

    void printGraph(Graph<V, E> graph, Edge nullValue) {
        // Trying to find a way to customise
        // return method, i.e. getLabel().
        printGraph(graph, nullValue, arc -> {
            if (arc == null) {
                return arc;
            } else {
                return arc.getLabel();
            }
        });
    }

    void printGraph(Graph<V, E> graph,
                    Edge nullValue,
                    Function<Edge, Object> function) {
        AdjacencyMatrix<Weight<E>> matrix = graph.getAdjacencyMatrix();
        Map<V, Integer> vertices = graph.getVertices();

        int size = matrix.size();
        // Print column labels
        System.out.println();
        System.out.print("   ");
        for (int i = 0; i < size; i++) {
            for (Map.Entry<V, Integer> vertex : vertices.entrySet()) {
                // TODO: Replace with LinkedHashMap
                if (i == vertex.getValue()) {
                    System.out.print(vertex.getKey().getLabel() + "  ");
                }
            }
        }
        // Print matrix
        System.out.println();
        for (int i = 0; i < size; i++) {
            // HashMap isn't ordered, so
            // get correct vertex label
            // and print with space.
            for (Map.Entry<V, Integer> vertex : vertices.entrySet()) {
                if (i == vertex.getValue()) {
                    System.out.print(vertex.getKey().getLabel() + "  ");
                }
            }
            // Print weights for vertices,
            // for this row.
            for (int j = 0; j < size; j++) {
                String spacer = (j != size - 1) ? ", " : "";
                Weight<E> weight = matrix.getCell(i, j);
                Object weightValue;
                // Allow for custom operation,
                // e.g. get something other
                // than label.
                if (weight != null) {
                    weightValue = function.apply(weight.getWeight());
                } else {
                    weightValue = function.apply(nullValue);
                }
                System.out.print(weightValue + spacer);
            }
            System.out.println();
        }
    }
}

class Pair {

    public String key, value;

    public Pair() {
        key = "";
        value = "";
    }

    public Pair(String k, String v) {
        key = k;
        value = v;
    }
}