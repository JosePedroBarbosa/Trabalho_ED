package game.interfaces;

import game.character.Enemy;
import game.exceptions.EmptyBackPackException;
import game.items.Backpack;
import game.items.Item;

/**
 * The IPlayer interface defines the basic contract for any player in the game.
 */
public interface IPlayer extends IEntity {
    /**
     * Attacks all enemies in the current room.
     * The damage dealt is determined by the player's power.
     */
    public void attack();

    /**
     * Attacks a specific enemy, dealing damage based on the player's power.
     *
     * @param target The enemy to be attacked.
     */
    public void attack(Enemy target);

    /**
     * Uses an item from the player's backpack to restore health or gain other benefits.
     * The specific effect depends on the item used.
     *
     * @throws EmptyBackPackException If the backpack is empty and no item can be used.
     */
    public void useItem() throws EmptyBackPackException;

    /**
     * Collects an item.
     * The item is either added to the backpack (if it can be stored) or immediately applied
     * (shields might increase health instantly).
     *
     * @param item The item to be collected.
     */
    public void collectItem(Item item);

    /**
     * Returns the player's backpack, which stores collected items.
     *
     * @return The backpack containing the player's items.
     */
    public Backpack getBackpack();
}