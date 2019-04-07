package com.got.genealogy.core.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public void printGraph() {
        for (List<Weight<Integer>> row : matrix) {
            for (int i = 0; i < row.size(); i++) {
                String printValue;
                printValue = i == row.size() - 1 ? "" : ", ";
                System.out.print(row.get(i).getWeight() + printValue);
            }
            System.out.println();
        }
    }
}