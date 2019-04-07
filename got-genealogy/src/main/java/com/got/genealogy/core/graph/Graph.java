package com.got.genealogy.core.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph<Vert extends Vertex> {

    private List<List<Weight<Integer>>> matrix;
    private HashMap<Vert, Integer> vertices;
    private boolean directed;

    public Graph(boolean d) {
        matrix = new ArrayList<>();
        vertices = new HashMap<>();
        directed = d;
    }

    public void addEdge(Vert vertex1, Vertex vertex2) {
        // Get index numbers of vertices
        int fromVertex = vertices.get(vertex1);
        int toVertex = vertices.get(vertex2);

        // Add outgoing edge
        matrix.get(fromVertex)
                .get(toVertex)
                .setWeight(1);

        if (!directed) {
            // Add incoming edge
            matrix.get(toVertex)
                    .get(fromVertex)
                    .setWeight(1);
        }
    }

    public void addVertex(Vert vertex) {
        // Add vertex with new index
        vertices.put(vertex, vertices.size());
        int newIndex = matrix.size();

        // Add new column to existing rows
        for (List<Weight<Integer>> row : matrix) {
            row.add(new Weight<>(0));
        }

        // Add new empty row
        matrix.add(new ArrayList<>());

        // Fill new row
        for (int i = 0; i <= newIndex; i++) {
            matrix.get(newIndex)
                    .add(new Weight<>(0));
        }
    }

    public void removeVertex(Vert vertex) {
        int index = vertices.get(vertex);

        // Remove and shift left
        vertices.remove(vertex);
        vertices.replaceAll((k, v) -> {
            return v >= index ? v-1 : v;
        });

        // Remove from both axes
        matrix.remove(index);
        for (List<Weight<Integer>> row : matrix) {
            row.remove(index);
        }
    }

    public void printGraph() {
        int size = matrix.size();
        for (int i = 0; i < size; i++) {
            // HashMap isn't ordered, so
            // get correct vertex label
            // and print with space.
            for (Map.Entry<Vert, Integer> vertex : vertices.entrySet()) {
                if (i == vertex.getValue()) {
                    System.out.print(vertex.getKey().getLabel() + "  ");
                }
            }
            // Print weights for vertices,
            // for this row.
            for (int j = 0; j < size; j++) {
                String printValue = (j != size-1) ? ", " : "";
                int weight = matrix.get(i).get(j).getWeight();
                System.out.print(weight + printValue);
            }
            System.out.println();
        }
    }
}