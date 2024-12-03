package game.exceptions;

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