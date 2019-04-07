package com.got.genealogy.core.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph<Vert extends Vertex, Arc> {

    private List<List<Weight<Arc>>> matrix;
    private HashMap<Vert, Integer> vertices;
    private boolean directed;

    public Graph() {
        this(false);
    }

    public Graph(boolean directed) {
        matrix = new ArrayList<>();
        vertices = new HashMap<>();
        this.directed = directed;
    }


    public void addEdge(Vert vertex1, Vert vertex2) {
        addEdge(vertex1, vertex2, new Weight<>());
    }


    public void addEdge(Vert vertex1, Vert vertex2, Weight<Arc> weight) {
        // Get index numbers of vertices
        int fromVertex = vertices.get(vertex1);
        int toVertex = vertices.get(vertex2);

        // Add outgoing edge
        matrix.get(fromVertex)
                .set(toVertex, weight);

        if (!directed) {
            // Add incoming edge
            matrix.get(toVertex)
                    .set(fromVertex, weight);
        }
    }

    public void removeEdge(Vert vertex1, Vert vertex2) {
        addEdge(vertex1, vertex2, null);
    }

    public void addVertex(Vert vertex) {
        // Add vertex with new index
        vertices.put(vertex, vertices.size());
        int newIndex = matrix.size();

        // Add new column to existing rows
        for (List<Weight<Arc>> row : matrix) {
            row.add(null);
        }

        // Add new empty row
        matrix.add(new ArrayList<>());

        // Fill new row
        for (int i = 0; i <= newIndex; i++) {
            matrix.get(newIndex)
                    .add(null);
        }
    }

    public void removeVertex(Vert vertex) {
        int index = vertices.get(vertex);

        // Remove and shift left
        vertices.remove(vertex);
        vertices.replaceAll((k, v) -> {
            return (v >= index) ? v - 1 : v;
        });

        // Remove from both axes
        matrix.remove(index);
        for (List<Weight<Arc>> row : matrix) {
            row.remove(index);
        }
    }

    public void printGraph() {
        int size = matrix.size();
        System.out.println();
        for (int i = 0; i < size; i++) {
            // HashMap isn't ordered, so
            // get correct vertex label
            // and print with space.
            for (Map.Entry<Vert, java.lang.Integer> vertex : vertices.entrySet()) {
                if (i == vertex.getValue()) {
                    System.out.print(vertex.getKey().getLabel() + "  ");
                }
            }
            // Print weights for vertices,
            // for this row.
            for (int j = 0; j < size; j++) {
                String spacer = (j != size - 1) ? ", " : "";
                Weight<Arc> weight = matrix.get(i).get(j);
                Arc weightValue = weight != null ? weight.getWeight() : null;
                System.out.print(weightValue + spacer);
            }
            System.out.println();
        }
    }
}
