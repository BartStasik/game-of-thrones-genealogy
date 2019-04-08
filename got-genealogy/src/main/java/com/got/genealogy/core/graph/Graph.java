package com.got.genealogy.core.graph;

import com.got.genealogy.core.graph.collection.AdjacencyMatrix;
import com.got.genealogy.core.graph.property.Vertex;
import com.got.genealogy.core.graph.property.Weight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Graph<Vert extends Vertex, Arc> {

    private AdjacencyMatrix<Weight<Arc>> matrix;
    private HashMap<Vert, Integer> vertices;
    private boolean directed;

    public Graph() {
        this(false);
    }

    public Graph(boolean directed) {
        matrix = new AdjacencyMatrix<>();
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
        matrix.setCell(fromVertex, toVertex, weight);

        if (!directed) {
            // Add incoming edge
            matrix.setCell(toVertex, fromVertex, weight);
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
        matrix.addColumn(null);
        // Add new empty row
        matrix.addRow(new ArrayList<>());
        // Fill new row
        matrix.fillRow(newIndex, null);
    }

    public void removeVertex(Vert vertex) {
        int index = vertices.get(vertex);
        // Remove and shift left
        vertices.remove(vertex);
        vertices.replaceAll((k, v) -> {
            return (v >= index) ? v - 1 : v;
        });
        // Remove from both axes
        matrix.removeColumn(index);
        matrix.removeRow(index);
    }

    public boolean visitVertex(Vert vertex) {
        if (vertex.isVisited()) {
            // Can use to check if visited
            return false;
        } else {
            vertex.setVisited(true);
            return true;
        }
    }

    public void leaveVertex(Vert vertex) {
        vertex.setVisited(false);
    }

    public void setAnnotation(Vert vertex, String annotation) {
        // Can use for custom annotations
        vertex.setAnnotation(annotation);
    }

    public String getAnnotation(Vert vertex) {
        return vertex.getAnnotation();
    }

    public void printGraph() {
        printGraph(null);
    }

    public void printGraph(Arc nullValue) {
        int size = matrix.size();
        // Print column labels
        System.out.println();
        System.out.print("   ");
        for (int i = 0; i < size; i++) {
            for (Map.Entry<Vert, java.lang.Integer> vertex : vertices.entrySet()) {
                if (i == vertex.getValue()) {
                    System.out.print(vertex.getKey().getLabel() + "  ");
                }
            }
        }

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
                Weight<Arc> weight = matrix.getCell(i, j);
                Arc weightValue = weight != null ? weight.getWeight() : nullValue;
                System.out.print(weightValue + spacer);
            }
            System.out.println();

        }
    }
}
