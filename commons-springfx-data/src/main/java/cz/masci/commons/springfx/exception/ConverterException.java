package cz.masci.commons.springfx.exception;

/**
 * Exception thrown when a conversion operation fails.
 *
 * @author Daniel Mašek
 */
public class ConverterException extends Exception {

  /**
   * Creates a new {@code ConverterException} with the given message and cause.
   *
   * @param message the detail message describing the conversion failure
   * @param cause   the underlying cause of the exception
   */
  public ConverterException(String message, Throwable cause) {
    super(message, cause);
  }
  
}
