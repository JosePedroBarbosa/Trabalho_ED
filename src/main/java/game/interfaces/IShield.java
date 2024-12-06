package game.interfaces;

/**
 * The IShield interface defines the basic contract for any shield in the game.
 */
public interface IShield {
    /**
     * Sets the number of extra defensive points the shield provides.
     *
     * @param extraPoints The new value for the shield's extra points.
     */
    public void setPoints(int extraPoints);

    /**
     * Gets the number of extra defensive points the shield provides.
     *
     * @return The number of extra points provided by the shield.
     */
    public int getGivenPoints();
}
