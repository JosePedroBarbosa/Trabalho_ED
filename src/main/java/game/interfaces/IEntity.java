package game.interfaces;

import game.map.Room;

/**
 * The ICharacter interface defines the basic contract for any character in the
 * game.
 */
public interface IEntity {

    /**
     * Retrieves the unique identifier (ID) of the character.
     *
     * @return the character's unique ID as an integer.
     */
    public int getId();

    /**
     * Retrieves the character's name.
     *
     * @return the character's name as a string.
     */
    public String getName();

    /**
     * Sets the character's name to a specified value.
     *
     * @param name the new name to assign to the character. Cannot be null.
     */
    public void setName(String name);

    /**
     * Retrieves the room where the character is currently located.
     *
     * @return the current Room object where the character is located.
     */
    public Room getCurrentRoom();

    /**
     * Sets the character's current room to a specified location. This method is
     * used to move the character from one room to another within the game.
     *
     * @param nextRoom the new Room object where the character should be
     * placed. Cannot be null.
     */
    public void setCurrentRoom(Room nextRoom);

    /**
     * Checks if the character is currently alive.
     *
     * @return {@code true} if the character is alive; {@code false} otherwise.
     */
    public boolean isAlive();

    /**
     * Retrieves the character's current health points (life).
     *
     * @return the character's current health as an integer.
     */
    public int getLife();

    /**
     * Reduces the character's health by a specified amount of damage.
     *
     * @param damage the amount of damage to inflict. Must be a positive
     * integer.
     */
    public void takeDamage(int damage);

    /**
     * Abstract method that Performs an attack on another character, causing damage.
     * This method must be implemented by subclasses of Entity.
     *
     */
    public abstract void attack();

    /**
     * Retrieves the current power level of the character. Power represents the
     * character's attack strength.
     *
     * @return the character's current power as an integer.
     */
    public int getPower();

    /**
     * Sets the health of the entity to the initial health value.
     */
    public void setLife();
}