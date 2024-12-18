package game.interfaces;

import dataStructures.implementations.Graph;
import dataStructures.implementations.Network;
import game.map.Room;

/**
 * The IMap interface defines the basic contract for the map in the game.
 */
public interface IMap {
    /**
     * Inserts a new room into the map.
     *
     * @param room The room to be added to the map.
     */
    public void insertRoom(Room room);

    /**
     * Creates a connection between two rooms, indicating they are adjacent to each other.
     *
     * @param room1 The first room in the connection.
     * @param room2 The second room in the connection.
     */
    public void insertConnection(Room room1, Room room2);

    /**
     * Prints a visual or textual representation of the map, showing the rooms and their connections.
     */
    public void printMap();

    /**
     * Retrieves the network representation of the map.
     *
     * @return The network representing the map.
     */
    public Network<Room> getMap();
}