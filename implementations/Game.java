package implementations;

public class Game {

    private static int maxBackpackCapacity;
    private static int maxEnemyMoves;
    private static int maxCharacterLife;

    public static int getMaxBackpackCapacity() {
        return maxBackpackCapacity;
    }

    public static void setMaxBackpackCapacity(int maxBackpackCapacity) {
        Game.maxBackpackCapacity = maxBackpackCapacity;
    }

    public static int getMaxEnemyMoves() {
        return maxEnemyMoves;
    }

    public static void setMaxEnemyMoves(int maxEnemyMoves) {
        Game.maxEnemyMoves = maxEnemyMoves;
    }

    public static int getMaxCharacterLife() {
        return maxEnemyMoves;
    }

    public static void setMaxCharacterLife(int maxCharacterLife) {
        Game.maxCharacterLife = maxCharacterLife;
    }
}
