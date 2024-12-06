package game.exceptions;

/**
 * The EmptyBackPackException is thrown when an attempt is made to interact with
 * an empty backpack, such as trying to use an item when there are no items
 * available.
 */
public class EmptyBackPackException extends Exception{

    /**
     * Creates a new instance of <code>EmptyBackPackException</code> without
     * detail message.
     */
    public EmptyBackPackException() {
    }

    /**
     * Constructs an instance of <code>EmptyBackPackException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public EmptyBackPackException(String msg) {
        super(msg);
    }
}