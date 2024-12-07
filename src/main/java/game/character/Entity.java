package game.character;

import game.settings.GameSettings;
import game.map.Room;
import game.interfaces.IEntity;

/**
 * Represents a generic entity in the game.
 * An entity has a unique id, a name, health, power, and a current room.
 */
public abstract class Entity implements IEntity {
    private static int nextId = 0;
    private final int id;
    private String name;
    protected int health;
    protected final int power;
    protected Room currentRoom;

    /**
     * Constructs a new entity with the given name, power, and current room.
     * Sets the initial health using the GameSettings.
     *
     * @param name The name of the entity.
     * @param power The power of the entity.
     * @param currentRoom The room where the entity is located.
     */
    public Entity(String name, int power, Room currentRoom) {
        this.id = nextId++;
        this.name = name;
        this.health = GameSettings.getInitialCharacterHealth();
        this.power = power;
        this.currentRoom = currentRoom;
    }

    /**
     * Constructs a new entity with the given name and power, without a room.
     * The entity will not be placed in a room initially.
     *
     * @param name The name of the entity.
     * @param power The power of the entity.
     */
    public Entity(String name, int power) {
        this.id = nextId++;
        this.name = name;
        this.health = GameSettings.getInitialCharacterHealth();
        this.power = power;
        this.currentRoom = null;
    }

    /**
     * Returns the ID of the entity.
     *
     * @return The ID of the entity.
     */
    @Override
    public int getId() {
        return this.id;
    }

    /**
     * Returns the name of the entity.
     *
     * @return The name of the entity.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of the entity.
     *
     * @param name The new name of the entity.
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the current room where the entity is located.
     *
     * @return The current room of the entity.
     */
    @Override
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Sets the current room of the entity. Throws an exception if the room is null.
     *
     * @param nextRoom The new room where the entity will be placed.
     */
    @Override
    public void setCurrentRoom(Room nextRoom) {
        this.currentRoom = nextRoom;
    }

    /**
     * Checks if the entity is still alive (health > 0).
     *
     * @return True if the entity is alive, false otherwise.
     */
    @Override
    public boolean isAlive() {
        return this.health > 0;
    }

    /**
     * Returns the current health of the entity.
     *
     * @return The health of the entity.
     */
    @Override
    public int getLife() {
        return this.health;
    }

    /**
     * Reduces the entity's health by the given damage amount.
     * If the damage is negative, health will not be reduced.
     *
     * @param damage The amount of damage to apply.
     */
    @Override
    public void takeDamage(int damage) {
        if (damage >= 0) {
            this.health -= damage;
        }
    }

    /**
     * Returns the power of the entity.
     *
     * @return The power of the entity.
     */
    @Override
    public int getPower() {
        return this.power;
    }

    /**
     * Abstract method that Performs an attack on another character, causing damage.
     * This method must be implemented by subclasses of Entity.
     *
     */
    public abstract void attack();

    /**
     * Sets the health of the entity to the initial health value.
     */
    @Override
    public void setLife() {
        this.health =  GameSettings.getInitialCharacterHealth();
    }
}