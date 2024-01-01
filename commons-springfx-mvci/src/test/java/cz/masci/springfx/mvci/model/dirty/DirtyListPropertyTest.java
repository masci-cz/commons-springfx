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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DirtyListPropertyTest {

  private final static String INITIAL_VALUE = "INITIAL";
  private final static String DIRTY_VALUE = "DIRTY";

  @Test
  void isDirty_emptyList() {
    DirtyListProperty<DirtyStringProperty> dirtyListProperty = new DirtyListProperty<>();

    assertFalse(dirtyListProperty.isDirty());
  }

  // region addElement

  @Test
  void isDirty_addElement() {
    DirtyListProperty<DirtyStringProperty> dirtyListProperty = new DirtyListProperty<>();

    dirtyListProperty.add(new DirtyStringProperty(INITIAL_VALUE));

    assertFalse(dirtyListProperty.isDirty());
  }

  @Test
  void isDirty_addElement_dirty() {
    DirtyStringProperty dirtyProperty = new DirtyStringProperty(INITIAL_VALUE);
    DirtyListProperty<DirtyStringProperty> dirtyListProperty = new DirtyListProperty<>();

    dirtyProperty.set(DIRTY_VALUE);

    dirtyListProperty.add(dirtyProperty);

    assertTrue(dirtyListProperty.isDirty());
  }

  @Test
  void isDirty_addElement_multiple() {
    DirtyStringProperty dirtyProperty1 = new DirtyStringProperty(INITIAL_VALUE);
    DirtyStringProperty dirtyProperty2 = new DirtyStringProperty(INITIAL_VALUE);

    DirtyListProperty<DirtyStringProperty> dirtyListProperty = new DirtyListProperty<>();

    dirtyListProperty.add(dirtyProperty1);
    assertFalse(dirtyListProperty.isDirty());
    dirtyListProperty.add(dirtyProperty2);
    assertFalse(dirtyListProperty.isDirty());
  }

  @Test
  void isDirty_addElement_multiple_firstDirty() {
    DirtyStringProperty dirtyProperty1 = new DirtyStringProperty(INITIAL_VALUE);
    DirtyStringProperty dirtyProperty2 = new DirtyStringProperty(INITIAL_VALUE);

    DirtyListProperty<DirtyStringProperty> dirtyListProperty = new DirtyListProperty<>();

    dirtyProperty1.set(DIRTY_VALUE);

    dirtyListProperty.add(dirtyProperty1);
    assertTrue(dirtyListProperty.isDirty());
    assertTrue(dirtyProperty1.isDirty());
    assertFalse(dirtyProperty2.isDirty());

    dirtyListProperty.add(dirtyProperty2);
    assertTrue(dirtyListProperty.isDirty());
    assertTrue(dirtyProperty1.isDirty());
    assertFalse(dirtyProperty2.isDirty());
  }

  @Test
  void isDirty_addElement_multiple_secondDirty() {
    DirtyStringProperty dirtyProperty1 = new DirtyStringProperty(INITIAL_VALUE);
    DirtyStringProperty dirtyProperty2 = new DirtyStringProperty(INITIAL_VALUE);

    DirtyListProperty<DirtyStringProperty> dirtyListProperty = new DirtyListProperty<>();

    dirtyProperty2.set(DIRTY_VALUE);

    dirtyListProperty.add(dirtyProperty1);
    assertFalse(dirtyListProperty.isDirty());
    assertFalse(dirtyProperty1.isDirty());
    assertTrue(dirtyProperty2.isDirty());

    dirtyListProperty.add(dirtyProperty2);
    assertTrue(dirtyListProperty.isDirty());
    assertFalse(dirtyProperty1.isDirty());
    assertTrue(dirtyProperty2.isDirty());
  }

  // endregion

  // region updateElement

  @Test
  void isDirty_updateElement() {
    DirtyStringProperty dirtyProperty = new DirtyStringProperty(INITIAL_VALUE);
    DirtyListProperty<DirtyStringProperty> dirtyListProperty = new DirtyListProperty<>();

    dirtyListProperty.add(dirtyProperty);

    dirtyProperty.set(DIRTY_VALUE);
    assertTrue(dirtyListProperty.isDirty());
  }

  @Test
  void isDirty_updateElement_multiple_firstDirty() {
    DirtyStringProperty dirtyProperty1 = new DirtyStringProperty(INITIAL_VALUE);
    DirtyStringProperty dirtyProperty2 = new DirtyStringProperty(INITIAL_VALUE);

    DirtyListProperty<DirtyStringProperty> dirtyListProperty = new DirtyListProperty<>();

    dirtyListProperty.add(dirtyProperty1);
    dirtyListProperty.add(dirtyProperty2);

    dirtyProperty1.set(DIRTY_VALUE);
    assertTrue(dirtyListProperty.isDirty());
    assertTrue(dirtyProperty1.isDirty());
    assertFalse(dirtyProperty2.isDirty());
  }

  @Test
  void isDirty_updateElement_multiple_secondDirty() {
    DirtyStringProperty dirtyProperty1 = new DirtyStringProperty(INITIAL_VALUE);
    DirtyStringProperty dirtyProperty2 = new DirtyStringProperty(INITIAL_VALUE);

    DirtyListProperty<DirtyStringProperty> dirtyListProperty = new DirtyListProperty<>();

    dirtyListProperty.add(dirtyProperty1);
    dirtyListProperty.add(dirtyProperty2);

    dirtyProperty2.set(DIRTY_VALUE);
    assertTrue(dirtyListProperty.isDirty());
    assertFalse(dirtyProperty1.isDirty());
    assertTrue(dirtyProperty2.isDirty());
  }

  // endregion

  // region removeElement

  @Test
  void isDirty_removeElement() {
    DirtyStringProperty dirtyProperty = new DirtyStringProperty(INITIAL_VALUE);
    DirtyListProperty<DirtyStringProperty> dirtyListProperty = new DirtyListProperty<>();

    dirtyListProperty.add(dirtyProperty);
    dirtyListProperty.remove(dirtyProperty);

    assertFalse(dirtyListProperty.isDirty());
    assertFalse(dirtyProperty.isDirty());
  }

  @Test
  void isDirty_removeElement_dirty() {
    DirtyStringProperty dirtyProperty = new DirtyStringProperty(INITIAL_VALUE);
    DirtyListProperty<DirtyStringProperty> dirtyListProperty = new DirtyListProperty<>();

    dirtyListProperty.add(dirtyProperty);
    dirtyProperty.set(DIRTY_VALUE);
    dirtyListProperty.remove(dirtyProperty);

    assertFalse(dirtyListProperty.isDirty());
    assertTrue(dirtyProperty.isDirty());
  }

  @Test
  void isDirty_removeElement_multiple() {
    DirtyStringProperty dirtyProperty1 = new DirtyStringProperty(INITIAL_VALUE);
    DirtyStringProperty dirtyProperty2 = new DirtyStringProperty(INITIAL_VALUE);

    DirtyListProperty<DirtyStringProperty> dirtyListProperty = new DirtyListProperty<>();

    dirtyListProperty.add(dirtyProperty1);
    dirtyListProperty.add(dirtyProperty2);

    assertFalse(dirtyListProperty.isDirty());

    dirtyListProperty.remove(dirtyProperty1);
    assertFalse(dirtyListProperty.isDirty());

    dirtyListProperty.remove(dirtyProperty2);
    assertFalse(dirtyListProperty.isDirty());
  }

  @Test
  void isDirty_removeElement_multiple_allDirty_removeDirty() {
    DirtyStringProperty dirtyProperty1 = new DirtyStringProperty(INITIAL_VALUE);
    DirtyStringProperty dirtyProperty2 = new DirtyStringProperty(INITIAL_VALUE);

    DirtyListProperty<DirtyStringProperty> dirtyListProperty = new DirtyListProperty<>();

    dirtyListProperty.add(dirtyProperty1);
    dirtyListProperty.add(dirtyProperty2);

    dirtyProperty1.set(DIRTY_VALUE);
    dirtyProperty2.set(DIRTY_VALUE);

    assertTrue(dirtyListProperty.isDirty());

    dirtyListProperty.remove(dirtyProperty1);
    assertTrue(dirtyListProperty.isDirty());

    dirtyListProperty.remove(dirtyProperty2);
    assertFalse(dirtyListProperty.isDirty());
  }

  @Test
  void isDirty_removeElement_multiple_oneDirty_removeDirty() {
    DirtyStringProperty dirtyProperty1 = new DirtyStringProperty(INITIAL_VALUE);
    DirtyStringProperty dirtyProperty2 = new DirtyStringProperty(INITIAL_VALUE);

    DirtyListProperty<DirtyStringProperty> dirtyListProperty = new DirtyListProperty<>();

    dirtyListProperty.add(dirtyProperty1);
    dirtyListProperty.add(dirtyProperty2);

    dirtyProperty1.set(DIRTY_VALUE);

    assertTrue(dirtyListProperty.isDirty());

    dirtyListProperty.remove(dirtyProperty1);
    assertFalse(dirtyListProperty.isDirty());

    dirtyListProperty.remove(dirtyProperty2);
    assertFalse(dirtyListProperty.isDirty());
  }

  @Test
  void isDirty_removeElement_multiple_oneDirty_removeNotDirty() {
    DirtyStringProperty dirtyProperty1 = new DirtyStringProperty(INITIAL_VALUE);
    DirtyStringProperty dirtyProperty2 = new DirtyStringProperty(INITIAL_VALUE);

    DirtyListProperty<DirtyStringProperty> dirtyListProperty = new DirtyListProperty<>();

    dirtyListProperty.add(dirtyProperty1);
    dirtyListProperty.add(dirtyProperty2);

    dirtyProperty2.set(DIRTY_VALUE);

    assertTrue(dirtyListProperty.isDirty());

    dirtyListProperty.remove(dirtyProperty1);
    assertTrue(dirtyListProperty.isDirty());

    dirtyListProperty.remove(dirtyProperty2);
    assertFalse(dirtyListProperty.isDirty());
  }

  // endregion
}