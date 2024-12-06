package game.exceptions;

/**
 * The NoMissionInstantiated exception is thrown when an attempt is made to access
 * the Mission instance before it has been properly initialized.
 */
public class NoMissionInstantiated extends Exception {
    /**
     * Creates a new instance of <code>EmptyBackPackException</code> without
     * detail message.
     */
    public NoMissionInstantiated() {
    }

    /**
     * Constructs an instance of <code>EmptyBackPackException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoMissionInstantiated(String msg) {
        super(msg);
    }
}