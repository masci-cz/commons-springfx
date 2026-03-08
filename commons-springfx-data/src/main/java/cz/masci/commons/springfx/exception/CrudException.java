package cz.masci.commons.springfx.exception;

/**
 * Exception thrown when a CRUD (Create, Read, Update, Delete) operation fails.
 *
 * @author Daniel Mašek
 */
public class CrudException extends Exception {

  /** Default message used for read operation failures. */
  private static final String READ_EXCEPTION_TEXT = "Read error";
  /** Default message used for write operation failures. */
  private static final String WRITE_EXCEPTION_TEXT = "Write error";

  /**
   * Creates a new {@code CrudException} with the given message and cause.
   *
   * @param message the detail message describing the failure
   * @param cause   the underlying cause of the exception
   */
  public CrudException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Creates a new {@code CrudException} with the given message.
   *
   * @param message the detail message describing the failure
   */
  public CrudException(String message) {
    super(message);
  }

  /**
   * Creates a {@code CrudException} representing a read operation failure.
   *
   * @param throwable the underlying cause of the read failure
   * @return a new {@code CrudException} with a default read error message
   */
  public static CrudException createReadException(Throwable throwable) {
    return new CrudException(READ_EXCEPTION_TEXT, throwable);
  }

  /**
   * Creates a {@code CrudException} representing a write operation failure.
   *
   * @param throwable the underlying cause of the write failure
   * @return a new {@code CrudException} with a default write error message
   */
  public static CrudException createWriteException(Throwable throwable) {
    return new CrudException(WRITE_EXCEPTION_TEXT, throwable);
  }

}
