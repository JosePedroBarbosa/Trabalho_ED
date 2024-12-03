package dataStructures.exceptions;

/**
 * IllegalArgumentException is thrown when an argument is not valid.
 */
public class IllegalArgumentException extends RuntimeException {

    /**
     * Constructs an IllegalArgumentException with no detail message.
     */
    public IllegalArgumentException() {
        super();
    }

    /**
     * Constructs an IllegalArgumentException with the specified detail message.
     *
     * @param message the detail message
     */
    public IllegalArgumentException(String message) {
        super(message);
    }
}