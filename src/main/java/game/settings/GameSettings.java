package game.settings;

/**
 * This class represents the settings for the game, such as backpack capacity, max enemy moves,
 * character health, and player power.
 */
public class GameSettings {
    /**
     * Maximum capacity of the player's backpack.
     */
    private static int maxBackpackCapacity;

    /**
     * Maximum number of moves an enemy can make.
     */
    private static int maxEnemyMoves;

    /**
     * Initial health of the player's character.
     */
    private static int initialCharacterHealth;

    /**
     * Power level of the player's character.
     */
    private static int playerPower;

    /**
     * Gets the maximum capacity of the player's backpack.
     *
     * @return the maximum backpack capacity
     */
    public static int getMaxBackpackCapacity() {
        return maxBackpackCapacity;
    }

    /**
     * Sets the maximum capacity of the player's backpack.
     *
     * @param maxBackpackCapacity the maximum backpack capacity to set
     */
    public static void setMaxBackpackCapacity(int maxBackpackCapacity) {
        GameSettings.maxBackpackCapacity = maxBackpackCapacity;
    }

    /**
     * Gets the maximum number of moves an enemy can make.
     *
     * @return the maximum enemy moves
     */
    public static int getMaxEnemyMoves() {
        return maxEnemyMoves;
    }

    /**
     * Sets the maximum number of moves an enemy can make.
     *
     * @param maxEnemyMoves the maximum enemy moves to set
     */
    public static void setMaxEnemyMoves(int maxEnemyMoves) {
        GameSettings.maxEnemyMoves = maxEnemyMoves;
    }

    /**
     * Gets the initial health of the player's character.
     *
     * @return the initial character health
     */
    public static int getInitialCharacterHealth() {
        return initialCharacterHealth;
    }

    /**
     * Sets the initial health of the player's character.
     *
     * @param initialCharacterHealth the initial character health to set
     */
    public static void setInitialCharacterHealth(int initialCharacterHealth) {
        GameSettings.initialCharacterHealth = initialCharacterHealth;
    }

    /**
     * Sets the power level of the player's character.
     *
     * @param playerPower the player power to set
     */
    public static void setPlayerPower(int playerPower) {
        GameSettings.playerPower = playerPower;
    }

    /**
     * Gets the power level of the player's character.
     *
     * @return the player power
     */
    public static int getPlayerPower() {
        return playerPower;
    }
}