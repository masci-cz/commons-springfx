package cz.masci.commons.springfx.exception;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ConverterExceptionTest {

  @Test
  void testSimpleConstructor() {
    var converterException = assertThrows(ConverterException.class, () -> {
      throw new ConverterException("test message", new Exception());
    });

    assertEquals("test message", converterException.getMessage());
  }

}