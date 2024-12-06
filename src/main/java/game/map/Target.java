package game.map;

import game.interfaces.ITarget;

/**
 * Represents a target in the game, which the player must interact with.
 * A target has a type, a current location (room), and a state indicating whether it has been picked up.
 */
public class Target implements ITarget {
    /**
     * The current room where the target is located.
     */
    private Room currentRoom;

    /**
     * The type of the target.
     */
    private String type;

    /**
     * Indicates whether the target has been picked up by the player.
     */
    private boolean pickedUp;

    /**
     * Constructs a Target with a specified initial room and type.
     *
     * @param currentRoom The room where the target is initially located.
     * @param type The type of the target.
     */
    public Target(Room currentRoom, String type){
        this.currentRoom = currentRoom;
        this.type = type;
    }

    /**
     * Sets the type of the target.
     *
     * @param type The new type of the target.
     * @throws IllegalArgumentException if the type is null.
     */
    @Override
    public void setType(String type){
        if(type == null){
            throw new IllegalArgumentException("Target type cannot be null");
        }
        this.type = type;
    }

    /**
     * Retrieves the type of the target.
     *
     * @return The type of the target.
     */
    @Override
    public String getType(){
        return type;
    }

    /**
     * Retrieves the current room where the target is located.
     *
     * @return The current room of the target.
     */
    @Override
    public Room getCurrentRoom(){
        return currentRoom;
    }

    /**
     * Sets the current room where the target is located.
     *
     * @param currentRoom The new room for the target.
     * @throws IllegalArgumentException if the room is null.
     */
    @Override
    public void setCurrentRoom(Room currentRoom){
        if(currentRoom == null){
            throw new IllegalArgumentException("Room cannot be null.");
        }
        this.currentRoom = currentRoom;
    }

    /**
     * Sets the target as picked up or not.
     *
     * @param pickedUp true if the target is picked up, false otherwise.
     */
    @Override
    public void setPickedUp(boolean pickedUp){
        this.pickedUp = pickedUp;
    }

    /**
     * Checks whether the target has been picked up.
     *
     * @return true if the target is picked up, false otherwise.
     */
    @Override
    public boolean isPickedUp(){
        return pickedUp;
    }
}