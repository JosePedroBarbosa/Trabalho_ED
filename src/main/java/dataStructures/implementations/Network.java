package dataStructures.implementations;

import dataStructures.ADTS.NetworkADT;

import java.util.Iterator;

/**
 * Represents a weighted undirected network.
 *
 * @param <T> the type of the vertices in the network
 */
public class Network<T> extends Graph<T> implements NetworkADT<T> {

    /**
     * Constructs a new empty network with default capacity.
     */
    public Network(){
        super();
    }

    /**
     * Constructs a new empty network with a given capacity.
     *
     * @param initialCapacity the initial capacity of the network
     */
    public Network(int initialCapacity){
        super(initialCapacity);
    }

    /**
     * Adds an edge between two vertices in the network with a given weight.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     * @param weight the weight of the edge
     */
    @Override
    public void addEdge(T vertex1, T vertex2, double weight) {
        this.addEdge(getIndex(vertex1), getIndex(vertex2), weight);
    }

    /**
     * Adds an edge between two vertices in the network given their indices and weight.
     *
     * @param index1 the index of the first vertex
     * @param index2 the index of the second vertex
     * @param weight the weight of the edge
     */
    private void addEdge(int index1, int index2, double weight) {
        if(indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = weight;
            adjMatrix[index2][index1] = weight;
        }
    }

    /**
     * Returns the weight of the edge between two vertices.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     * @return the weight of the edge, or {@code Double.POSITIVE_INFINITY} if no edge exists
     */
    public double getEdgeWeight(T vertex1, T vertex2) {
        return getEdgeWeight(getIndex(vertex1), getIndex(vertex2));
    }

    /**
     * Returns the weight of the edge between two vertices given their indices.
     *
     * @param index1 the index of the first vertex
     * @param index2 the index of the second vertex
     * @return the weight of the edge, or Double.POSITIVE_INFINITY if no edge exists
     */
    private double getEdgeWeight(int index1, int index2) {
        if(indexIsValid(index1) && indexIsValid(index2)) {
            return adjMatrix[index1][index2];
        }
        return Double.POSITIVE_INFINITY;
    }

    /**
     * Finds the shortest path between two vertices and returns the path as a list of vertices.
     *
     * @param startVertex the starting vertex
     * @param endVertex the destination vertex
     * @return an ArrayUnorderedList of vertices representing the shortest path
     */
    public ArrayUnorderedList<T> shortestPath(T startVertex, T endVertex) {
        Iterator<Integer> it = dijkstra(getIndex(startVertex), getIndex(endVertex));
        ArrayUnorderedList<T> path = new ArrayUnorderedList<>();

        while (it.hasNext()) {
            path.addToRear(vertices[it.next()]);
        }

        return path;
    }

    /**
     * Calculates the weight of the shortest path between two vertices by their values.
     *
     * @param vertex1 The starting vertex.
     * @param vertex2 The destination vertex.
     * @return The weight of the shortest path, or Double.POSITIVE_INFINITY if no path exists.
     */
    @Override
    public double shortestPathWeight(T vertex1, T vertex2) {
        return shortestPathWeight(getIndex(vertex1), getIndex(vertex2));
    }

    /**
     * Calculates the weight of the shortest path between two vertices specified by their indices.
     *
     * @param startIndex The index of the starting vertex.
     * @param targetIndex The index of the destination vertex.
     * @return The total weight of the shortest path between the vertices, or Double.POSITIVE_INFINITY if no path exists.
     */
    public double shortestPathWeight(int startIndex, int targetIndex) {
        double result = 0;
        if(!indexIsValid(startIndex) || !indexIsValid(targetIndex)){
            return Double.POSITIVE_INFINITY;
        }

        int index1, index2;
        Iterator<Integer> it = dijkstra(startIndex, targetIndex);

        if(it.hasNext()){
            index1 = it.next();
        }else {
            return Double.POSITIVE_INFINITY;
        }

        while(it.hasNext()){
            index2 = it.next();
            result += adjMatrix[index1][index2];
            index1 = index2;
        }

        return result;
    }

    /**
     * Finds the vertex with the smallest distance that has not yet been processed.
     *
     * @param distance Array containing the current shortest distances from the source to each vertex.
     * @param spSet Boolean array indicating whether each vertex has been processed.
     * @return The index of the vertex with the smallest distance.
     */
    int minimumDistance(double[] distance, boolean[] spSet) {
        // Initialize the minimum value
        double m = Double.POSITIVE_INFINITY; // The smallest distance starts as infinite
        int m_index = -1; // Index of the vertex with the smallest distance

        // Loop through all vertices
        for (int i = 0; i < numVertices; i++) {
            // If the vertex is not yet processed and its distance is smaller or equal to the current minimum
            if (!spSet[i] && distance[i] <= m) {
                m = distance[i]; // Update the minimum value
                m_index = i;     // Update the index of the vertex with the smallest distance
            }
        }

        return m_index; // Return the index of the vertex with the smallest distance
    }

    /**
     * Implements Dijkstra's shortest path algorithm for a graph represented as an adjacency matrix.
     *
     * This method was inspired by an implementation of Dijkstra and can be found at the following link:
     * "https://www.javatpoint.com/dijkstra-algorithm-java". We have modified the algorithm to return an Integer Iterator
     * of the vertex positions and adapted the variables to fit our network implementation.
     *
     * @param s The source vertex from which the shortest path is calculated.
     * @param t The target vertex to which the shortest path is calculated.
     * @return An iterator over the vertices in the shortest path from `s` to `t`.
     */
    private Iterator<Integer> dijkstra(int s, int t) {
        double[] distance = new double[numVertices]; // Array to store the shortest distances from source `s` to all vertices

        // spSet[j] will be true if vertex `j` is included in the shortest
        // path tree or if the shortest distance from source `s` to `j` is finalized
        boolean[] spSet = new boolean[numVertices]; // Array to mark processed vertices

        // Array to store predecessors of each vertex in the shortest path
        int[] predecessor = new int[numVertices];

        // Initialize distances, processed vertices set, and predecessors
        for (int i = 0; i < numVertices; i++) {
            distance[i] = Double.POSITIVE_INFINITY; // All distances start as infinite
            spSet[i] = false;                       // No vertex has been processed yet
            predecessor[i] = -1;                    // No predecessor at the start
        }

        distance[s] = 0; // Distance to the source vertex is 0

        // Main loop to find the shortest path for all vertices
        for (int cnt = 0; cnt < numVertices - 1; cnt++) {
            // Find the vertex with the smallest distance that has not been processed yet
            int ux = minimumDistance(distance, spSet);
            spSet[ux] = true; // Mark this vertex as processed

            // Update distances and predecessors for neighbors of the selected vertex
            for (int vx = 0; vx < numVertices; vx++) {
                // Check if the vertex `vx` can be updated:
                // - Not yet processed
                // - There is an edge between `ux` and `vx`
                // - The distance to `ux` is not infinite
                // - The new distance through `ux` is smaller than the current distance to `vx`
                if (!spSet[vx] && adjMatrix[ux][vx] != Double.POSITIVE_INFINITY && distance[ux] != Double.POSITIVE_INFINITY && distance[ux] + adjMatrix[ux][vx] < distance[vx]) {
                    distance[vx] = distance[ux] + adjMatrix[ux][vx]; // Update the distance
                    predecessor[vx] = ux; // Update the predecessor of `vx`
                }
            }
        }

        // Reconstruct the shortest path from the destination `t` back to the source `s`
        ArrayUnorderedList<Integer> path = new ArrayUnorderedList<>(); // List to store the path
        for (int at = t; at != -1; at = predecessor[at]) {
            path.addToFront(at); // Add each vertex to the front of the path
        }

        return path.iterator(); // Return the path as an iterator
    }


}
