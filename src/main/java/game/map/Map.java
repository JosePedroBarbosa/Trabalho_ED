package game.map;

import dataStructures.implementations.Graph;
import game.interfaces.IMap;

/**
 * Represents a map of the game, consisting of interconnected rooms.
 * This class uses a graph data structure to represent rooms as vertices
 * and connections between rooms as edges. It provides methods to add rooms and connections,
 * as well as functionality to retrieve or print the map.
 */
public class Map implements IMap {
    /**
     * The graph representation of the map.
     */
    protected Graph<Room> map = new Graph<>();

    /**
     * Inserts a room into the map as a vertex.
     *
     * @param room The room to be added as a vertex to the map.
     */
    @Override
    public void insertRoom(Room room) {
        map.addVertex(room);
    }

    /**
     * Inserts a connection (edge) between two rooms in the map.
     *
     * @param room1 The first room to connect.
     * @param room2 The second room to connect.
     */
    @Override
    public void insertConnection(Room room1, Room room2) {
        map.addEdge(room1, room2);
    }

    /**
     * Prints the map to the console, displaying all rooms and their connections.
     */
    public void printMap(){
        map.printGraph();
    }

    /**
     * Retrieves the graph representation of the map.
     *
     * @return The graph representing the map.
     */
    public Graph<Room> getMap() {
        return map;
    }
}