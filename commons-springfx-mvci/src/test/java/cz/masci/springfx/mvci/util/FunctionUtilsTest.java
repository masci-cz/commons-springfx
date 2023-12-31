/*
 * Copyright (c) 2023
 *
 * This file is part of commons-springfx library.
 *
 * commons-springfx library is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option)
 *   any later version.
 *
 * commons-springfx library is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *   FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 *    License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *  along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */

package cz.masci.springfx.mvci.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class FunctionUtilsTest {

  private static final String NOT_NULL_VALUE = "VALUE";
  private static final String NULL_VALUE = "ELSE VALUE";

  @Test
  void applyNotNull_null() {
    TestClass source = null;

    var result = FunctionUtils.applyNotNull(source, TestClass::value);

    assertNull(result);
  }

  @Test
  void applyNotNull_notNull() {
    TestClass source = new TestClass(NOT_NULL_VALUE);

    var result = FunctionUtils.applyNotNull(source, TestClass::value);

    assertEquals(NOT_NULL_VALUE, result);
  }

  @Test
  void applyNotNullElse_null() {
    TestClass source = null;

    var result = FunctionUtils.applyNotNullElse(source, TestClass::value, NULL_VALUE);

    assertEquals(NULL_VALUE, result);
  }

  @Test
  void applyNotNullElse_notNull() {
    TestClass source = new TestClass(NOT_NULL_VALUE);

    var result = FunctionUtils.applyNotNullElse(source, TestClass::value, NULL_VALUE);

    assertEquals(NOT_NULL_VALUE, result);
  }

  @Test
  void applyNotNullElseGet_null() {
    TestClass source = null;

    var result = FunctionUtils.applyNotNullElseGet(source, TestClass::value, () -> NULL_VALUE);

    assertEquals(NULL_VALUE, result);
  }

  @Test
  void applyNotNullElseGet_notNull() {
    TestClass source = new TestClass(NOT_NULL_VALUE);

    var result = FunctionUtils.applyNotNullElseGet(source, TestClass::value, () -> NULL_VALUE);

    assertEquals(NOT_NULL_VALUE, result);
  }

  private record TestClass(String value) {}
}