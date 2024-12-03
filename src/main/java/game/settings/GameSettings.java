package game.settings;

public class GameSettings {

    private static int maxBackpackCapacity;
    private static int maxEnemyMoves;
    private static int initialCharacterHealth;
    private static int playerPower;

    public static int getMaxBackpackCapacity() {
        return maxBackpackCapacity;
    }

    public static void setMaxBackpackCapacity(int maxBackpackCapacity) {
        GameSettings.maxBackpackCapacity = maxBackpackCapacity;
    }

    public static int getMaxEnemyMoves() {
        return maxEnemyMoves;
    }

    public static void setMaxEnemyMoves(int maxEnemyMoves) {
        GameSettings.maxEnemyMoves = maxEnemyMoves;
    }

    public static int getInitialCharacterHealth() {
        return initialCharacterHealth;
    }

    public static void setInitialCharacterHealth(int initialCharacterHealth) {
        GameSettings.initialCharacterHealth = initialCharacterHealth;
    }

    public static void setPlayerPower(int playerPower) {
        GameSettings.playerPower = playerPower;
    }

    public static int getPlayerPower() {
        return playerPower;
    }

}
