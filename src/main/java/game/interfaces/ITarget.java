package game.interfaces;

import game.map.Room;

/**
 * The ITarget interface defines the basic contract for any target in the game.
 */
public interface ITarget {
    
    public void setType(String type);

    public String getType();

    public Room getCurrentRoom();

    public void setCurrentRoom(Room currentRoom);
}
