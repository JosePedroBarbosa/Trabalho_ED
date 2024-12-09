package dataStructures.implementations;

import dataStructures.ADTS.NetworkADT;

import java.util.Iterator;

/**
 * Represents a weighted undirected network.
 *
 * @param <T> the type of the vertices in the network
 */
public class Network<T> extends Graph<T> implements NetworkADT<T> {
    protected double[][] adjMatrix;

    /**
     * Constructs a new empty network with default capacity.
     */
    public Network(){
        numVertices = 0;
        this.adjMatrix = new double[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        this.vertices = (T[]) (new Object[DEFAULT_CAPACITY]);
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
        super.addEdge(vertex1, vertex2); //add in the graph matrix
        this.addEdge(getIndex(vertex1), getIndex(vertex2), weight);
    }

    /**
     * Adds an edge between two vertices in the network given their indices and weight.
     *
     * @param index1 the index of the first vertex
     * @param index2 the index of the second vertex
     * @param weight the weight of the edge
     */
    public void addEdge(int index1, int index2, double weight) {
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
     * Removes the edge between two vertices.
     *
     * @param vertex1 the first vertex
     * @param vertex2 the second vertex
     */
    @Override
    public void removeEdge(T vertex1, T vertex2) {
        removeEdge(getIndex(vertex1), getIndex(vertex2));
    }

    /**
     * Removes the edge between two vertices given their indices.
     *
     * @param index1 the index of the first vertex
     * @param index2 the index of the second vertex
     */
    public void removeEdge(int index1, int index2) {
        if(indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = Double.POSITIVE_INFINITY;
            adjMatrix[index2][index1] = Double.POSITIVE_INFINITY;
        }
    }

    /**
     * Adds a new vertex to the network.
     * If the network is at max capacity, it will expand.
     *
     * @param vertex the vertex to add
     * @throws IllegalArgumentException if the vertex is null
     */
    @Override
    public void addVertex(T vertex){
        if (vertex == null) {
            throw new IllegalArgumentException("The Element Cant Be Null");
        }

        if (this.numVertices == this.vertices.length) {
            expandNetworkCapacity();
        }

        for (int i = 0; i <= numVertices; i++) {
            adjMatrix[numVertices][i] = Double.POSITIVE_INFINITY;
            adjMatrix[i][numVertices] = Double.POSITIVE_INFINITY;
        }
        super.addVertex(vertex); //add vertex in the graph
    }

    /**
     * Expands the capacity of the network by a factor of EXPANSION_FATORIAL.
     */
    public void expandNetworkCapacity(){
        int newCapacity = numVertices * EXPANSION_FATORIAL;
        double[][] newAdjMatrix = new double[newCapacity][newCapacity];
        for (int i = 0; i < numVertices; i++) {
            for(int j = 0; j < numVertices; j++) {
                newAdjMatrix[i][j] = adjMatrix[i][j];
            }
        }

        this.adjMatrix = newAdjMatrix;
    }

    /**
     * Returns an iterator for depth-first search (DFS) traversal starting from the given vertex.
     *
     * @param startVertex the starting vertex
     * @return an iterator for the DFS traversal
     */
    @Override
    public Iterator<T> iteratorDFS(T startVertex){
        return iteratorDFS(getIndex(startVertex));
    }

    /**
     * Returns an iterator for depth-first search (DFS) traversal starting from the given vertex index.
     *
     * @param startIndex the index of the starting vertex
     * @return an iterator for the DFS traversal
     */
    public Iterator<T> iteratorDFS(int startIndex){
        Integer x;
        boolean found;
        LinkedStack<Integer> traversalStack = new LinkedStack<>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();
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

        while(!traversalStack.isEmpty()){
            x = traversalStack.peek();
            found = false;

            /** Find a vertex adjacent to x that has not been visited
             and push it on the stack */
            for(int i = 0; i < numVertices && !found; i++){
                if ((adjMatrix[x][i] < Double.POSITIVE_INFINITY) && !visited[i]) {
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

    /**
     * Returns an iterator for breadth-first search (BFS) traversal starting from the given vertex.
     *
     * @param startVertex the starting vertex
     * @return an iterator for the BFS traversal
     */
    @Override
    public Iterator<T> iteratorBFS(T startVertex) {
        return iteratorBFS(getIndex(startVertex));
    }

    /**
     * Returns an iterator for breadth-first search (BFS) traversal starting from the given vertex index.
     *
     * @param startIndex the index of the starting vertex
     * @return an iterator for the BFS traversal
     */
    public Iterator<T> iteratorBFS(int startIndex) {
        Integer x;
        LinkedQueue<Integer> traversalQueue = new LinkedQueue<>();
        ArrayUnorderedList<T> resultList = new ArrayUnorderedList<>();

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

            //Find all vertices adjacent to x that have not been visited and
            //queue them up
            for (int i = 0; i < numVertices; i++) {
                if ((adjMatrix[x][i] < Double.POSITIVE_INFINITY) && !visited[i]) {
                    traversalQueue.enqueue(i);
                    visited[i] = true;
                }
            }
        }
        return resultList.iterator();
    }

    /**
     * Finds the shortest path between two vertices and returns the path as a list of vertices.
     *
     * @param startVertex the starting vertex
     * @param endVertex the destination vertex
     * @return an ArrayUnorderedList of vertices representing the shortest path
     */
    public ArrayUnorderedList<T> shortestPath(T startVertex, T endVertex) {
        Iterator<Integer> it = iteratorShortestPathIndices(getIndex(startVertex), getIndex(endVertex));
        ArrayUnorderedList<T> path = new ArrayUnorderedList<>();

        while (it.hasNext()) {
            path.addToRear(vertices[it.next()]);
        }

        return path;
    }

    /**
     * Returns an iterator for the shortest path between the specified vertices.
     *
     * @param startVertex The starting vertex.
     * @param endVertex The destination vertex.
     * @return An iterator for the shortest path between the provided vertices.
     */
    @Override
    public Iterator<T> iteratorShortestPath(T startVertex, T endVertex) {
        return shortestPath(startVertex, endVertex).iterator();
    }

    /**
     * The shortest path between two vertices given their indices, returning
     * an iterator with the indices of the vertices in the shortest path.
     *
     * @param startIndex The index of the starting vertex.
     * @param targetIndex The index of the destination vertex.
     * @return An iterator containing the indices of the vertices in the shortest path.
     */
    protected Iterator<Integer> iteratorShortestPathIndices(int startIndex, int targetIndex) {
        int index;
        double weight;
        int[] predecessor = new int[numVertices];
        LinkedHeap<Double> traversalMinHeap = new LinkedHeap<>();
        ArrayUnorderedList<Integer> resultList = new ArrayUnorderedList<>();
        LinkedStack<Integer> stack = new LinkedStack<>();

        int[] pathIndex = new int[numVertices];
        double[] pathWeight = new double[numVertices];
        for (int i = 0; i < numVertices; i++) {
            pathWeight[i] = Double.POSITIVE_INFINITY;
        }

        boolean[] visited = new boolean[numVertices];
        for (int i = 0; i < numVertices; i++) {
            visited[i] = false;
        }

        if (!indexIsValid(startIndex) || !indexIsValid(targetIndex) || (startIndex == targetIndex) || isEmpty()) {
            return resultList.iterator();
        }

        pathWeight[startIndex] = 0;
        predecessor[startIndex] = -1;
        visited[startIndex] = true;
        weight = 0;

        //Update the pathWeight for each vertex except the startVertex. Notice
        //that all vertices not adjacent to the startVertex will have a
        //pathWeight of infinity for now
        for (int i = 0; i < numVertices; i++) {
            if (!visited[i]) {
                pathWeight[i] = pathWeight[startIndex] + adjMatrix[startIndex][i];
                predecessor[i] = startIndex;
                traversalMinHeap.addElement(pathWeight[i]);
            }
        }

        do {
            weight = traversalMinHeap.removeMin();
            traversalMinHeap.removeAllElements();
            if (weight == Double.POSITIVE_INFINITY){
                return resultList.iterator();
            } else {
                index = getIndexOfAdjVertexWithWeightOf(visited, pathWeight, weight);
                visited[index] = true;
            }

            //Update the pathWeight for each vertex that has not been
            //visited and is adjacent to the last vertex that was visited.
            //Also, add each unvisited vertex to the heap
            for (int i = 0; i < numVertices; i++) {
                if (!visited[i]) {
                    if ((adjMatrix[index][i] < Double.POSITIVE_INFINITY) && (pathWeight[index] + adjMatrix[index][i]) < pathWeight[i]) {
                        pathWeight[i] = pathWeight[index] + adjMatrix[index][i];
                        predecessor[i] = index;
                    }
                    traversalMinHeap.addElement(pathWeight[i]);
                }
            }
        } while (!traversalMinHeap.isEmpty() && !visited[targetIndex]);

        index = targetIndex;
        stack.push(index);
        do {
            index = predecessor[index];
            stack.push(index);
        } while (index != startIndex);

        while (!stack.isEmpty()) {
            resultList.addToRear((stack.pop()));
        }

        return resultList.iterator();
    }

    /**
     * Finds the index of an adjacent vertex that has the specified weight,
     * and has been visited.
     *
     * @param visited A boolean array indicating whether each vertex has been visited.
     * @param pathWeight An array containing the path weights for each vertex.
     * @param weight The weight to be found.
     * @return The index of the adjacent vertex with the specified weight, or -1 if none is found.
     */
    protected int getIndexOfAdjVertexWithWeightOf(boolean[] visited, double[] pathWeight, double weight) {
        for(int i = 0; i < numVertices; i++){
            if((pathWeight[i] == weight) && !visited[i]){
                for(int j = 0; j < numVertices; j++){
                    if((adjMatrix[i][j] < Double.POSITIVE_INFINITY) && visited[j]){
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * Returns the Minimum Spanning Tree (MST) of the graph.
     * A new graph representing the MST is created and returned.
     *
     * @return A new graph representing the Minimum Spanning Tree (MST) of the graph.
     */
    public Network<T> mstNetwork(){
        int x, y;
        int index;
        double weight;
        int[] edge = new int[2];
        LinkedHeap<Double> minHeap = new LinkedHeap<Double>();
        Network<T> resultGraph = new Network<T>();

        if(isEmpty() || !isConnected()){
            return resultGraph;
        }

        resultGraph.adjMatrix = new double[numVertices][numVertices];
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j <  numVertices; j++) {
                resultGraph.adjMatrix[i][j] = Double.POSITIVE_INFINITY;
            }
            resultGraph.vertices = (T[]) (new Object[numVertices]);
        }

        boolean[] visited = new boolean[numVertices];
        for(int i = 0; i < numVertices; i++){
            visited[i] = false;
        }
        edge[0] = 0;
        resultGraph.vertices[0] = this.vertices[0];
        resultGraph.numVertices++;
        visited[0] = true;

        /**
         * Add all edges, which are adjacent to the starting vertex, to the heap
         */
        for(int i = 0; i < numVertices; i++){
            minHeap.addElement(adjMatrix[0][i]);
        }

        while((resultGraph.size() < this.size()) && !minHeap.isEmpty()){
            /**
             * Get the edge with the smallest weight that has exactly one vertex
             * already in the resultGraph
             */
            do {
                weight = minHeap.removeMin();
                edge = getEdgeWithWeightOf(weight, visited);
            } while(!indexIsValid(edge[0]) || !indexIsValid(edge[1]));
        }

        x = edge[0];
        y = edge[1];
        if (!visited[x]) {
            index = x;
        } else {
            index = y;
        }

        /**
         * Add the new edge and vertex to the resultGraph
         */
        resultGraph.vertices[index] = this.vertices[index];
        visited[index] = true;
        resultGraph.numVertices++;

        resultGraph.adjMatrix[x][y] = this.adjMatrix[x][y];
        resultGraph.adjMatrix[y][x] = this.adjMatrix[y][x];

        /**
         * Add all edges, that are adjacent to the newly added vertex, to
         * the heap
         */
        for (int i = 0; i < numVertices; i++) {
            if (!visited[i] && (this.adjMatrix[i][index] < Double.POSITIVE_INFINITY)) {
                edge[0] = index;
                edge[1] = i;
                minHeap.addElement(adjMatrix[index][i]);
            }
        }
        return resultGraph;
    }

    /**
     * Finds the edge with the specified weight, where one vertex has been visited and the other has not.
     *
     * @param weight The weight of the edge to be found.
     * @param visited A boolean array indicating whether each vertex has been visited.
     * @return An array with the indices of the vertices connected by the edge with the specified weight.
     */
    protected int[] getEdgeWithWeightOf(double weight, boolean[] visited) {
        int[] edge = new int[2];
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if ((adjMatrix[i][j] == weight) && (visited[i] ^ visited[j])) {
                    edge[0] = i;
                    edge[1] = j;
                    return edge;
                }
            }
        }

        edge[0] = -1;
        edge[1] = -1;
        return edge;
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
        Iterator<Integer> it = iteratorShortestPathIndices(startIndex, targetIndex);

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

}