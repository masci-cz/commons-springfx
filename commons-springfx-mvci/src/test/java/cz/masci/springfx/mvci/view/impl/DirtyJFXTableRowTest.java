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

package cz.masci.springfx.mvci.view.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nield.dirtyfx.beans.DirtyStringProperty;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(ApplicationExtension.class)
class DirtyJFXTableRowTest {

  public static final String DIRTY_STYLE_CLASS = "DIRTY_STYLE_CLASS";

  @Test
  void dirtyRowStyleClass_noItem() {
    var tableRow = new DirtyJFXTableRow<DirtyStringProperty>(DIRTY_STYLE_CLASS);

    assertFalse(tableRow.getStyleClass().contains(DIRTY_STYLE_CLASS));
  }

  @Test
  void dirtyRowStyleClass_item_classNotPresent() {
    var item = new DirtyStringProperty("Initial");
    var tableRow = new DirtyJFXTableRow<DirtyStringProperty>(DIRTY_STYLE_CLASS);

    tableRow.setItem(item);

    assertFalse(tableRow.getStyleClass().contains(DIRTY_STYLE_CLASS));
  }

  @Test
  void dirtyRowStyleClass_item_classPresent_alreadyDirty() {
    var item = new DirtyStringProperty("Initial");
    var tableRow = new DirtyJFXTableRow<DirtyStringProperty>(DIRTY_STYLE_CLASS);

    item.set("");
    tableRow.setItem(item);

    assertTrue(tableRow.getStyleClass().contains(DIRTY_STYLE_CLASS));
  }

  @Test
  void dirtyRowStyleClass_item_classPresent_dirtyAfterChange() {
    var item = new DirtyStringProperty("Initial");
    var tableRow = new DirtyJFXTableRow<DirtyStringProperty>(DIRTY_STYLE_CLASS);

    tableRow.setItem(item);
    item.set("");

    assertTrue(tableRow.getStyleClass().contains(DIRTY_STYLE_CLASS));

    tableRow.setItem(null);

    assertFalse(tableRow.getStyleClass().contains(DIRTY_STYLE_CLASS));
  }

  @Test
  void dirtyRowStyleClass_item_classPresent_notDirtyAfterChange() {
    var item = new DirtyStringProperty("Initial");
    var tableRow = new DirtyJFXTableRow<DirtyStringProperty>(DIRTY_STYLE_CLASS);

    tableRow.setItem(item);
    item.set("");

    assertTrue(tableRow.getStyleClass().contains(DIRTY_STYLE_CLASS));

    item.reset();
    assertFalse(tableRow.getStyleClass().contains(DIRTY_STYLE_CLASS));
  }
}