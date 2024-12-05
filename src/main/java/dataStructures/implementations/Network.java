package dataStructures.implementations;

import dataStructures.ADTS.NetworkADT;

/**
 * Represents a network (graph with weighted edges) that extends the basic graph structure.
 *
 * @param <T> the type of elements stored in the network
 */
public class Network<T> extends Graph<T> implements NetworkADT<T> {
    /** The adjacency matrix storing edge weights between vertices. */
    protected double[][] adjMatrix;

    /**
     * Constructs an empty network with the default capacity.
     * Initializes the adjacency matrix, setting diagonal elements to 0
     * (no cost to itself) and all other elements to positive infinity.
     */
    public Network(){
        super();
        this.adjMatrix = new double[DEFAULT_CAPACITY][DEFAULT_CAPACITY];

        for (int i = 0; i < DEFAULT_CAPACITY; i++) {
            for (int j = 0; j < DEFAULT_CAPACITY; j++) {
                if(i == j){
                    adjMatrix[i][j] = 0;
                }else {
                    adjMatrix[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }
    }

    /**
     * Adds a weighted, undirected edge between two vertices in the network.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     * @param weight  the weight of the edge connecting the vertices
     */
    @Override
    public void addEdge(T vertex1, T vertex2, double weight) {
        int index1 = getIndex(vertex1);
        int index2 = getIndex(vertex2);

        if(indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = weight;
            adjMatrix[index2][index1] = weight;
        }
    }

    /**
     * Calculates the shortest path weight between two vertices using Dijkstra's algorithm.
     *
     * @param vertex1 the starting vertex
     * @param vertex2 the target vertex
     * @return the weight of the shortest path between the vertices
     * @throws IllegalArgumentException if either vertex is invalid
     */
    @Override
    public double shortestPathWeight(T vertex1, T vertex2) {
        int index1 = getIndex(vertex1);
        int index2 = getIndex(vertex2);

        if(!indexIsValid(index1) || !indexIsValid(index2)) {
            throw new IllegalArgumentException("Vertices are not valid.");
        }

        return shortestPathWeight(index1, index2);
    }

    /**
     * Implements Dijkstra's algorithm to find the shortest path from a source vertex
     * to a target vertex in the network.
     *
     * @param index1 the index of the source vertex
     * @param index2 the index of the target vertex
     * @return the shortest path weight between the source and target vertices
     */
    private double shortestPathWeight(int index1, int index2) {
        double[] dist = new double[numVertices];
        boolean[] visited = new boolean[numVertices];
        int[] predecessor = new int[numVertices];

        for (int i = 0; i < numVertices; i++) {
            dist[i] = Double.POSITIVE_INFINITY;
            predecessor[i] = -1;
        }
        dist[index1] = 0;

        for (int i = 0; i < numVertices; i++) {
            int u = minDistance(dist, visited);
            visited[u] = true;

            for (int v = 0; v < numVertices; v++) {
                if (!visited[v] && adjMatrix[u][v] != Double.POSITIVE_INFINITY && dist[u] + adjMatrix[u][v] < dist[v]) {
                    dist[v] = dist[u] + adjMatrix[u][v];
                    predecessor[v] = u;
                }
            }
        }

        return dist[index2];
    }

    /**
     * Finds the vertex with the minimum distance value from the set of vertices
     * that have not been visited yet.
     *
     * @param dist    the array containing distances from the source vertex
     * @param visited the array indicating whether a vertex has been visited
     * @return the index of the vertex with the minimum distance
     */
    private int minDistance(double[] dist, boolean[] visited) {
        double min = Double.POSITIVE_INFINITY;
        int minIndex = -1;

        for (int i = 0; i < dist.length; i++) {
            if (!visited[i] && dist[i] < min) {
                min = dist[i];
                minIndex = i;
            }
        }

        return minIndex;
    }
}
