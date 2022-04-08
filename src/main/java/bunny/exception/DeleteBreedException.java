/**
 * 
 */
package bunny.exception;

/**
 * This class is an unchecked exception. It is thrown by the bunny service if an error occurs
 * deleting a bunny breed.
 * 
 * @author Promineo
 *
 */
@SuppressWarnings("serial")
public class DeleteBreedException extends RuntimeException {

  /**
   * Create an empty exception.
   */
  public DeleteBreedException() {}

  /**
   * Create an exception with a message.
   * 
   * @param message The message
   */
  public DeleteBreedException(String message) {
    super(message);
  }

  /**
   * Create an exception with a cause.
   * 
   * @param cause The cause
   */
  public DeleteBreedException(Throwable cause) {
    super(cause);
  }

  /**
   * Create an exception with a message and a cause.
   * 
   * @param message The message
   * @param cause The cause
   */
  public DeleteBreedException(String message, Throwable cause) {
    super(message, cause);
  }
}
