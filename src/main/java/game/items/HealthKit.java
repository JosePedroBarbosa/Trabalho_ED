package game.items;

import game.interfaces.IHealthKit;
import game.map.Room;

/**
 * Represents a health kit item that can recover a player's health points.
 * A health kit has a specified number of recovery points and is associated with a room in the game.
 */
public class HealthKit extends Item implements IHealthKit {
    /**
     * The number of health points recovered by the health kit.
     */
    private int recoveredPoints;

    /**
     * Constructs a new HealthKit with the specified recovery points and room.
     *
     * @param recoveredPoints The number of health points the health kit restores.
     * @param currentRoom The room where the health kit is located.
     */
    public HealthKit(int recoveredPoints, Room currentRoom) {
        super("Health Kit", currentRoom);
        this.recoveredPoints = recoveredPoints;
    }

    /**
     * Sets the number of health points the health kit restores.
     *
     * @param recoveredPoints The number of health points to set.
     */
    @Override
    public void setPoints(int recoveredPoints) {
        this.recoveredPoints = recoveredPoints;
    }

    /**
     * Returns the number of health points the health kit provides.
     *
     * @return The number of health points restored by the health kit.
     */
    @Override
    public int getGivenPoints() {
        return this.recoveredPoints;
    }
}