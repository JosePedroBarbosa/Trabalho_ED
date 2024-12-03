package dataStructures.exceptions;

/**
 * ConcurrentModificationException is an exception that is thrown when a collection is modified while an iterator is's in use.
 */
public class ConcurrentModificationException extends RuntimeException {

    /**
     * Creates a new ConcurrentModificationException with no message.
     */
    public ConcurrentModificationException() {
        super();
    }

    /**
     * Creates a new ConcurrentModificationException with the specified message.
     *
     * @param message the message for this exception.
     */
    public ConcurrentModificationException(String message) {
        super(message);
    }
}