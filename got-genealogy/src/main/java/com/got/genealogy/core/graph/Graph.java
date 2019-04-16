package com.got.genealogy.core.graph;

import com.got.genealogy.core.graph.collection.AdjacencyMatrix;
import com.got.genealogy.core.graph.property.Edge;
import com.got.genealogy.core.graph.property.Vertex;
import com.got.genealogy.core.graph.property.Weight;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class Graph<Vert extends Vertex, Arc extends Edge> {

    private AdjacencyMatrix<Weight<Arc>> matrix;
    private Map<Vert, Integer> vertices;
    private boolean directed;

    public Graph() {
        this(false);
    }

    public Graph(boolean directed) {
        matrix = new AdjacencyMatrix<>();
        vertices = new HashMap<>();
        this.directed = directed;
    }

    public Arc getEdge(Vert vertex1, Vert vertex2) {
        return getEdgeWeighted(vertex1, vertex2).getWeight();
    }

    public Weight<Arc> getEdgeWeighted(Vert vertex1, Vert vertex2) {
        int fromVertex = vertices.get(vertex1);
        int toVertex = vertices.get(vertex2);
        return matrix.getCell(fromVertex, toVertex);
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

    public Vert getVertex(int index) {
        for (Map.Entry<Vert, Integer> vertex : vertices.entrySet()) {
            if (vertex.getValue().equals(index)) {
                return vertex.getKey();
            }
        }
        return null;
    }

    public Vert getVertex(String label) {
        for (Map.Entry<Vert, Integer> vertex : vertices.entrySet()) {
            if (vertex.getKey().getLabel().equals(label)) {
                return vertex.getKey();
            }
        }
        return null;
    }

    public void addVertex(Vert vertex) {
        // Add vertex with new index
        vertices.put(vertex, vertices.size());
        int newIndex = matrix.size();
        // Add new column to existing rows
        matrix.addColumn(null);
        // Add new empty row
        matrix.addRow();
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
        matrix.removeRow(index);
        matrix.removeColumn(index);
    }

    public boolean visitVertex(Vert vertex) {
        if (vertex.isVisited()) {
            return false;
        } else {
            vertex.setVisited(true);
            return true;
        }
    }

    public void leaveVertex(Vert vertex) {
        vertex.setVisited(false);
    }

    public Set<Vert> adjacentVertices(Vert vertex) {
        int vertexIndex = vertices.get(vertex);
        Set<Vert> adjacentVertices = new HashSet<>();
        List<Weight<Arc>> row, column;

        row = matrix.getRow(vertexIndex);
        column = matrix.getColumn(vertexIndex);

        for (int i = 0; i < row.size(); i++) {
            for (Map.Entry<Vert, Integer> vertexItem : vertices.entrySet()) {
                // TODO: Replace with LinkedHashMap
                // Get vertex with corresponding
                // index in the HashMap.
                if (containsAdjacent(row, vertexItem.getValue(), i)) {
                    adjacentVertices.add(vertexItem.getKey());
                }
                // Matrix rows and columns have
                // the same size. If not directed
                // then look at column too. Column
                // collection is null is not
                // directed.
                if (directed && containsAdjacent(column, vertexItem.getValue(), i)) {
                    // TODO: Need to check if adjacent
                    // still counts, regardless of
                    // direction.
                    adjacentVertices.add(vertexItem.getKey());
                }
            }
        }
        return adjacentVertices;
    }

    public void printGraph() {
        // This will be moved out of Graph.
        // Currently, only being used for
        // testing.
        printGraph(null);
    }

    public void printGraph(Arc nullValue) {
        // Trying to find a way to customise
        // return method, i.e. getLabel().
        printGraph(nullValue, Edge::getLabel);
    }

    public void printGraph(Arc nullValue, Function<Arc, Object> function) {
        int size = matrix.size();
        // Print column labels
        System.out.println();
        System.out.print("   ");
        for (int i = 0; i < size; i++) {
            for (Map.Entry<Vert, Integer> vertex : vertices.entrySet()) {
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
            for (Map.Entry<Vert, Integer> vertex : vertices.entrySet()) {
                if (i == vertex.getValue()) {
                    System.out.print(vertex.getKey().getLabel() + "  ");
                }
            }
            // Print weights for vertices,
            // for this row.
            for (int j = 0; j < size; j++) {
                String spacer = (j != size - 1) ? ", " : "";
                Weight<Arc> weight = matrix.getCell(i, j);
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

    /**
     * Shortcut, used in adjacentVertices.
     * Works only if weights collection
     * matches the number of existing
     * vertices.
     *
     * @param weights        list of weights,
     *                       the order of which
     *                       should match that
     *                       of the matrix.
     * @param vertexPosition position of vertex.
     * @param index          used to get position
     *                       from weights and to
     *                       compare against row
     *                       or column position.
     * @return a boolean after comparing
     * row/column position with position
     * in weights.
     */
    private boolean containsAdjacent(List<Weight<Arc>> weights,
                                     int vertexPosition,
                                     int index) {
        if (weights.size() == vertices.size()) {
            return weights.get(index) != null && vertexPosition == index;
        }
        return false;
    }
}
