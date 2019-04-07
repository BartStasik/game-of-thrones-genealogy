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
        int fromVertex = vertices.get(vertex1);
        int toVertex = vertices.get(vertex2);

        matrix.get(fromVertex)
                .get(toVertex)
                .setWeight(1);

        if (!directed) {
            matrix.get(toVertex)
                    .get(fromVertex)
                    .setWeight(1);
        }
    }

    public void addVertex(Vert vertex) {
        vertices.put(vertex, vertices.size());
        int newIndex = matrix.size();

        for (List<Weight<Integer>> row : matrix) {
            row.add(new Weight<>(0));
        }

        matrix.add(new ArrayList<>());

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
        for (int i = 0; i < matrix.size(); i++) {
            for (Map.Entry<Vert, Integer> vertex : vertices.entrySet()) {
                if (i == vertex.getValue()) {
                    System.out.print(vertex.getKey().getLabel() + "  ");
                }
            }
            for (int j = 0; j < matrix.get(i).size(); j++) {
                String printValue;
                printValue = j == matrix.get(i).size() - 1 ? "" : ", ";
                System.out.print(matrix.get(i).get(j).getWeight() + printValue);
            }
            System.out.println();
        }
    }
}