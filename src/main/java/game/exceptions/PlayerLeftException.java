package game.exceptions;

/**
 *
 * @author carlo
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
