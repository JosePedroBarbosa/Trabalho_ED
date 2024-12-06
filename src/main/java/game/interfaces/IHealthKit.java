package game.interfaces;

/**
 * The IHealthKit interface defines the basic contract for any health kit in the game.
 */
public interface IHealthKit {
    /**
     * Sets the number of health points the health kit restores.
     *
     * @param recoveredPoints The new value for the health kit's restored points.
     */
    public void setPoints(int recoveredPoints);

    /**
     * Gets the number of health points the health kit restores.
     *
     * @return The number of health points restored by the health kit.
     */
    public int getGivenPoints();
}
