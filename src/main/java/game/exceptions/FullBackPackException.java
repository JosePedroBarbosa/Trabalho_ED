package game.exceptions;

/**
 * The FullBackPackException is thrown when an attempt is made to add an item
 * to a backpack that has reached its maximum capacity.
 */
public class FullBackPackException extends Exception{
    /**
     * Creates a new instance of <code>FullBackPackException</code> without
     * detail message.
     */
    public FullBackPackException() {
    }

    /**
     * Constructs an instance of <code>FullBackPackException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public FullBackPackException(String msg) {
        super(msg);
    }
}