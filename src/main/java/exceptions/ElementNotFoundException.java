package exceptions;

/**
 * ElementNotFoundException is an exception that is thrown when an element is not found in a collection.
 */
public class ElementNotFoundException extends RuntimeException {

  /**
   * Creates a new ElementNotFoundException with no message.
   */
  public ElementNotFoundException() {
    super();
  }

  /**
   * Creates a new ElementNotFoundException with the specified message.
   *
   * @param message the message for this exception.
   */
  public ElementNotFoundException(String message) {
    super(message);
  }
}