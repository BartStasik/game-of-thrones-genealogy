package com.got.genealogy.core.graph;

import com.got.genealogy.core.graph.collection.AdjacencyList;
import com.got.genealogy.core.graph.collection.AdjacencyMatrix;
import com.got.genealogy.core.graph.property.Edge;
import com.got.genealogy.core.graph.property.Vertex;
import com.got.genealogy.core.graph.property.Weight;
import com.got.genealogy.core.graph.property.WeightedVertex;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        if (getEdgeWeighted(vertex1, vertex2) == null) {
            return null;
        }
        return getEdgeWeighted(vertex1, vertex2).getWeight();
    }

    public Weight<Arc> getEdgeWeighted(Vert vertex1, Vert vertex2) {
        if (existingVertex(vertex1, vertex2)) {
            int fromVertex = vertices.get(vertex1);
            int toVertex = vertices.get(vertex2);
            return matrix.getCell(fromVertex, toVertex);
        }
        return null;
    }

    public void addEdge(Vert vertex1, Vert vertex2) {
        addEdge(vertex1, vertex2, new Weight<>());
    }

    public void addEdge(Vert vertex1, Vert vertex2, Weight<Arc> weight) {
        if (existingVertex(vertex1, vertex2)) {
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
        if (!existingVertex(vertex)) {
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
    }

    public void removeVertex(Vert vertex) {
        if (existingVertex(vertex)) {
            Integer index = vertices.get(vertex);
            // Remove and shift left
            vertices.remove(vertex);
            vertices.replaceAll((k, v) -> {
                return (v >= index) ? v - 1 : v;
            });
            // Remove from both axes
            matrix.removeRow(index);
            matrix.removeColumn(index);
        }
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

    public boolean isAdjacent(Vert vertex1, Vert vertex2) {
        return getEdge(vertex1, vertex2) != null;
    }

    public Set<Vert> adjacentVertices(Vert vertex) {
        if (existingVertex(vertex)) {
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
                        // Adjacent if incoming or
                        // outgoing from vertex.
                        adjacentVertices.add(vertexItem.getKey());
                    }
                }
            }
            return adjacentVertices;
        }
        return null;
    }

    public AdjacencyList<Vert, Arc> adjacencyListWeighted() {
        AdjacencyList<Vert, Arc> adjacencyList = new AdjacencyList<>();
        // For each vertex, check adjacent
        // vertices and attach a weight to
        // them
        vertices.forEach((vertex, vertexIndex) -> {
            List<Weight<Arc>> row = matrix.getRow(vertexIndex);
            Set<WeightedVertex<Vert, Arc>> adjacentVertices = new HashSet<>();
            // Get edge and add to set
            for (Map.Entry<Vert, Integer> vertexItem : vertices.entrySet()) {
                Vert adjacentVertex = vertexItem.getKey();
                Arc weight = getEdge(vertex, adjacentVertex);
                if (weight != null) {
                    adjacentVertices.add(
                            new WeightedVertex<>(
                                    adjacentVertex,
                                    weight));
                }
            }
            adjacencyList.put(vertex, adjacentVertices);
        });
        return adjacencyList;
    }

    public AdjacencyMatrix<Weight<Arc>> adjacencyMatrix() {
        return matrix;
    }

    public Map<Vert, Integer> vertices() {
        return vertices;
    }

    private boolean existingVertex(Vert vertex) {
        boolean b = vertices.get(vertex) != null;
        return b;
    }

    private boolean existingVertex(Vert vertex1, Vert vertex2) {
        return existingVertex(vertex1) && existingVertex(vertex2);
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