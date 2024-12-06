package game.interfaces;

import game.map.Room;

/**
 * The ITarget interface defines the basic contract for any target in the game.
 */
public interface ITarget {
    /**
     * Sets the type of the target.
     *
     * @param type The type of the target.
     */
    public void setType(String type);

    /**
     * Gets the type of the target.
     *
     * @return The type of the target.
     */
    public String getType();

    /**
     * Returns the current room where the target is located.
     *
     * @return The current room of the target.
     */
    public Room getCurrentRoom();

    /**
     * Sets the current room of the target.
     *
     * @param currentRoom The room to set as the target's current room.
     */
    public void setCurrentRoom(Room currentRoom);

    /**
     * Sets the status of whether the target has been picked up or not.
     *
     * @param pickedUp True if the target is picked up, false otherwise.
     */
    public void setPickedUp(boolean pickedUp);

    /**
     * Checks if the target has been picked up.
     *
     * @return True if the target has been picked up, false otherwise.
     */
    public boolean isPickedUp();
}