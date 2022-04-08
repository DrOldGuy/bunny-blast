/**
 * 
 */
package bunny.exception;

import org.springframework.dao.DuplicateKeyException;
import bunny.controller.ErrorHandler;

/**
 * This class is an unchecked exception. It is used in the error handler to reduce the amount of
 * information sent to the caller when a {@link DuplicateKeyException} is thrown by Spring JDBC. See
 * {@link ErrorHandler#handleDuplicateKeyException(DuplicateKeyException, org.springframework.web.context.request.WebRequest)}
 * for details.
 * 
 * @author Promineo
 *
 */
@SuppressWarnings("serial")
public class AlreadyExistsException extends RuntimeException {

  /**
   * Create an empty exception.
   */
  public AlreadyExistsException() {}

  /**
   * Create an exception with a message.
   * 
   * @param message The message
   */
  public AlreadyExistsException(String message) {
    super(message);
  }

  /**
   * Create an exception with a cause.
   * 
   * @param cause The cause
   */
  public AlreadyExistsException(Throwable cause) {
    super(cause);
  }

  /**
   * Create an exception with a message and a cause.
   * 
   * @param message The message
   * @param cause The cause
   */
  public AlreadyExistsException(String message, Throwable cause) {
    super(message, cause);
  }
}
