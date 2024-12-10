package game.interfaces;

import dataStructures.implementations.ArrayUnorderedList;
import game.character.Enemy;
import game.character.Entity;
import game.character.Player;
import game.items.Item;

/**
 * Represents the interface for a room in the game.
 */
public interface IRoom {

    /**
     * Sets the name of the room.
     *
     * @param name The new name of the room.
     */
    public void setName(String name);

    /**
     * Gets the name of the room.
     *
     * @return The name of the room.
     */
    public String getName();

    /**
     * Retrieves the list of enemies in the room.
     *
     * @return A list of enemies in the room.
     */
     public ArrayUnorderedList<Enemy> getEnemies();

    /**
     * Removes a specific enemy from the room.
     *
     * @param enemy The enemy to be removed.
     */
    public void removeEnemy(Enemy enemy);

    /**
     * Adds an enemy to the room.
     *
     * @param enemy The enemy to be added.
     */
    public void addEnemy(Enemy enemy);

    /**
     * Retrieves the list of items in the room.
     *
     * @return A list of items in the room.
     */
    public ArrayUnorderedList<Item> getItems();

    /**
     * Removes a specific item from the room.
     *
     * @param item The item to be removed.
     */
    public void removeItem(Item item);

    /**
     * Adds an item to the room.
     *
     * @param item The item to be added.
     */
    public void addItem(Item item);

    /**
     * Checks if the room contains a player.
     *
     * @return True if the room contains a player, false otherwise.
     */
    public boolean hasPlayer();

    /**
     * Retrieves the player currently in the room.
     *
     * @return The player in the room.
     * @throws IllegalArgumentException If no player is present in the room.
     */
    public Player getPlayer();

    /**
     * Removes the player from the room.
     */
    public void removePlayer();

    /**
     * Places a player in the room.
     *
     * @param player The player to be added.
     */
    public void setPlayer(Player player);

    /**
     * Checks if the room contains any enemies.
     *
     * @return True if the room contains enemies, false otherwise.
     */
    public boolean hasEnemies();

    /**
     * Marks the room as both an entrance and an exit.
     */
    public void setEntranceAndExit();

    /**
     * Checks if the room is an entrance and an exit.
     *
     * @return True if the room is an entrance and an exit, false otherwise.
     */
    public boolean isEntranceAndExit();
}
