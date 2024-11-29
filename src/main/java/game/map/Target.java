package game.map;

import game.map.Room;
import game.interfaces.ITarget;

public class Target implements ITarget {
    private Room currentRoom;
    private String type;

    public Target(Room currentRoom, String type){
        this.currentRoom = currentRoom;
        this.type = type;
    }

    public void setType(String type){
        if(type == null){
            throw new IllegalArgumentException("Target type cannot be null");
        }
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public Room getCurrentRoom(){
        return currentRoom;
    }

    public void setCurrentRoom(Room currentRoom){
        if(currentRoom == null){
            throw new IllegalArgumentException("Room cannot be null.");
        }
        this.currentRoom = currentRoom;
    }
}
