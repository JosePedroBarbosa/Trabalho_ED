package dataStructures.implementations;

import dataStructures.ADTS.GraphADT;
import dataStructures.exceptions.EmptyCollectionException;
import java.util.Iterator;


/**
 * A class that represents an undirected graph using an adjacency matrix.
 *
 * @param <T> the type of the vertices in the graph
 */
public class Graph<T> implements GraphADT<T> {
    protected static int DEFAULT_CAPACITY = 10;
    protected static int EXPANSION_FATORIAL = 2;

    protected T[] vertices;
    protected double[][] adjMatrix;
    protected int numVertices;

    /**
     * Creates an empty graph with a default capacity.
     */
    public Graph() {
        this.vertices = (T[]) new Object[DEFAULT_CAPACITY];
        this.adjMatrix = new double[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        this.numVertices = 0;
    }

    /**
     * Creates an empty graph with a specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the graph
     */
    public Graph(int initialCapacity) {
        this.vertices = (T[]) new Object[initialCapacity];
        this.adjMatrix = new double[initialCapacity][initialCapacity];
        this.numVertices = 0;
    }

    /**
     * Adds a vertex to the graph.
     *
     * @param vertex the vertex to be added
     * @throws IllegalArgumentException if the vertex is null
     */
    @Override
    public void addVertex(T vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("The Element Cant Be Null");
        }

        if (this.numVertices == this.vertices.length) {
            expandCapacity();
        }

        vertices[numVertices] = vertex;
        for (int i = 0; i <= numVertices; i++) {
            adjMatrix[numVertices][i] = Double.POSITIVE_INFINITY;
            adjMatrix[i][numVertices] = Double.POSITIVE_INFINITY;
        }

        numVertices++;
    }

    /**
     * Expands the capacity of the graph's vertices and adjacency matrix.
     */
    protected void expandCapacity() {
        int newCapacity = numVertices * EXPANSION_FATORIAL;
        T[] newVertices = (T[]) new Object[newCapacity];

        for(int i = 0; i < numVertices; i++) {
            newVertices[i] = vertices[i];
        }
        this.vertices = newVertices;

        double[][] newAdjMatrix = new double[newCapacity][newCapacity];
        for (int i = 0; i < numVertices; i++) {
            for(int j = 0; j < numVertices; j++) {
                newAdjMatrix[i][j] = adjMatrix[i][j];
            }
        }
        this.adjMatrix = newAdjMatrix;
    }

    /**
     * Removes a vertex from the graph.
     *
     * @param vertex the vertex to be removed
     * @throws IllegalArgumentException if the vertex is null
     */
    @Override
    public void removeVertex(T vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("The Element Cant Be Null");
        }
        for (int i = 0; i < numVertices; i++) {
            if (vertices[i].equals(vertex)) {
                removeVertex(i);
            }
        }
    }

    /**
     * Removes a vertex by its index in the graph.
     *
     * @param index the index of the vertex to be removed
     * @throws IllegalArgumentException if the index is invalid
     */
    private void removeVertex(int index) {
        if (!indexIsValid(index)) {
            throw new IllegalArgumentException("Invalid vertex index");
        }

        numVertices--;
        for (int i = index; i < numVertices; i++) {
            vertices[i] = vertices[i + 1];
        }
        vertices[numVertices] = null;

        for (int i = index; i < numVertices; i++) {
            for (int j = 0; j < numVertices + 1; j++) {
                adjMatrix[i][j] = adjMatrix[i + 1][j];
                adjMatrix[j][i] = adjMatrix[j][i + 1];
            }
        }

        for (int i = 0; i <= numVertices; i++) {
            adjMatrix[numVertices][i] = Double.POSITIVE_INFINITY;
            adjMatrix[i][numVertices] = Double.POSITIVE_INFINITY;
        }
    }

    /**
     * Returns the index of the given vertex.
     *
     * @param vertex the vertex whose index is to be found
     * @return the index of the vertex, or -1 if the vertex is not found
     */
    protected int getIndex(T vertex) {
        int index = -1;
        for (int i = 0; i < numVertices; i++) {
            if (vertices[i].equals(vertex)) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * Adds an edge between two vertices.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     */
    @Override
    public void addEdge(T vertex1, T vertex2) {
        addEdge(getIndex(vertex1), getIndex(vertex2));
    }

    /**
     * Adds an edge between two vertices by their indices.
     *
     * @param indexVertex1 the index of the first vertex
     * @param indexVertex2 the index of the second vertex
     * @throws IllegalArgumentException if the indices are invalid
     */
    private void addEdge(int indexVertex1, int indexVertex2) {
        if (!indexIsValid(indexVertex1) || !indexIsValid(indexVertex2)) {
            throw new IllegalArgumentException("Invalid vertex indices");
        }
        adjMatrix[indexVertex1][indexVertex2] = 0;
        adjMatrix[indexVertex2][indexVertex1] = 0;
    }

    /**
     * Removes an edge between two vertices.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     */
    @Override
    public void removeEdge(T vertex1, T vertex2) {
        removeEdge(getIndex(vertex1), getIndex(vertex2));
    }

    /**
     * Removes an edge between two vertices by their indices.
     *
     * @param indexVertex1 the index of the first vertex
     * @param indexVertex2 the index of the second vertex
     */
    private void removeEdge(int indexVertex1, int indexVertex2) {
        adjMatrix[indexVertex1][indexVertex2] = Double.POSITIVE_INFINITY;
        adjMatrix[indexVertex2][indexVertex1] = Double.POSITIVE_INFINITY;
    }

    /**
     * Checks if the given index is valid.
     *
     * @param index the index to be checked
     * @return true if the index is valid, false otherwise
     */
    protected boolean indexIsValid(int index) {
        return (index < numVertices && index >= 0);
    }

    /**
     * Performs a Breadth-First Search (BFS) starting from the specified vertex.
     *
     * @param startVertex the vertex to start the BFS from
     * @return an iterator for the BFS traversal
     */
    @Override
    public Iterator<T> iteratorBFS(T startVertex) {
        return iteratorBFS(getIndex(startVertex));
    }

    /**
     * Returns an iterator that performs a breadth first search traversal
     * starting at the given index.
     *
     * @param startIndex the index to begin the search from
     * @return an iterator that performs a breadth first traversal
     */
    private Iterator<T> iteratorBFS(int startIndex) {
        Integer x;
        LinkedQueue<Integer> traversalQueue = new LinkedQueue<Integer>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<T>();
        if (!indexIsValid(startIndex)) {
            return resultList.iterator();
        }
        boolean[] visited = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
        }
        traversalQueue.enqueue(startIndex);
        visited[startIndex] = true;

        try {
            while (!traversalQueue.isEmpty()) {
                x = traversalQueue.dequeue();
                resultList.addToRear(vertices[x]);
                /**
                 * Find all vertices adjacent to x that have not been visited
                 * and queue them up
                 */
                for (int i = 0; i < numVertices; i++) {
                    if (adjMatrix[x][i] < Double.POSITIVE_INFINITY && !visited[i]) {
                        traversalQueue.enqueue(i);
                        visited[i] = true;
                    }
                }
            }
        } catch (EmptyCollectionException ex) {
            System.out.println(ex.getMessage());
        }

        return resultList.iterator();
    }

    /**
     * Performs a Depth-First Search (DFS) starting from the specified vertex.
     *
     * @param startVertex the vertex to start the DFS from
     * @return an iterator for the DFS traversal
     */
    @Override
    public Iterator<T> iteratorDFS(T startVertex) {
        return iteratorDFS(getIndex(startVertex));
    }

    /**
     * Returns an iterator that performs a depth first search traversal starting
     * at the given index.
     *
     * @param startIndex the index to begin the search traversal from
     * @return an iterator that performs a depth first traversal
     */
    private Iterator<T> iteratorDFS(int startIndex) {
        Integer x;
        boolean found;
        LinkedStack<Integer> traversalStack = new LinkedStack<Integer>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<T>();
        boolean[] visited = new boolean[numVertices];
        if (!indexIsValid(startIndex)) {
            return resultList.iterator();
        }
        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
        }
        traversalStack.push(startIndex);
        resultList.addToRear(vertices[startIndex]);
        visited[startIndex] = true;

        try {
            while (!traversalStack.isEmpty()) {
                x = traversalStack.peek();
                found = false;
                /**
                 * Find a vertex adjacent to x that has not been visited and
                 * push it on the stack
                 */
                for (int i = 0; (i < numVertices) && !found; i++) {
                    if (adjMatrix[x][i] < Double.POSITIVE_INFINITY && !visited[i]) {
                        traversalStack.push(i);
                        resultList.addToRear(vertices[i]);
                        visited[i] = true;
                        found = true;
                    }
                }
                if (!found && !traversalStack.isEmpty()) {
                    traversalStack.pop();
                }
            }
        } catch (EmptyCollectionException ex) {
            System.out.println(ex.getMessage());
        }

        return resultList.iterator();
    }

    /**
     * Returns an iterator representing the shortest path from the start vertex to the target vertex.
     *
     * @param startVertex the start vertex
     * @param targetVertex the target vertex
     * @return an iterator for the shortest path between the start and target vertices
     */
    @Override
    public Iterator iteratorShortestPath(T startVertex, T targetVertex) {
        int startIndex = getIndex(startVertex);
        int targetIndex = getIndex(targetVertex);

        if (!indexIsValid(startIndex) || !indexIsValid(targetIndex)) {
            throw new IllegalArgumentException("Invalid Vertices");
        }

        if (startIndex == targetIndex) {
            ArrayUnorderedList<T> result = new ArrayUnorderedList<>();
            result.addToRear(startVertex);
            return result.iterator();
        }

        LinkedQueue<Integer> queue = new LinkedQueue<>();
        boolean[] visited = new boolean[numVertices];
        int[] path = new int[numVertices];
        for (int i = 0; i < numVertices; i++) {
            path[i] = -1;
        }

        queue.enqueue(startIndex);
        visited[startIndex] = true;

        boolean found = false;

        try {
            while (!queue.isEmpty() && !found) {
                int current = queue.dequeue();

                // Explore neighbors
                for (int i = 0; i < numVertices; i++) {
                    if (adjMatrix[current][i] < Double.POSITIVE_INFINITY && !visited[i]) {
                        queue.enqueue(i);
                        visited[i] = true;
                        path[i] = current;

                        // if the target is found
                        if (i == targetIndex) {
                            found = true;
                            break;
                        }
                    }
                }
            }
        } catch (EmptyCollectionException e) {
            System.out.println(e.getMessage());
        }

        // If no path exists
        if (!found) {
            return new ArrayUnorderedList<T>().iterator(); // Empty iterator
        }

        // Reconstruct the path
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();
        int current = targetIndex;
        while (current != -1) {
            resultList.addToFront(vertices[current]);
            current = path[current];
        }

        return resultList.iterator();
    }

    /**
     * Checks if the graph is empty.
     *
     * @return true if the graph is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return this.numVertices == 0;
    }

    /**
     * Checks if the graph is connected.
     *
     * @return true if the graph is connected, false otherwise
     */
    @Override
    public boolean isConnected() {
        if (isEmpty()) {
            return false;
        }

        Iterator it = iteratorBFS(0);
        int counter = 0;

        while (it.hasNext()) {
            it.next();
            counter++;
        }

        return counter == numVertices;
    }

    /**
     * Returns the number of vertices in the graph.
     *
     * @return the number of vertices in the graph
     */
    @Override
    public int size() {
        return this.numVertices;
    }

    /**
     * Returns a list of vertices in the graph.
     *
     * @return a list of vertices in the graph
     */
    public ArrayUnorderedList<T> getVertices(){
        ArrayUnorderedList<T> newVertices = new ArrayUnorderedList<>();

        for(int i = 0; i < numVertices; i++){
            newVertices.addToRear(vertices[i]);
        }
        return newVertices;
    }

    /**
     * Returns the adjacency matrix of the graph.
     *
     * @return the adjacency matrix
     */
    public double[][] getAdjMatrix(){
        return this.adjMatrix;
    }

    /**
     * Prints a visualization of the graph's vertices and their connections.
     */
    public void printGraph() {
        ArrayUnorderedList<T> vertices = this.getVertices();
        double[][] adjMatrix = this.getAdjMatrix();

        System.out.println("=== Graph Visualization ===");

        for (int i = 0; i < this.size(); i++) {
            if (vertices.getByIndex(i) != null) {
                System.out.printf("[%s]\n", vertices.getByIndex(i));

                String connections = "   ➯ ";
                boolean hasConnections = false;

                for (int j = 0; j < this.size(); j++) {
                    if (adjMatrix[i][j] != Double.POSITIVE_INFINITY) {
                        connections += vertices.getByIndex(j) + "   ";
                        hasConnections = true;
                    }
                }

                if (hasConnections) {
                    System.out.println(connections.trim());
                } else {
                    System.out.println("   ➯ None");
                }

                System.out.println();
            }
        }

        System.out.println("===========================");
    }

    /**
     * Returns a string representation of the graph, including the adjacency matrix and vertex values.
     *
     * @return a string representation of the graph
     */
    @Override
    public String toString() {
        if (numVertices == 0) {
            return "Graph is empty";
        }

        String result = "";

        result += "Adjacency Matrix\n";
        result += "----------------\n";
        result += "index\t";

        for (int i = 0; i < numVertices; i++) {
            result += "" + i;
            if (i < 10) {
                result += " ";
            }
        }
        result += "\n\n";

        for (int i = 0; i < numVertices; i++) {
            result += "" + i + "\t";

            for (int j = 0; j < numVertices; j++) {
                result += "" + adjMatrix[i][j] + " ";

            }
            result += "\n";
        }

        result += "\n\nVertex Values";
        result += "\n-------------\n";
        result += "index\tvalue\n\n";

        for (int i = 0; i < numVertices; i++) {
            result += "" + i + "\t";
            result += vertices[i].toString() + "\n";
        }
        result += "\n";
        return result;
    }

    /**
     * Retrieves the neighbors of a specified vertex in the graph.
     * Neighbors are defined as vertices that are directly connected to the specified vertex via an edge.
     *
     * @param element the vertex whose neighbors are to be found
     * @return an ArrayUnorderedList containing the neighbors of the specified vertex
     */
    public ArrayUnorderedList<T> getNeighbours(T element){
        int elementIndex = getIndex(element);
        ArrayUnorderedList<T> neighbours = new ArrayUnorderedList<>();

        if (!indexIsValid(elementIndex)) {
            throw new IllegalArgumentException("The vertex is not in the graph.");
        }

        for(int i = 0; i < numVertices; i++){
            if(adjMatrix[elementIndex][i] < Double.POSITIVE_INFINITY){
                neighbours.addToFront(vertices[i]);
            }
        }

        return neighbours;
    }
}