package game.interfaces;

/**
 * The IEnemy interface defines the basic contract for any enemy in the game.
 */
public interface IEnemy extends IEntity {
    /**
     * The attack method defines the behavior of an enemy when attacking.
     * Any class implementing this interface must provide an implementation for this method.
     */
    public void attack();
}
