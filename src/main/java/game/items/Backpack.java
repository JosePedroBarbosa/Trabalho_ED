package game.items;

import dataStructures.implementations.ArrayStack;
import game.exceptions.EmptyBackPackException;
import game.exceptions.FullBackPackException;
import game.settings.GameSettings;
import game.interfaces.IBackpack;

/**
 * Represents a backpack that holds items for a player.
 * The backpack has a limited capacity defined by the game settings
 * and allows players to add or use items.
 */
public class Backpack implements IBackpack {
    /**
     * The stack used to store items in the backpack.
     */
    private ArrayStack<Item> inventory;

    /**
     * Constructs a new backpack with the maximum capacity specified in the game settings.
     */
    public Backpack() {
        this.inventory = new ArrayStack<>(GameSettings.getMaxBackpackCapacity());
    }

    /**
     * Removes and returns the most recently added item from the backpack.
     *
     * @return The item removed from the backpack.
     * @throws EmptyBackPackException If the backpack is empty.
     */
    @Override
    public Item useBackpackItem() throws EmptyBackPackException {
        if (inventory.isEmpty()) {
            throw new EmptyBackPackException("The backpack is empty.");
        }
        return inventory.pop();
    }

    /**
     * Adds a new item to the backpack.
     *
     * @param item The item to add to the backpack.
     * @throws FullBackPackException If the backpack has reached its maximum capacity.
     * @throws IllegalArgumentException If the item is null.
     */
    @Override
    public void addItem(Item item) throws FullBackPackException {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null.");
        }
        if (inventory.size() == GameSettings.getMaxBackpackCapacity()) {
            throw new FullBackPackException("Maximum number of items reached.");
        }

        inventory.push(item);
    }

    /**
     * Returns the current number of items in the backpack.
     *
     * @return The number of items in the backpack.
     */
    @Override
    public int getBackpackSize(){
        return inventory.size();
    }

}