package game.items;

import game.interfaces.IItem;
import game.map.Room;

/**
 * Represents a generic item in the game.
 * Items can have a specific type, be located in a room, and be marked as picked up or not.
 */
public abstract class Item implements IItem {
    /**
     * The type of the item.
     */
    protected String type;

    /**
     * The current room where the item is located.
     */
    protected Room currentRoom;

    /**
     * Indicates whether the item has been picked up by a player.
     */
    protected boolean pickedUp;

    /**
     * Constructs a new item with the specified type and initial room location.
     * By default, the item is not picked up.
     *
     * @param type The type of the item.
     * @param currentRoom The room where the item is initially located.
     */
    public Item(String type, Room currentRoom) {
        this.type = type;
        this.currentRoom = currentRoom;
        this.pickedUp = false;
    }

    /**
     * Returns the type of the item.
     *
     * @return The type of the item.
     */
    @Override
    public String getType() {
        return this.type;
    }

    /**
     * Returns the current room where the item is located.
     *
     * @return The room where the item is currently located.
     */
    @Override
    public Room getCurrentRoom(){
        return currentRoom;
    }

    /**
     * Sets the current room where the item is located.
     *
     * @param currentRoom The new room for the item.
     * @throws IllegalArgumentException if the provided room is null.
     */
    @Override
    public void setCurrentRoom(Room currentRoom) {
        if (currentRoom == null) {
            throw new IllegalArgumentException("Room cannot be null");
        }
        this.currentRoom = currentRoom;
    }

    /**
     * Checks if the item has been picked up by a player.
     *
     * @return True if the item has been picked up, false otherwise.
     */
    @Override
    public boolean isPickedUp() {
        return pickedUp;
    }

    /**
     * Marks the item as picked up or not.
     *
     * @param pickedUp True to mark the item as picked up, false otherwise.
     */
    @Override
    public void setPickedUp(boolean pickedUp) {
        this.pickedUp = pickedUp;
    }

    /**
     * Abstract method that returns the number of points this item provides.
     * This method must be implemented by subclasses of Item.
     *
     * @return The number of points this item provides.
     */
    public abstract int getGivenPoints();
}