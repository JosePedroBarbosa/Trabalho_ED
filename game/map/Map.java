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
    public void insterConnection(Room room1, Room room2) {
        map.addEdge(room1, room2);
    }

    @Override
    public void selectTargetRoom(Room targetRoom) {

    }

    @Override
    public void setEntraces(Room[] entraces) {

    }



}
