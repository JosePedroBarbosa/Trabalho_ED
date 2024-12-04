package dataStructures.exceptions;

/**
 * NonComparableElementException is thrown when an element is not comparable
 */
public class NonComparableElementException extends RuntimeException {

    /**
     * Create a new instance of NonComparableElementException without a message
     */
    public NonComparableElementException() {
        super();
    }

    /**
     * Create a new instance of NonComparableElementException with a message
     *
     * @param message The message to be displayed
     */
    public NonComparableElementException(String message) {
        super(message);
    }

}
