package game.map;

import dataStructures.implementations.ArrayUnorderedList;
import game.character.Enemy;
import game.character.Player;
import game.items.Item;
import game.interfaces.IRoom;

/**
 * Represents a room in the game.
 * A room can contain enemies, items, and optionally a player.
 * It can also be designated as an entrance/exit.
 */
public class Room implements IRoom {
    /**
     * The name of the room.
     */
    private String name;

    /**
     * The list of enemies currently present in the room.
     */
    private ArrayUnorderedList<Enemy> enemies;

    /**
     * The list of items currently present in the room.
     */
    private ArrayUnorderedList<Item> items;

    /**
     * The player currently in the room, if any.
     */
    private Player player;

    /**
     * Indicates whether the room is both an entrance and an exit.
     */
    private boolean isEntranceAndExit;

    /**
     * Constructs a new Room with the specified name.
     * The room is initialized with no enemies, items, or players.
     * It is not marked as an entrance or exit by default.
     *
     * @param name The name of the room.
     */
    public Room(String name) {
        this.name = name;
        this.enemies = new ArrayUnorderedList<>();
        this.items = new ArrayUnorderedList<>();
        this.player = null;
        this.isEntranceAndExit = false;
    }

    /**
     * Sets the name of the room.
     *
     * @param name The new name of the room.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the name of the room.
     *
     * @return The name of the room.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves the list of enemies in the room.
     *
     * @return A list of enemies in the room.
     */
    @Override
    public ArrayUnorderedList<Enemy> getEnemies() {
        return this.enemies;
    }

    /**
     * Removes a specific enemy from the room.
     *
     * @param enemy The enemy to be removed.
     */
    public void removeEnemy(Enemy enemy) {
        this.enemies.remove(enemy);
    }

    /**
     * Adds an enemy to the room.
     *
     * @param enemy The enemy to be added.
     */
    public void addEnemy(Enemy enemy) {
        this.enemies.addToRear(enemy);
    }

    /**
     * Retrieves the list of items in the room.
     *
     * @return A list of items in the room.
     */
    @Override
    public ArrayUnorderedList<Item> getItems() {
        return items;
    }

    /**
     * Removes a specific item from the room.
     *
     * @param item The item to be removed.
     */
    public void removeItem(Item item) {
        this.items.remove(item);
    }

    /**
     * Adds an item to the room.
     *
     * @param item The item to be added.
     */
    public void addItem(Item item) {
        this.items.addToRear(item);
    }

    /**
     * Checks if the room contains a player.
     *
     * @return True if the room contains a player, false otherwise.
     */
    @Override
    public boolean hasPlayer() {
        return this.player != null;
    }

    /**
     * Retrieves the player currently in the room.
     *
     * @return The player in the room.
     * @throws IllegalArgumentException If no player is present in the room.
     */
    @Override
    public Player getPlayer() {
        if(!hasPlayer()){
            throw new IllegalArgumentException("No Player In The Room");
        }
        return this.player;
    }

    /**
     * Removes the player from the room.
     */
    public void removePlayer() {
        this.player = null;
    }

    /**
     * Places a player in the room.
     *
     * @param player The player to be added.
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Returns a string representation of the room.
     *
     * @return The name of the room.
     */
    @Override
    public String toString(){
        return name;
    }

    /**
     * Checks if the room contains any enemies.
     *
     * @return True if the room contains enemies, false otherwise.
     */
    public boolean hasEnemies(){
        return !this.enemies.isEmpty();
    }

    /**
     * Marks the room as both an entrance and an exit.
     */
    public void setEntranceAndExit(){
        this.isEntranceAndExit = true;
    }

    /**
     * Checks if the room is an entrance and an exit.
     *
     * @return True if the room is an entrance and an exit, false otherwise.
     */
    public boolean isEntranceAndExit(){
        return this.isEntranceAndExit;
    }

}