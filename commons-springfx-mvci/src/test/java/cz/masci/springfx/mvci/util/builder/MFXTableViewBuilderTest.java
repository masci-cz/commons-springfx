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

package cz.masci.springfx.mvci.util.builder;

import static cz.masci.springfx.mvci.TestConstants.INITIAL_TEXT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cz.masci.springfx.mvci.TestDetailModel;
import cz.masci.springfx.mvci.model.list.impl.BaseListModel;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(ApplicationExtension.class)
class MFXTableViewBuilderTest {

  private final MFXTableViewBuilder<Integer, TestDetailModel> builder = MFXTableViewBuilder.builder(new BaseListModel<>());

  @Test
  void testMaxHeight() {
    double expectedMaxHeight = 500.0;

    assertEquals(expectedMaxHeight, builder.maxHeight(expectedMaxHeight)
                                           .build()
                                           .getMaxHeight());
  }

  @Test
  void testMaxWidth() {
    double expectedMaxWidth = 800.0;

    assertEquals(expectedMaxWidth, builder.maxWidth(expectedMaxWidth)
                                          .build()
                                          .getMaxWidth());
  }

  @Test
  void testAllowsMultipleSelection() {
    assertTrue(builder.allowsMultipleSelection(true)
                      .build()
                      .getSelectionModel()
                      .allowsMultipleSelection());
  }

  @Test
  void testColumn() {
    String expectedTitle = "Title";
    Function<TestDetailModel, String> expectedProperty = TestDetailModel::getText;
    double expectedPrefWidth = 200.0;

    MFXTableView<TestDetailModel> tableView = builder.column(expectedTitle, expectedProperty, expectedPrefWidth)
                                                     .build();

    MFXTableColumn<TestDetailModel> column = tableView.getTableColumns()
                                                      .get(0);

    var rowCellFactory = column.getRowCellFactory();
    var tableRowCell = rowCellFactory.apply(null);
    assertInstanceOf(MFXTableRowCell.class, tableRowCell);
    tableRowCell.update(new TestDetailModel());
    assertEquals(INITIAL_TEXT, tableRowCell.getText());
    assertEquals(expectedTitle, column.getText());
    assertEquals(expectedPrefWidth, column.getPrefWidth());
  }

  @Test
  void testColumn_noPrefWidth() {
    String expectedTitle = "Title";
    Function<TestDetailModel, String> expectedProperty = TestDetailModel::getText;

    MFXTableView<TestDetailModel> tableView = builder.column(expectedTitle, expectedProperty)
                                                     .build();

    MFXTableColumn<TestDetailModel> column = tableView.getTableColumns()
                                                      .get(0);

    var rowCellFactory = column.getRowCellFactory();
    var tableRowCell = rowCellFactory.apply(null);
    assertInstanceOf(MFXTableRowCell.class, tableRowCell);
    tableRowCell.update(new TestDetailModel());
    assertEquals(INITIAL_TEXT, tableRowCell.getText());
    assertEquals(expectedTitle, column.getText());
  }
}