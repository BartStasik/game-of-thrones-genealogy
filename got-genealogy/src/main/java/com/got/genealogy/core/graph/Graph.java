package com.got.genealogy.core.graph;

import com.got.genealogy.core.graph.collection.AdjacencyList;
import com.got.genealogy.core.graph.collection.AdjacencyMatrix;
import com.got.genealogy.core.graph.property.Edge;
import com.got.genealogy.core.graph.property.Vertex;
import com.got.genealogy.core.graph.property.Weight;
import com.got.genealogy.core.graph.property.WeightedVertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Graph object, storing vertices of type Vertex
 * and edges of type Edge, in an AdjacencyMatrix.
 *
 * Edges are wrapped in the Weight wrapper class,
 * to distinguish between null edges (signifying
 * a lack of an Edge) and edges with null weights
 * (signifying an Edge, but no weight).
 *
 * @param <V> of type Vertex.
 * @param <E> of type Edge.
 */
public class Graph<V extends Vertex, E extends Edge> {

    private AdjacencyMatrix<Weight<E>> matrix;
    private Map<V, Integer> vertices;
    private boolean directed;
    private String label;

    /**
     * Graph constructor, specifying an empty
     * label and making it undirected by default.
     */
    public Graph() {
        this("", false);
    }

    /**
     * Graph constructor, specifying its label
     * and making it undirected by default.
     *
     * @param label new label.
     */
    public Graph(String label) {
        this(label, false);
    }

    /**
     * Graph constructor, specifying an empty
     * label and it is directed.
     *
     * @param directed boolean, used to determine
     *                 how edges are mapped in an
     *                 AdjacencyMatrix.
     */
    public Graph(boolean directed) {
        this("", directed);
    }

    /**
     * Graph constructor, specifying its label
     * and if it is directed.
     *
     * @param label    new label.
     * @param directed boolean, used to determine
     *                 how edges are mapped in an
     *                 AdjacencyMatrix.
     */
    public Graph(String label, boolean directed) {
        matrix = new AdjacencyMatrix<>();
        vertices = new HashMap<>();
        this.directed = directed;
        this.label = label;
    }

    /**
     * Returns the AdjacencyMatrix object, used
     * to store the weights between all vertices.
     *
     * @return the AdjacencyMatrix object, used
     * to store the weights between all vertices.
     */
    public AdjacencyMatrix<Weight<E>> getAdjacencyMatrix() {
        return matrix;
    }

    /**
     * Returns a Map of Vertex-Integer entries,
     * where each key's value is a position index,
     * corresponding to a row and column in an
     * AdjacencyMatrix.
     *
     * @return a Map of Vertex-Integer entries,
     * where each key's value is a position index,
     * corresponding to a row and column in an
     * AdjacencyMatrix.
     */
    public Map<V, Integer> getVertices() {
        return vertices;
    }

    /**
     * Returns this Graph's label.
     *
     * @return this Graph's label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Replaces the label of this Graph.
     *
     * @param label new label.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Compares an Object with this Graph's
     * label and <tt>directed</tt> boolean.
     *
     * @param object being compared with this Graph.
     * @return <tt>true</tt> if the hashCode is the
     * same, or if both objects are directed and
     * they have the same labels.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null) {
            return false;
        }
        if (!(object instanceof Graph)) {
            return false;
        }
        Graph<?, ?> graph = (Graph<?, ?>) object;
        return directed == graph.directed &&
                graph.label.equals(this.label);
    }

    /**
     * Returns the hashCode of this Graph, based
     * on its label and the <tt>directed</tt> boolean.
     *
     * @return the hashCode of this Graph, based
     * on its label and the <tt>directed</tt> boolean.
     */
    @Override
    public int hashCode() {
        return Objects.hash(directed, label);
    }

    /**
     * Returns the Edge between two Vertex objects,
     * using the labels to compare them.
     *
     * @param label1 first Vertex.
     * @param label2 second Vertex.
     * @return null if an Edge doesn't exist between
     * the two Vertex objects or there aren't any
     * Vertex objects with those labels.
     */
    public E getEdge(String label1, String label2) {
        V vertex1 = getVertex(label1);
        V vertex2 = getVertex(label2);
        return getEdge(vertex1, vertex2);
    }

    /**
     * Returns the Edge between two Vertex objects.
     * <p>
     * No filter is used.
     *
     * @param vertex1 first Vertex.
     * @param vertex2 second Vertex.
     * @return null if an Edge doesn't exist between
     * the two Vertex objects.
     */
    public E getEdge(V vertex1, V vertex2) {
        return getEdge(vertex1, vertex2, (e) -> true);
    }

    /**
     * Returns the Edge between two Vertex objects,
     * if it exists and meets the criteria of the
     * provided filter.
     *
     * @param vertex1 first Vertex.
     * @param vertex2 second Vertex.
     * @param filter  used to exclude Edges if they
     *                don't meet custom criteria.
     * @return null if an Edge doesn't exist between
     * the two Vertex objects and it doesn't meet
     * the filter criteria.
     */
    public E getEdge(V vertex1,
                     V vertex2,
                     Function<E, Boolean> filter) {
        Weight<E> edge = getEdgeWeighted(vertex1, vertex2);
        if (edge != null && filter.apply(edge.getWeight())) {
            return edge.getWeight();
        }
        return null;
    }

    /**
     * Returns the Edge between two Vertex objects,
     * using their labels, inside the Weight
     * wrapper class.
     *
     * @param label1 first Vertex.
     * @param label2 second Vertex.
     * @return the Edge, inside the Weight wrapper
     * class.
     */
    public Weight<E> getEdgeWeighted(String label1, String label2) {
        V vertex1 = getVertex(label1);
        V vertex2 = getVertex(label2);
        return getEdgeWeighted(vertex1, vertex2);
    }

    /**
     * Returns the Edge between two Vertex objects,
     * inside the Weight wrapper class.
     *
     * @param vertex1 first Vertex.
     * @param vertex2 second Vertex.
     * @return the Edge, inside the Weight wrapper
     * class.
     */
    public Weight<E> getEdgeWeighted(V vertex1, V vertex2) {
        if (!existingVertex(vertex1, vertex2)) {
            return null;
        }
        int fromVertex = vertices.get(vertex1);
        int toVertex = vertices.get(vertex2);
        return matrix.getCell(fromVertex, toVertex);
    }

    /**
     * Adds an Edge between two Vertex objects,
     * if one doesn't already exist.
     *
     * @param vertex1 first Vertex.
     * @param vertex2 second Vertex.
     */
    public void addEdge(V vertex1, V vertex2) {
        addEdge(vertex1, vertex2, new Weight<>());
    }

    /**
     * Adds an Edge between two Vertex objects,
     * if one doesn't already exist.
     *
     * @param vertex1 first Vertex.
     * @param vertex2 second Vertex.
     * @param weight  wrapper class used to either
     *                deem an Edge between two
     *                vertices non-existent or
     *                simply without a weight.
     */
    public void addEdge(V vertex1, V vertex2, Weight<E> weight) {
        if (existingVertex(vertex1, vertex2)) {
            // Get index numbers of vertices
            V vertexItem1 = getVertex(vertex1.getLabel());
            V vertexItem2 = getVertex(vertex2.getLabel());
            int fromVertex = vertices.get(vertexItem1);
            int toVertex = vertices.get(vertexItem2);
            // Add outgoing edge
            matrix.setCell(fromVertex, toVertex, weight);
            if (!directed) {
                // Add incoming edge
                matrix.setCell(toVertex, fromVertex, weight);
            }
        }
    }

    /**
     * Replaces an Edge between two Vertex objects
     * with null, if one already exists.
     *
     * @param vertex1 first Vertex.
     * @param vertex2 second Vertex.
     */
    public void removeEdge(V vertex1, V vertex2) {
        if (existingEdge(vertex1, vertex2)) {
            addEdge(vertex1, vertex2, null);
        }
    }

    /**
     * Gets a Vertex object, using its position
     * in the AdjacencyMatrix. This value is the
     * value in the Entry of Vertex to Integer.
     *
     * @param index of the Vertex in the matrix.
     * @return null if the Vertex doesn't exist.
     */
    public V getVertex(int index) {
        for (Map.Entry<V, Integer> vertex : vertices.entrySet()) {
            if (vertex.getValue().equals(index)) {
                return vertex.getKey();
            }
        }
        return null;
    }

    /**
     * Gets a Vertex object, using its label and
     * with a filter applied, to specify how to
     * differentiate vertices.
     * <p>
     * Doesn't use a filter, so the object's
     * hashCode would be used to differentiate
     * between them.
     *
     * @param label of the Vertex to be returned.
     * @return null if the Vertex doesn't exist.
     */
    public V getVertex(String label) {
        return getVertex(label, (e) -> e);
    }

    /**
     * Gets a Vertex object, using its label and
     * with a filter applied, to specify how to
     * differentiate vertices.
     *
     * @param label  of the Vertex to be returned.
     * @param filter used to specify how to
     *               differentiate between Vertex
     *               objects in the Map.
     * @return null if the Vertex doesn't exist.
     */
    public V getVertex(String label,
                       Function<String, String> filter) {
        for (V vertex : vertices.keySet()) {
            String vertexFiltered = filter.apply(vertex.getLabel());
            String labelFiltered = filter.apply(label);
            if (vertexFiltered.equals(labelFiltered)) {
                return vertex;
            }
        }
        return null;
    }

    /**
     * Adds a new Vertex object to the
     * AdjacencyMatrix, if it doesn't already
     * exist.
     *
     * @param vertex Vertex object to be added.
     * @return null if the Vertex already
     * exists.
     */
    public V addVertex(V vertex) {
        if (existingVertex(vertex)) {
            return null;
        }
        // Add vertex with new index
        vertices.put(vertex, vertices.size());
        int newIndex = matrix.size();
        // Add new column to existing rows
        matrix.addColumn(null);
        // Add new empty row
        matrix.addRow();
        // Fill new row
        matrix.fillRow(newIndex, null);
        return vertex;
    }

    /**
     * Removes an already-existing vertex from
     * the AdjacencyMatrix and the Map of
     * Vertex objects.
     * <p>
     * Shifts the corresponding position indexes
     * for every Vertex that came after the old
     * one.
     *
     * @param vertex Vertex object to be removed.
     */
    public void removeVertex(V vertex) {
        if (existingVertex(vertex)) {
            V vertexItem = getVertex(vertex.getLabel());
            Integer index = vertices.get(vertexItem);
            // Remove and shift left
            vertices.remove(vertexItem);
            vertices.replaceAll((k, v) -> {
                return (v >= index) ? v - 1 : v;
            });
            // Remove from both axes
            matrix.removeRow(index);
            matrix.removeColumn(index);
        }
    }

    /**
     * Checks if an edge between two Vertex
     * objects doesn't equal to null.
     * <p>
     * If there is an edge between two Vertex
     * objects, but with a weight of null, this
     * will return <tt>true</tt>.
     *
     * @param vertex1 first Vertex.
     * @param vertex2 second Vertex.
     * @return <tt>true</tt> if there is an Edge
     * between the two vertices.
     */
    public boolean existingEdge(V vertex1, V vertex2) {
        return getEdge(vertex1, vertex2) != null;
    }

    /**
     * Checks if a vertex is stored in the
     * vertices Map.
     *
     * @param vertex Vertex object, of which
     *               the label is used to check
     *               if it exists.
     * @return null if a Vertex with that label
     * doesn't exist.
     */
    public boolean existingVertex(V vertex) {
        if (vertex == null) {
            return false;
        }
        V vertexItem = getVertex(vertex.getLabel());
        return vertexItem != null;
    }

    /**
     * Checks if two Vertex objects exist in
     * the Graph.
     *
     * @param vertex1 first Vertex object.
     * @param vertex2 second Vertex object.
     * @return <tt>true</tt> if both vertices
     * exist.
     */
    public boolean existingVertex(V vertex1, V vertex2) {
        return existingVertex(vertex1) && existingVertex(vertex2);
    }

    /**
     * Returns a set of Vertex objects, all of
     * which are adjacent to one Vertex.
     * <p>
     * Goes through the collection of existing
     * Vertex objects and the AdjacencyMatrix
     * to see where there is an edge, in both
     * rows and columns.
     * <p>
     * Doesn't check for columns, if the graph
     * isn't directed.
     * <p>
     * Doesn't use a filter.
     *
     * @param vertex to which adjacent vertices
     *               are being found.
     * @return a set of Vertex objects, all of
     * which are adjacent to one Vertex.
     */
    public Set<V> adjacentVertices(V vertex) {
        return adjacentVertices(vertex, (list, index) -> true);
    }

    /**
     * Returns a set of Vertex objects, all of
     * which are adjacent to one Vertex.
     * <p>
     * Goes through the collection of existing
     * Vertex objects and the AdjacencyMatrix
     * to see where there is an edge, in both
     * rows and columns.
     * <p>
     * Doesn't check for columns, if the graph
     * isn't directed.
     * <p>
     * Filters adjacent nodes by applying a
     * BiFunction, which takes a List of
     * Weights and an position index of type
     * Integer. Returns a Boolean, which is
     * used for the actual filtering.
     *
     * @param vertex to which adjacent vertices
     *               are being found.
     * @param filter used to ignore adjacent
     *               vertices, according the
     *               BiFunction.
     * @return a set of Vertex objects, all of
     * which are adjacent to one Vertex.
     */
    public Set<V> adjacentVertices(V vertex,
                                   BiFunction<List<Weight<E>>, Integer, Boolean> filter) {
        if (existingVertex(vertex)) {
            int vertexIndex = vertices.get(vertex);
            Set<V> adjacentVertices = new HashSet<>();
            List<Weight<E>> row, column;

            row = matrix.getRow(vertexIndex);
            column = matrix.getColumn(vertexIndex);

            for (int i = 0; i < row.size(); i++) {
                for (Map.Entry<V, Integer> vertexItem : vertices.entrySet()) {
                    // Get vertex with corresponding
                    // index in the HashMap.
                    if (containsAdjacent(row, vertexItem.getValue(), i, filter)) {
                        adjacentVertices.add(vertexItem.getKey());
                    }
                    // Matrix rows and columns have
                    // the same size. If not directed
                    // then look at column too. Column
                    // collection is null is not
                    // directed.
                    if (directed &&
                            containsAdjacent(column, vertexItem.getValue(), i, filter)) {
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

    /**
     * Convert the AdjacencyMatrix into an
     * AdjacencyList, with weighted vertices, to
     * store the weight between connected nodes.
     * <p>
     * Only looking at rows in matrix.
     *
     * @return AdjacencyList of vertices adjacent
     * to weighted vertices.
     */
    public AdjacencyList<V, E> adjacencyListWeighted() {
        AdjacencyList<V, E> adjacencyList = new AdjacencyList<>();
        // For each vertex, check adjacent
        // vertices and attach a weight to
        // them.
        vertices.forEach((vertex, vertexIndex) -> {
            List<Weight<E>> row = matrix.getRow(vertexIndex);
            Set<WeightedVertex<V, E>> adjacentVertices = new HashSet<>();
            // Get edge and add to set
            for (Map.Entry<V, Integer> vertexItem : vertices.entrySet()) {
                V adjacentVertex = vertexItem.getKey();
                E weight = getEdge(vertex, adjacentVertex);
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

    /**
     * Use DFS traversal to see if any path exists
     * between two vertices.
     *
     * @param vertex1 Starting vertex.
     * @param vertex2 Goal vertex.
     * @return Boolean after running DFS traversal.
     */
    public boolean pathExistsBetween(V vertex1, V vertex2) {
        return depthFirstTraversal(vertex1).contains(vertex2);
    }

    /**
     * Depth-first traversal of the graph from
     * a starting vertex.
     *
     * @param vertex Starting vertex.
     * @return ArrayList of vertices, in order
     * of the path.
     */
    public List<V> depthFirstTraversal(V vertex) {
        return depthFirstTraversal(vertex, (list, index) -> true);
    }

    /**
     * Depth-first traversal of the graph from
     * a starting vertex, using the filter
     * provided as a BiFunction, which looks a
     * List of Weight objects and their index,
     * and returns a Boolean.
     *
     * @param vertex Starting vertex.
     * @return ArrayList of vertices, in order
     * of the path.
     */
    public List<V> depthFirstTraversal(V vertex,
                                       BiFunction<List<Weight<E>>, Integer, Boolean> filter) {
        Stack<V> stack = new Stack<>();
        List<V> path = new ArrayList<>();
        // Un-visit all vertices before
        // traversing. Push starting
        // vertex onto the stack.
        vertices.forEach((k, v) -> k.setVisited(false));
        stack.push(vertex);
        while (!stack.empty()) {
            V topVertex = stack.pop();
            if (!topVertex.isVisited()) {
                path.add(topVertex);
                topVertex.setVisited(true);
                // Safely iterate over
                // adjacent vertices
                // (regardless of direction)
                // and push unvisited onto
                // stack.
                Iterator<V> adjacentVertices = adjacentVertices(topVertex, filter).iterator();
                while (adjacentVertices.hasNext()) {
                    V adjacentVertex = adjacentVertices.next();
                    if (!adjacentVertex.isVisited()) {
                        // Push unvisited neighbour
                        stack.push(adjacentVertex);
                    }
                }
            }
        }
        return path;
    }

    /**
     * Gets the shortest, unweighted, path between
     * two vertices, without a filter.
     *
     * @param vertex1 Starting point, for graph
     *                traversal.
     * @param vertex2 Goal vertex, to reach after
     *                traversing all relatives of
     *                vertex1.
     * @return Return the shortestPath of the inner
     * class, after graph traversal.
     */
    public List<V> getShortestUnweightedPath(V vertex1, V vertex2) {
        return getShortestUnweightedPath(vertex1, vertex2, (list, index) -> true);
    }

    /**
     * Gets the shortest, unweighted, path between
     * two vertices, using the filter provided as a
     * BiFunction, which looks a List of Weight
     * objects and their index, and returns a Boolean.
     * <p>
     * Uses an inner class to initialise a shortestPath
     * ArrayList and update its contents based on the
     * result from a recursive relative-traversal
     * method.
     *
     * @param vertex1 Starting point, for graph
     *                traversal.
     * @param vertex2 Goal vertex, to reach after
     *                traversing all relatives of
     *                vertex1.
     * @return Return the shortestPath of the inner
     * class, after graph traversal.
     */
    public List<V> getShortestUnweightedPath(V vertex1,
                                             V vertex2,
                                             BiFunction<List<Weight<E>>, Integer, Boolean> filter) {
        // Need to use an inner class, to use
        // with the adjacentVertices() method
        // from the inherited Graph class.
        class PathProcessor {
            // Can directly access shortestPath
            // outside this nested class.
            private List<V> shortestPath = new ArrayList<>();

            /**
             * Prepare an empty directed path list and
             * call processAllPaths to update the
             * shortestPath.
             *
             * @param vertex1   Starting point for
             *                  graph traversal.
             * @param vertex2   Goal vertex, to reach
             *                  after traversing.
             */
            private PathProcessor(V vertex1,
                                  V vertex2,
                                  BiFunction<List<Weight<E>>, Integer, Boolean> filter) {
                List<V> tempPath = new ArrayList<>();
                tempPath.add(vertex1);
                processAllPaths(vertex1, vertex2, tempPath, filter);
            }

            /**
             * Recursive path processor. Goes
             * through all possible paths from
             * vertex1 to vertex2 and updates the
             * shortestPath if a shortest path exists.
             *
             * @param vertex1   Starting point for
             *                  for graph traversal.
             *                  Every relative of
             *                  initial vertex is
             *                  recursively traversed.
             * @param vertex2   Goal vertex, to reach
             *                  after traversing.
             *                  Unvisited when reached,
             *                  to search for other
             *                  possible paths.
             * @param currentPath   Path-so-far tracker.
             */
            private void processAllPaths(V vertex1,
                                         V vertex2,
                                         List<V> currentPath,
                                         BiFunction<List<Weight<E>>, Integer, Boolean> filter) {
                vertex1.setVisited(true);
                if (vertex1.equals(vertex2)) {
                    // Need to un-visit node to
                    // look for other possible paths.
                    vertex1.setVisited(false);
                    boolean shorterPath = currentPath.size() < this.shortestPath.size();
                    boolean emptyGlobalPath = this.shortestPath.size() == 0;

                    if (shorterPath || emptyGlobalPath) {
                        // Update shortest path variable
                        this.shortestPath = new ArrayList<>(currentPath);
                    }
                }
                // Get all relatives and
                // traverse graph over all
                // unvisited relatives.
                Iterator<V> adjacentVertices = adjacentVertices(vertex1, filter).iterator();
                while (adjacentVertices.hasNext()) {
                    V adjacentVertex = adjacentVertices.next();
                    if (!adjacentVertex.isVisited()) {
                        currentPath.add(adjacentVertex);
                        // Recursively traverse neighbours
                        processAllPaths(adjacentVertex, vertex2, currentPath, filter);
                        currentPath.remove(adjacentVertex);
                    }
                }
                vertex1.setVisited(false);
            }
        }
        return new PathProcessor(vertex1, vertex2, filter).shortestPath;
    }

    /**
     * Shortcut, used in adjacentVertices.
     * <p>
     * Works only if weights collection matches
     * the number of existing vertices.
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
     * @return a boolean after comparing the row
     * or column position with its position in
     * a list of weights.
     */
    private boolean containsAdjacent(List<Weight<E>> weights,
                                     int vertexPosition,
                                     int index,
                                     BiFunction<List<Weight<E>>, Integer, Boolean> filter) {
        if (weights.size() == vertices.size() && weights.get(index) != null) {
            return vertexPosition == index && filter.apply(weights, index);
        }
        return false;
    }
}