package game.exceptions;

/**
 * The PlayerLeftException is thrown when an action is attempted after the player
 * has left or exited from the game.
 */
public class PlayerLeftException extends Exception{
    /**
     * Creates a new instance of <code>EmptyBackPackException</code> without
     * detail message.
     */
    public PlayerLeftException() {
    }

    /**
     * Constructs an instance of <code>EmptyBackPackException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public PlayerLeftException(String msg) {
        super(msg);
    }
}