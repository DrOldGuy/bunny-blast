/**
 * 
 */
package bunny.controller;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import bunny.entity.AddBreedRequest;
import bunny.exception.AlreadyExistsException;
import bunny.exception.DeleteBreedException;
import bunny.exception.FieldValidationException;
import lombok.extern.slf4j.Slf4j;

/**
 * This class manages runtime exceptions thrown during the bunny operations. Spring's default
 * behavior is to return a 500 status if it encounters an exception in an operation. Spring will
 * route an exception to the appropriate handler if it is declared in this class, which is annotated
 * with @RestControllerAdvice. This annotation tells Spring that this is a global error handler
 * class.
 * <p>
 * To declare an exception handler, use the @ExceptionHandler annotation to define the type of
 * exception to handle in any method. You can also use @ResponseStatus to declare the status code
 * returned from the error handler. The status codes declared in these methods override the status
 * codes declared in the controller methods, as well as the default status code (500) returned by
 * Spring.
 * <p>
 * You can also change the return type from that declared in the controller. These methods return a
 * map with error values. See
 * {@link #createExceptionMessage(Exception, HttpStatus, WebRequest, LogStatus)} for details.
 * <p>
 * By putting all error handling in a single class, it allows the controller code, service code, and
 * DAO code to be much simpler. This class also manages a generic Exception, which will catch any
 * unplanned and unspecified runtime exceptions.
 * 
 * @author Promineo
 *
 */
@RestControllerAdvice
@Slf4j
public class ErrorHandler {

  /*
   * This enum determines if a stack trace is logged in the log file or just the error message. In
   * general stack traces are only logged for unplanned errors. All others are "planned for" so just
   * the error message is logged.
   */
  private enum LogStatus {
    STACK_TRACE, MESSAGE_ONLY
  }

  /**
   * This is the handler for a custom {@link DeleteBreedException}. This exception is thrown if the
   * breed cannot be deleted for some reason.
   * 
   * @param e The caught exception.
   * @param webRequest The Spring-supplied object that describes the request.
   * @return A map containing information about the error.
   */
  @ExceptionHandler(DeleteBreedException.class)
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  public Map<String, Object> handleDeleteBreedException(DeleteBreedException e,
      WebRequest webRequest) {
    return createExceptionMessage(e, HttpStatus.INTERNAL_SERVER_ERROR, webRequest,
        LogStatus.MESSAGE_ONLY);
  }

  /**
   * This is the handler for Spring's {@link DuplicateKeyException}. This exception is thrown if the
   * user adds a bunny with a name that already exists.
   * 
   * @param e The caught exception.
   * @param webRequest The Spring-supplied object that describes the request.
   * @return A map containing information about the error.
   */
  @ExceptionHandler(DuplicateKeyException.class)
  @ResponseStatus(code = HttpStatus.CONFLICT)
  public Map<String, Object> handleDuplicateKeyException(DuplicateKeyException e,
      WebRequest webRequest) {
    String message = "Duplicate key";

    /*
     * The Spring DuplicateKeyException contains a message with table names and column names. This
     * greatly increases the chance that someone will be able to hack the system. The message in the
     * embedded SQLIntegrityConstraintViolationException contains the Java field name that is
     * sufficient for our purposes, yet not revealing of back-end details that we prefer to keep
     * secret.
     */
    if (Objects.nonNull(e.getCause())
        && e.getCause() instanceof SQLIntegrityConstraintViolationException violationException) {
      message = violationException.getMessage();
    }

    /*
     * Change the type of exception to a custom AlreadyExistsException so that application details
     * are not revealed to the caller.
     */
    Exception alt = new AlreadyExistsException(message);
    return createExceptionMessage(alt, HttpStatus.CONFLICT, webRequest, LogStatus.MESSAGE_ONLY);
  }

  /**
   * This method handles a {@link MethodArgumentNotValidException}. This exception is thrown if a
   * JSON field fails Bean validation. Validation annotations are defined in classes that input data
   * to the controllers, such as {@link AddBreedRequest}.
   * 
   * @param e The caught exception.
   * @param webRequest The Spring-supplied object that describes the request.
   * @return A map containing information about the error.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public Map<String, Object> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException e, WebRequest webRequest) {

    /*
     * The method getFieldErrors() contains a list of FieldError objects. We just need the name of
     * the field(s) that failed Bean validation. This code does the following: 1) Create a Stream of
     * FieldError objects from the list of FieldError objects, 2) Map the Stream of FieldError to a
     * Stream of String (field names), 3) Concatenate the field names together separated by commas.
     */
    String errorFieldNames = e.getBindingResult()
        .getFieldErrors() // @formatter:off
        .stream()
        .map(FieldError::getField)
        .collect(Collectors.joining(", ")); // @formatter:on

    /*
     * Turn the complex MethodArgumentNotValidException into a simple FieldValidationException. The
     * exception name is returned to the caller so it should be something understandable.
     */
    Exception alt = new FieldValidationException("Invalid field(s): " + errorFieldNames);
    return createExceptionMessage(alt, HttpStatus.BAD_REQUEST, webRequest, LogStatus.MESSAGE_ONLY);
  }

  /**
   * This method handles the MethodArgumentTypeMismatchException. This exception is thrown when
   * simple arguments are supplied to a controller method (like an URL parameter breedId). If the
   * JSON cannot be converted to the proper type, like if the JSON field is "abc" and it is being
   * converted to an integer, this exception is thrown.
   * 
   * @param e The caught exception.
   * @param webRequest The Spring-supplied object that describes the request.
   * @return A map containing information about the error.
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  public Map<String, Object> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException e, WebRequest webRequest) {
    return createExceptionMessage(e, HttpStatus.BAD_REQUEST, webRequest, LogStatus.MESSAGE_ONLY);
  }

  /**
   * This handles the {@link NoSuchElementException}, turning the default Spring status code from
   * 500 to 404. It won't get called unless the DAO returns an empty Optional.
   * 
   * @param e The {@link NoSuchElementException}.
   * @param webRequest The Spring-supplied WebRequest object.
   * @return A detailed error message.
   */
  @ExceptionHandler(NoSuchElementException.class)
  @ResponseStatus(code = HttpStatus.NOT_FOUND)
  public Map<String, Object> handleNoSuchElementException(NoSuchElementException e,
      WebRequest webRequest) {
    return createExceptionMessage(e, HttpStatus.NOT_FOUND, webRequest, LogStatus.MESSAGE_ONLY);
  }

  /**
   * This handles any unplanned runtime exceptions that may occur in the application.
   * 
   * @param e The unplanned exception.
   * @param webRequest The Spring-supplied WebRequest object.
   * @return A detailed error message.
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
  public Map<String, Object> handleException(Exception e, WebRequest webRequest) {
    return createExceptionMessage(e, HttpStatus.INTERNAL_SERVER_ERROR, webRequest,
        LogStatus.STACK_TRACE);
  }

  /**
   * Create a map that contains details about the error.
   * 
   * @param e The caught exception
   * @param status The HTTP status code that is returned
   * @param webRequest A Spring-supplied object that contains information about the request
   * @param logStatus If this value is LogStatus.MESSAGE_ONLY, the exception message is logged. If
   *        the value is LogStatus.STACK_TRACE, the entire stack trace is logged.
   * @return The exception map
   */
  private Map<String, Object> createExceptionMessage(Exception e, HttpStatus status,
      WebRequest webRequest, LogStatus logStatus) {
    Map<String, Object> error = new HashMap<>();
    String timestamp = ZonedDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME);

    /* Use pattern matching instanceof operator to do check and cast in one operation. */
    if (webRequest instanceof ServletWebRequest servletWebRequest) {
      error.put("uri", servletWebRequest.getRequest().getRequestURI());
    }

    error.put("message", e.toString());
    error.put("status code", status.value());
    error.put("timestamp", timestamp);
    error.put("reason", status.getReasonPhrase());

    if (logStatus == LogStatus.MESSAGE_ONLY) {
      log.error("Exception: {}", e.toString());
    } else {
      log.error("Exception:", e);
    }

    return error;
  }
}
