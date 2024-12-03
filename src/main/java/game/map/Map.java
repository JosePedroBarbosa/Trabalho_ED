package game.map;

import dataStructures.implementations.Graph;
import game.interfaces.IMap;


public class Map implements IMap {
    Graph<Room> map = new Graph<>();

    @Override
    public void insertRoom(Room room) {
        map.addVertex(room);
    }

    @Override
    public void insertConnection(Room room1, Room room2) {
        map.addEdge(room1, room2);
    }

    public void printMap(){
        map.printGraph();
    }

    public Graph<Room> getMap() {
        return map;
    }
}