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

import io.github.palexdev.materialfx.controls.MFXTableView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.nield.dirtyfx.beans.DirtyStringProperty;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(ApplicationExtension.class)
class DirtyMFXTableRowTest {
  public static final String DIRTY_STYLE_CLASS = "DIRTY_STYLE_CLASS";

  @Test
  void dirtyRowStyleClass_noItem() {
    var tableView = new MFXTableView<DirtyStringProperty>();
    var tableRow = new DirtyMFXTableRow<>(tableView, null, DIRTY_STYLE_CLASS);

    assertFalse(tableRow.getStyleClass().contains(DIRTY_STYLE_CLASS));
  }

  @Test
  void dirtyRowStyleClass_item_classNotPresent() {
    var tableView = new MFXTableView<DirtyStringProperty>();
    var item = new DirtyStringProperty("Initial");
    var tableRow = new DirtyMFXTableRow<>(tableView, item, DIRTY_STYLE_CLASS);

    assertFalse(tableRow.getStyleClass().contains(DIRTY_STYLE_CLASS));
  }

  @Test
  void dirtyRowStyleClass_item_classPresent_alreadyDirty() {
    var tableView = new MFXTableView<DirtyStringProperty>();
    var item = new DirtyStringProperty("Initial");
    item.set("");
    var tableRow = new DirtyMFXTableRow<>(tableView, item, DIRTY_STYLE_CLASS);

    assertTrue(tableRow.getStyleClass().contains(DIRTY_STYLE_CLASS));
  }

  @Test
  void dirtyRowStyleClass_item_classPresent_dirtyAfterChange() {
    var tableView = new MFXTableView<DirtyStringProperty>();
    var item = new DirtyStringProperty("Initial");
    var tableRow = new DirtyMFXTableRow<>(tableView, null, DIRTY_STYLE_CLASS);

    tableRow.updateItem(item);
    item.set("");

    assertTrue(tableRow.getStyleClass().contains(DIRTY_STYLE_CLASS));

    tableRow.updateItem(null);

    assertFalse(tableRow.getStyleClass().contains(DIRTY_STYLE_CLASS));
  }

  @Test
  void dirtyRowStyleClass_item_classPresent_notDirtyAfterChange() {
    var tableView = new MFXTableView<DirtyStringProperty>();
    var item = new DirtyStringProperty("Initial");
    var tableRow = new DirtyMFXTableRow<>(tableView, null, DIRTY_STYLE_CLASS);

    tableRow.updateItem(item);
    item.set("");

    assertTrue(tableRow.getStyleClass().contains(DIRTY_STYLE_CLASS));

    item.reset();
    assertFalse(tableRow.getStyleClass().contains(DIRTY_STYLE_CLASS));
  }

}