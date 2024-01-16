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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cz.masci.springfx.mvci.model.dirty.DirtyStringProperty;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.Stop;

@ExtendWith(ApplicationExtension.class)
class DirtyMFXTableRowTest {
  public static final String DIRTY_STYLE_CLASS = "DIRTY_STYLE_CLASS";

  private MFXTableView<DirtyStringProperty> tableView;
  private ObservableList<DirtyStringProperty> items;

  @Start
  private void start(Stage stage) {
    items = FXCollections.observableArrayList();
    tableView = new MFXTableView<>(items);
    tableView.setMaxHeight(Double.MAX_VALUE);
    tableView.setMaxWidth(Double.MAX_VALUE);

    BorderPane pane = new BorderPane(tableView);
    stage.setScene(new Scene(pane, 640, 400));
    stage.show();
  }

  @Stop
  private void stop() {
  }

  @Test
  void dirtyRowStyleClass_noItem() {
    var tableRow = new DirtyMFXTableRow<>(tableView, null, DIRTY_STYLE_CLASS);

    assertFalse(tableRow.getStyleClass().contains(DIRTY_STYLE_CLASS));
  }

  @Test
  void dirtyRowStyleClass_item_classNotPresent() {
    var item = new DirtyStringProperty("Initial");
    var tableRow = new DirtyMFXTableRow<>(tableView, item, DIRTY_STYLE_CLASS);

    assertFalse(tableRow.getStyleClass().contains(DIRTY_STYLE_CLASS));
  }

  @Test
  void dirtyRowStyleClass_item_classPresent_alreadyDirty() {
    var item = new DirtyStringProperty("Initial");
    item.set("");
    var tableRow = new DirtyMFXTableRow<>(tableView, item, DIRTY_STYLE_CLASS);

    assertTrue(tableRow.getStyleClass().contains(DIRTY_STYLE_CLASS));
  }

  @Test
  void dirtyRowStyleClass_item_classPresent_dirtyAfterChange() {
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
//    var tableView = new MFXTableView<DirtyStringProperty>();
    var item = new DirtyStringProperty("Initial");
    var tableRow = new DirtyMFXTableRow<>(tableView, null, DIRTY_STYLE_CLASS);

    tableRow.updateItem(item);
    item.set("");

    assertTrue(tableRow.getStyleClass().contains(DIRTY_STYLE_CLASS));

    item.reset();
    assertFalse(tableRow.getStyleClass().contains(DIRTY_STYLE_CLASS));
  }

  @Test
  void dirtyRowStyleClass_manyItems(FxRobot robot) {
    var item1 = new DirtyStringProperty("String 0");
    var item2 = new DirtyStringProperty("String 1");
    var item3 = new DirtyStringProperty("String 2");
    var item4 = new DirtyStringProperty("String 3");
    item4.set("Change 1");

    robot.interact(() -> {
          MFXTableColumn<DirtyStringProperty> column = new MFXTableColumn<>("Dirty");
          column.setRowCellFactory(item -> new MFXTableRowCell<>(DirtyStringProperty::get));
          tableView.getTableColumns().add(column);
          tableView.setTableRowFactory(data -> new DirtyMFXTableRow<>(tableView, data, DIRTY_STYLE_CLASS));
          items.addAll(item1, item2, item3);
        });

    // initial state
    assertNotNull(tableView.getCell(0));
    assertFalse(tableView.getCell(0).getStyleClass().contains(DIRTY_STYLE_CLASS));
    assertNotNull(tableView.getCell(1));
    assertFalse(tableView.getCell(1).getStyleClass().contains(DIRTY_STYLE_CLASS));
    assertNotNull(tableView.getCell(2));
    assertFalse(tableView.getCell(2).getStyleClass().contains(DIRTY_STYLE_CLASS));
    // add dirty item
    robot.interact(() -> items.add(item4));
    assertNotNull(tableView.getCell(3));
    assertTrue(tableView.getCell(3).getStyleClass().contains(DIRTY_STYLE_CLASS));
    // remove dirty item
    robot.interact(() -> items.remove(item4));
    assertNotNull(tableView.getCell(0));
    assertFalse(tableView.getCell(0).getStyleClass().contains(DIRTY_STYLE_CLASS));
    assertNotNull(tableView.getCell(1));
    assertFalse(tableView.getCell(1).getStyleClass().contains(DIRTY_STYLE_CLASS));
    assertNotNull(tableView.getCell(2));
    assertFalse(tableView.getCell(2).getStyleClass().contains(DIRTY_STYLE_CLASS));
    // change item to dirty state
    robot.interact(() -> item3.set("Change 2"));
    assertNotNull(tableView.getCell(0));
    assertFalse(tableView.getCell(0).getStyleClass().contains(DIRTY_STYLE_CLASS));
    assertNotNull(tableView.getCell(1));
    assertFalse(tableView.getCell(1).getStyleClass().contains(DIRTY_STYLE_CLASS));
    assertNotNull(tableView.getCell(2));
    assertTrue(tableView.getCell(2).getStyleClass().contains(DIRTY_STYLE_CLASS));
    // discard dirty item state
    robot.interact(item3::reset);
    assertNotNull(tableView.getCell(0));
    assertFalse(tableView.getCell(0).getStyleClass().contains(DIRTY_STYLE_CLASS));
    assertNotNull(tableView.getCell(1));
    assertFalse(tableView.getCell(1).getStyleClass().contains(DIRTY_STYLE_CLASS));
    assertNotNull(tableView.getCell(2));
    assertFalse(tableView.getCell(2).getStyleClass().contains(DIRTY_STYLE_CLASS));
  }
}