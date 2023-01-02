package cz.masci.commons.springfx.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CrudExceptionTest {

  @Test
  void testCreateReadException() {
    var crudException = assertThrows(CrudException.class, () -> {
      throw CrudException.createReadException(new Exception());
    });

    assertEquals("Read error", crudException.getMessage());
  }

  @Test
  void testCreateWriteException() {
    var crudException = assertThrows(CrudException.class, () -> {
      throw CrudException.createWriteException(new Exception());
    });

    assertEquals("Write error", crudException.getMessage());
  }

  @Test
  void testSimpleConstructor() {
    var crudException = assertThrows(CrudException.class, () -> {
      throw new CrudException("test message");
    });

    assertEquals("test message", crudException.getMessage());
  }
}