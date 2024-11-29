package exceptions;

/**
 * NoSuchElementException is thrown when an element is not found
 */
public class NoSuchElementException extends RuntimeException {

    /**
     * Creates a new instance of NoSuchElementException without detail message.
     */
    public NoSuchElementException() {
        super();
    }

    /**
     * Constructs an instance of NoSuchElementException with the specified detail message.
     *
     * @param message the detail message.
     */
    public NoSuchElementException(String message) {
        super(message);
    }

}