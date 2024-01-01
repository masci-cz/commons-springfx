/*
 * Copyright (c) 2024
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

package cz.masci.springfx.mvci.model.dirty;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DirtyBooleanPropertyTest {
  
  public static final Boolean INITIAL_VALUE = false;
  public static final Boolean DIRTY_VALUE = true;

  @Test
  void isDirty_setValue() {
    var dirtyProperty = new DirtyBooleanProperty(INITIAL_VALUE);

    assertFalse(dirtyProperty.isDirty());

    dirtyProperty.setValue(DIRTY_VALUE);

    assertTrue(dirtyProperty.isDirty());
    assertEquals(DIRTY_VALUE, dirtyProperty.get());
    assertEquals(DIRTY_VALUE, dirtyProperty.getValue());
  }

  @Test
  void isDirty_set() {
    var dirtyProperty = new DirtyBooleanProperty(INITIAL_VALUE);

    assertFalse(dirtyProperty.isDirty());

    dirtyProperty.set(DIRTY_VALUE);

    assertTrue(dirtyProperty.isDirty());
    assertEquals(DIRTY_VALUE, dirtyProperty.get());
    assertEquals(DIRTY_VALUE, dirtyProperty.getValue());
  }

  @Test
  void rebaseline() {
    var dirtyProperty = new DirtyBooleanProperty(INITIAL_VALUE);

    assertFalse(dirtyProperty.isDirty());

    dirtyProperty.setValue(DIRTY_VALUE);

    assertTrue(dirtyProperty.isDirty());

    dirtyProperty.rebaseline();

    assertFalse(dirtyProperty.isDirty());
    assertEquals(DIRTY_VALUE, dirtyProperty.get());
    assertEquals(DIRTY_VALUE, dirtyProperty.getValue());
    assertEquals(DIRTY_VALUE, dirtyProperty.getOriginalValue());
  }

  @Test
  void reset() {
    var dirtyProperty = new DirtyBooleanProperty(INITIAL_VALUE);

    assertFalse(dirtyProperty.isDirty());

    dirtyProperty.setValue(DIRTY_VALUE);

    assertTrue(dirtyProperty.isDirty());

    dirtyProperty.reset();

    assertFalse(dirtyProperty.isDirty());
    assertEquals(INITIAL_VALUE, dirtyProperty.get());
    assertEquals(INITIAL_VALUE, dirtyProperty.getValue());
    assertEquals(INITIAL_VALUE, dirtyProperty.getOriginalValue());
  }

}