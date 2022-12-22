package cz.masci.commons.springfx.exception;

/**
 *
 * @author Daniel Mašek
 */
public class CrudException extends Exception {

  private static final String READ_EXCEPTION_TEXT = "Read error";
  private static final String WRITE_EXCEPTION_TEXT = "Write error";

  public CrudException(String message, Throwable cause) {
    super(message, cause);
  }

  public CrudException(String message) {
    super(message);
  }
  
  public static CrudException createReadException(Throwable throwable) {
    return new CrudException(READ_EXCEPTION_TEXT, throwable);
  }

  public static CrudException createWriteException(Throwable throwable) {
    return new CrudException(WRITE_EXCEPTION_TEXT, throwable);
  }

}
