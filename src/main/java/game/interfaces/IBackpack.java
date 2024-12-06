package game.interfaces;

import game.exceptions.EmptyBackPackException;
import game.exceptions.FullBackPackException;

import game.items.Item;

/**
 * Represents the interface for a backpack in the game.
 */
public interface IBackpack {
    /**
     * Removes and returns the most recently added item from the backpack.
     *
     * @return The item removed from the backpack.
     * @throws EmptyBackPackException If the backpack is empty.
     */
    public Item useBackpackItem() throws EmptyBackPackException;

    /**
     * Adds a new item to the backpack.
     *
     * @param item The item to add to the backpack.
     * @throws FullBackPackException If the backpack has reached its maximum capacity.
     * @throws IllegalArgumentException If the item is null.
     */
    public void addItem(Item item) throws FullBackPackException;

    /**
     * Returns the current number of items in the backpack.
     *
     * @return The number of items in the backpack.
     */
    public int getBackpackSize();
}
