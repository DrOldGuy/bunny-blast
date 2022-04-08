/**
 * 
 */
package bunny.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import bunny.controller.ErrorHandler;

/**
 * This class is an unchecked exception. It is used by the {@link ErrorHandler} to hide information
 * that is revealed in the {@link MethodArgumentNotValidException} message. See
 * {@link ErrorHandler#handleMethodArgumentNotValidException(MethodArgumentNotValidException, org.springframework.web.context.request.WebRequest)}
 * for details.
 * 
 * @author Promineo
 *
 */
@SuppressWarnings("serial")
public class FieldValidationException extends RuntimeException {

  /**
   * Create an empty exception.
   */
  public FieldValidationException() {}

  /**
   * Create an exception with a message.
   * 
   * @param message The message
   */
  public FieldValidationException(String message) {
    super(message);
  }

  /**
   * Create an exception with a cause.
   * 
   * @param cause The cause
   */
  public FieldValidationException(Throwable cause) {
    super(cause);
  }

  /**
   * Create an exception with a cause and a message.
   * 
   * @param message The message
   * @param cause The cause
   */
  public FieldValidationException(String message, Throwable cause) {
    super(message, cause);
  }
}
