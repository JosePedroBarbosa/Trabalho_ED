package dataStructures.implementations;

import dataStructures.ADTS.GraphADT;
import dataStructures.exceptions.EmptyCollectionException;
import java.util.Iterator;

public class Graph<T> implements GraphADT<T> {
    protected static int DEFAULT_CAPACITY = 10;
    protected static int EXPANSION_FATORIAL = 2;

    protected T[] vertices;
    protected boolean[][] adjMatrix;
    protected int numVertices;

    public Graph() {
        this.vertices = (T[]) new Object[DEFAULT_CAPACITY];
        this.adjMatrix = new boolean[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
        this.numVertices = 0;
    }

    public Graph(int initialCapacity) {
        this.vertices = (T[]) new Object[initialCapacity];
        this.adjMatrix = new boolean[initialCapacity][initialCapacity];
        this.numVertices = 0;
    }

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
            adjMatrix[numVertices][i] = false;
            adjMatrix[i][numVertices] = false;
        }

        numVertices++;
    }

    protected void expandCapacity() {
        int newCapacity = numVertices * EXPANSION_FATORIAL;
        T[] newVertices = (T[]) new Object[newCapacity];

        for(int i = 0; i < numVertices; i++) {
            newVertices[i] = vertices[i];
        }
        this.vertices = newVertices;

        boolean[][] newAdjMatrix = new boolean[newCapacity][newCapacity];
        for (int i = 0; i < numVertices; i++) {
            for(int j = 0; j < numVertices; j++) {
                newAdjMatrix[i][j] = adjMatrix[i][j];
            }
        }
        this.adjMatrix = newAdjMatrix;
    }

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
            adjMatrix[numVertices][i] = false;
            adjMatrix[i][numVertices] = false;
        }
    }


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

    @Override
    public void addEdge(T vertex1, T vertex2) {
        addEdge(getIndex(vertex1), getIndex(vertex2));
    }

    private void addEdge(int indexVertex1, int indexVertex2) {
        if (!indexIsValid(indexVertex1) || !indexIsValid(indexVertex2)) {
            throw new IllegalArgumentException("Invalid vertex indices");
        }
        adjMatrix[indexVertex1][indexVertex2] = true;
        adjMatrix[indexVertex2][indexVertex1] = true;
    }

    @Override
    public void removeEdge(T vertex1, T vertex2) {
        removeEdge(getIndex(vertex1), getIndex(vertex2));
    }

    private void removeEdge(int indexVertex1, int indexVertex2) {
        adjMatrix[indexVertex1][indexVertex2] = false;
        adjMatrix[indexVertex2][indexVertex1] = false;
    }

    protected boolean indexIsValid(int index) {
        return (index < numVertices && index >= 0);
    }

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
                    if (adjMatrix[x][i] && !visited[i]) {
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
        } catch (EmptyCollectionException ex) {
            System.out.println(ex.getMessage());
        }

        return resultList.iterator();
    }

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
                    if (adjMatrix[current][i] && !visited[i]) {
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

    @Override
    public boolean isEmpty() {
        return this.numVertices == 0;
    }

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

    @Override
    public int size() {
        return this.numVertices;
    }

    public ArrayUnorderedList<T> getVertices(){
        ArrayUnorderedList<T> newVertices = new ArrayUnorderedList<>();

        for(int i = 0; i < numVertices; i++){
            newVertices.addToRear(vertices[i]);
        }
        return newVertices;
    }

    public boolean[][] getAdjMatrix(){
        return this.adjMatrix;
    }

    public void printGraph() {
        ArrayUnorderedList<T> vertices = this.getVertices();
        boolean[][] adjMatrix = this.getAdjMatrix();

        System.out.println("=== Graph Visualization ===");

        for (int i = 0; i < this.size(); i++) {
            if (vertices.getByIndex(i) != null) {
                System.out.printf("[%s]\n", vertices.getByIndex(i));

                String connections = "   ➯ ";
                boolean hasConnections = false;

                for (int j = 0; j < this.size(); j++) {
                    if (adjMatrix[i][j]) {
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