package game.interfaces;

import game.map.Room;

/**
 * The IItem interface defines the basic contract for any item in the game.
 */
public interface IItem {
    /**
     * Retrieves the type of the item.
     *
     * @return A string representing the item's type.
     */
    public String getType();

    /**
     * Gets the current room where the item is located.
     *
     * @return The room where the item is currently placed.
     */
    public Room getCurrentRoom();

    /**
     * Updates the current room where the item is located.
     *
     * @param currentRoom The new room where the item will be placed. Must not be null.
     */
    public void setCurrentRoom(Room currentRoom);

    /**
     * Checks whether the item has been picked up by a player.
     *
     * @return True if the item has been picked up, false otherwise.
     */
    public boolean isPickedUp();

    /**
     * Updates the item's picked-up status.
     *
     * @param pickedUp True if the item has been picked up, false otherwise.
     */
    public void setPickedUp(boolean pickedUp);

    /**
     * Abstract method that returns the number of points this item provides.
     * This method must be implemented by subclasses of Item.
     *
     * @return The number of points this item provides.
     */
    public abstract int getGivenPoints();
}