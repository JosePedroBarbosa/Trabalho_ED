package game.items;

import dataStructures.implementations.ArrayStack;
import game.exceptions.EmptyBackPackException;
import game.exceptions.FullBackPackException;
import game.settings.GameSettings;
import game.interfaces.IBackpack;

public class Backpack implements IBackpack {

    private ArrayStack<Item> inventory;

    public Backpack() {
        this.inventory = new ArrayStack<>(GameSettings.getMaxBackpackCapacity());
    }

    @Override
    public Item useBackpackItem() throws EmptyBackPackException {
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
        if (inventory.size() == GameSettings.getMaxBackpackCapacity()) {
            throw new FullBackPackException("Maximum number of items reached.");
        }

        inventory.push(item);
    }

    public int getBackpackSize(){
        return inventory.size();
    }

}
