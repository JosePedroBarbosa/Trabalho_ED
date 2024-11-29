package game.items;

import dataStructures.implementations.ArrayStack;
import game.exceptions.EmptyBackPackException;
import game.exceptions.FullBackPackException;
import implementations.Game;
import game.interfaces.IBackpack;

public class Backpack implements IBackpack {

    private ArrayStack<Item> inventory;

    public Backpack() {
        this.inventory = new ArrayStack<>(Game.getMaxBackpackCapacity());
    }

    @Override
    public Item useItem() throws EmptyBackPackException {
        if (inventory.isEmpty()) {
            throw new EmptyBackPackException("No items to use.");
        }
        return inventory.pop();
    }

    @Override
    public void addItem(Item item) throws FullBackPackException {
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null.");
        }
        if (inventory.size() == Game.getMaxBackpackCapacity()) {
            throw new FullBackPackException("Maximum number of items reached.");
        }

        inventory.push(item);
    }

}
