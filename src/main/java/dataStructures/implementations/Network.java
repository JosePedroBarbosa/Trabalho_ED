package dataStructures.implementations;

import dataStructures.ADTS.NetworkADT;

public class Network<T> extends Graph<T> implements NetworkADT<T> {
    protected double[][] adjMatrix;

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

    @Override
    public void addEdge(T vertex1, T vertex2, double weight) {
        int index1 = getIndex(vertex1);
        int index2 = getIndex(vertex2);

        if(indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = weight;
            adjMatrix[index2][index1] = weight;
        }
    }

    @Override
    public double shortestPathWeight(T vertex1, T vertex2) {
        int index1 = getIndex(vertex1);
        int index2 = getIndex(vertex2);

        if(!indexIsValid(index1) || !indexIsValid(index2)) {
            throw new IllegalArgumentException("Vertices are not valid.");
        }

        return dijkstra(index1, index2);
    }

    private double dijkstra(int index1, int index2) {
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
