package dataStructures.implementations;

import dataStructures.ADTS.GraphADT;
import dataStructures.exceptions.EmptyCollectionException;
import java.util.Iterator;

/**
 * Graph represents an adjacency matrix implementation of a graph.
 *
 * @param <T> the type of elements held in this graph
 */
public class Graph<T> implements GraphADT<T> {
    protected final int DEFAULT_CAPACITY = 10; // Default initial capacity
    protected int numVertices; // number of vertices in the graph
    protected boolean[][] adjMatrix; // adjacency matrix
    protected T[] vertices; // values of vertices
    protected static int EXPANSION_FATORIAL = 2; // Expansion factor for dynamic resizing

    /**
     * Creates an empty graph.
     */
    public Graph() {
        numVertices = 0;
        this.adjMatrix = new boolean[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        this.vertices = (T[]) (new Object[DEFAULT_CAPACITY]);
    }

    /**
     * Creates a graph with a specific initial capacity.
     *
     * @param initialCapacity the initial number of vertices the graph can hold
     */
    public Graph(int initialCapacity) {
        numVertices = 0;
        this.adjMatrix = new boolean[initialCapacity][initialCapacity];
        this.vertices = (T[]) (new Object[initialCapacity]);
    }

    /**
     * Adds a vertex to the graph, expanding the capacity of the graph
     * if necessary. It also associates an object with the vertex.
     *
     * @param vertex the vertex to add to the graph
     */
    public void addVertex (T vertex) {
        if (numVertices == vertices.length){
            expandCapacity();
        }

        vertices[numVertices] = vertex;
        for (int i = 0; i <= numVertices; i++) {
            adjMatrix[numVertices][i] = false;
            adjMatrix[i][numVertices] = false;
        }
        numVertices++;
    }

    /**
     * Dynamically expands the capacity of the graph by a predefined factor.
     */
    protected void expandCapacity() {
        int newCapacity = numVertices * EXPANSION_FATORIAL;
        T[] newVertices = (T[]) (new Object[newCapacity]);

        for (int i = 0; i < numVertices; i++) {
            newVertices[i] = vertices[i];
        }

        this.vertices = newVertices;

        boolean[][] newAdjMatrix = new boolean[newCapacity][newCapacity];
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                newAdjMatrix[i][j] = adjMatrix[i][j];
            }
        }
        this.adjMatrix = newAdjMatrix;
    }

    /**
     * Removes a vertex from the graph by its value.
     *
     * @param vertex the vertex to remove
     * @throws IllegalArgumentException if the vertex is not found
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
     * Removes a vertex by its index, updating all references in the adjacency matrix.
     *
     * @param index the index of the vertex to remove
     */
    public void removeVertex(int index) {
        if (!indexIsValid(index)) {
            throw new IllegalArgumentException("Invalid vertex index");
        }

        for(int i = index; i < numVertices - 1; i++) {
            vertices[i] = vertices[i + 1];
        }

        vertices[numVertices - 1] = null;

        for (int i = index; i < numVertices - 1; i++) {
            for (int j = 0; j < numVertices; j++) {
                adjMatrix[i][j] = adjMatrix[i + 1][j];
                adjMatrix[j][i] = adjMatrix[j][i + 1];
            }
        }

        numVertices--;
    }

    /**
     * Returns the index of a given vertex.
     *
     * @param vertex the vertex to find
     * @return the index of the vertex, or -1 if not found
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
     * Inserts an edge between two vertices of the graph.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     */
    @Override
    public void addEdge(T vertex1, T vertex2) {
        addEdge(getIndex(vertex1), getIndex(vertex2));
    }

    /**
     * Inserts an edge between two vertices of the graph.
     *
     * @param index1 the first index
     * @param index2 the second index
     */
    public void addEdge (int index1, int index2) {
        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = true;
            adjMatrix[index2][index1] = true;
        }
    }

    /**
     * Removes an edge between two vertices of the graph.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     */
    @Override
    public void removeEdge(T vertex1, T vertex2) {
        removeEdge(getIndex(vertex1), getIndex(vertex2));
    }

    /**
     * Removes an edge between two vertices of the graph.
     *
     * @param indexVertex1 the first vertex
     * @param indexVertex2 the second vertex
     */
    public void removeEdge(int indexVertex1, int indexVertex2) {
        adjMatrix[indexVertex1][indexVertex2] = false;
        adjMatrix[indexVertex2][indexVertex1] = false;
    }

    /**
     * Checks if an index is valid for the current graph.
     *
     * @param index the index to check
     * @return true if the index is valid, false otherwise
     */
    protected boolean indexIsValid(int index) {
        return (index < numVertices && index >= 0);
    }

    /**
     * Returns an iterator for a Breadth-First Search (BFS) traversal
     * starting from a specified vertex.
     *
     * @param startVertex the vertex to begin traversal
     * @return an iterator for BFS traversal
     */
    @Override
    public Iterator<T> iteratorBFS(T startVertex) {
        return iteratorBFS(getIndex(startVertex));
    }

    /**
     * Returns an iterator that performs a breadth first search
     * traversal starting at the given index.
     *
     * @param startIndex the index to begin the search from
     * @return an iterator that performs a breadth first traversal
     */
    public Iterator<T> iteratorBFS(int startIndex) {
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

        while (!traversalQueue.isEmpty()) {
            x = traversalQueue.dequeue();
            resultList.addToRear(vertices[x]);

            /** Find all vertices adjacent to x that have
             not been visited and queue them up */
            for (int i = 0; i < numVertices; i++) {
                if (adjMatrix[x][i] && !visited[i]) {
                    traversalQueue.enqueue(i);
                    visited[i] = true;
                }
            }
        }
        return resultList.iterator();
    }

    /**
     * Returns an iterator that performs a depth-first search (DFS)
     * starting from the given vertex.
     *
     * @param startVertex the starting vertex for the DFS traversal
     * @return an iterator that performs a DFS traversal
     */
    @Override
    public Iterator<T> iteratorDFS(T startVertex) {
        return iteratorDFS(getIndex(startVertex));
    }

    /**
     * Returns an iterator that performs a depth first search
     * traversal starting at the given index.
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


        while (!traversalStack.isEmpty()) {
            x = traversalStack.peek();
            found = false;

            /** Find a vertex adjacent to x that has not been visited
             and push it on the stack */
            for (int i = 0; (i < numVertices) && !found; i++) {
                if (adjMatrix[x][i] && !visited[i]) {
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
        return resultList.iterator();
    }

    @Override
    public Iterator<T> iteratorShortestPath(T startVertex, T targetVertex) {
        return iteratorShortestPath(getIndex(startVertex), getIndex(targetVertex));
    }

    public Iterator<T> iteratorShortestPath(int startIndex, int targetIndex) {
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();
        if(!indexIsValid(startIndex) || !indexIsValid(targetIndex)) {
            return resultList.iterator();
        }

        Iterator<Integer> it = iteratorShortestPathIndices(startIndex, targetIndex);

        while(it.hasNext()) {
            resultList.addToRear(vertices[it.next()]);
        }

        return resultList.iterator();
    }

    protected Iterator<Integer> iteratorShortestPathIndices(int startIndex, int targetIndex){
        int index = startIndex;
        int[] pathLength = new int[numVertices];
        int[] predecessor = new int[numVertices];
        LinkedQueue<Integer> traversalQueue = new LinkedQueue<>();
        ArrayUnorderedList<Integer> resultList = new ArrayUnorderedList<>();

        if(!indexIsValid(startIndex) || !indexIsValid(targetIndex) || (startIndex == targetIndex)) {
            return resultList.iterator();
        }

        boolean[] visited = new boolean[numVertices];
        for(int i = 0; i < numVertices; i++) {
            visited[i] = false;
        }

        traversalQueue.enqueue(startIndex);
        visited[startIndex] = true;
        pathLength[startIndex] = 0;
        predecessor[startIndex] = -1;

        while(!traversalQueue.isEmpty() && (index != targetIndex)){
            index = traversalQueue.dequeue();

            for (int i = 0; i < numVertices; i++) {
                if (adjMatrix[index][i] && !visited[i]) {
                    pathLength[i] = pathLength[index] + 1;
                    predecessor[i] = index;
                    traversalQueue.enqueue(i);
                    visited[i] = true;
                }
            }
        }
        if(index != targetIndex){
            return resultList.iterator();
        }

        LinkedStack<Integer> stack = new LinkedStack<>();
        index = targetIndex;
        stack.push(index);

        do {
            index = predecessor[index];
            stack.push(index);
        }while(index != startIndex);

        while(!stack.isEmpty()){
            resultList.addToRear(stack.pop());
        }

        return resultList.iterator();
    }

    /**
     * Checks if the graph is empty.
     *
     * @return true if the graph has no vertices, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return this.numVertices == 0;
    }

    /**
     * Checks if the graph is connected. A graph is connected if all vertices
     * are reachable from any starting vertex.
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
     * @return the size of the graph
     */
    @Override
    public int size() {
        return this.numVertices;
    }

    /**
     * Returns an array of the vertices in the graph.
     *
     * @return an array containing all vertices
     */
    public T[] getVertices(){
        T[] newVertices = (T[]) new Object[numVertices];

        for(int i = 0; i < numVertices; i++){
            newVertices[i] = vertices[i];
        }

        return newVertices;
    }

    /**
     * Returns the adjacency matrix representing the graph.
     *
     * @return a boolean array representing the adjacency matrix
     */
    public boolean[][] getAdjMatrix(){
        return this.adjMatrix;
    }

    /**
     * Prints a visual representation of the graph, including the vertices
     * and their connections.
     */
    public void printGraph() {
        T[] vertices = this.getVertices();
        boolean[][] adjMatrix = this.getAdjMatrix();

        System.out.println("=== Graph Visualization ===");

        for (int i = 0; i < this.size(); i++) {
            if (vertices[i] != null) {
                System.out.printf("[%s]\n", vertices[i]);

                String connections = "   ➯ ";
                boolean hasConnections = false;

                for (int j = 0; j < this.size(); j++) {
                    if (adjMatrix[i][j]) {
                        connections += vertices[j] + "   ";
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
     * Returns a string representation of the graph, including the adjacency
     * matrix and vertex values.
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
     * Returns a list of direct neighbors of a given vertex.
     *
     * @param element the vertex whose neighbors are to be found
     * @return an unordered list of neighboring vertices
     */
    public ArrayUnorderedList<T> getNeighbours(T element){
        int elementIndex = getIndex(element);
        ArrayUnorderedList<T> neighbours = new ArrayUnorderedList<>();

        for(int i = 0; i < numVertices; i++){
            if(adjMatrix[elementIndex][i]){
                neighbours.addToFront(vertices[i]);
            }
        }

        return neighbours;
    }
}