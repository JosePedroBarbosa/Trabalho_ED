package game.items;

import game.interfaces.IShield;
import game.map.Room;

/**
 * Represents a shield item in the game. A shield provides additional defensive points
 * to the player when picked up or used.
 */
public class Shield extends Item implements IShield {
    /**
     * The number of extra points the shield provides to the player.
     */
    private int extraPoints;

    /**
     * Constructor of the Shield with specified extra points and the room it is located in.
     *
     * @param extraPoints  The number of extra points the shield provides.
     * @param currentRoom  The room where the shield is located.
     */
    public Shield(int extraPoints,Room currentRoom) {
        super("Shield", currentRoom);
        this.extraPoints = extraPoints;
    }

    /**
     * Sets the number of extra points the shield provides.
     *
     * @param extraPoints The new value for the extra points.
     */
    @Override
    public void setPoints(int extraPoints) {
        this.extraPoints = extraPoints;
    }

    /**
     * Returns the number of extra points the shield provides.
     *
     * @return The number of extra points provided by the shield.
     */
    @Override
    public int getGivenPoints() {
        return this.extraPoints;
    }
}