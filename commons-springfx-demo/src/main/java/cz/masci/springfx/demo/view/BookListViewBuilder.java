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

package cz.masci.springfx.demo.view;

import static cz.masci.springfx.mvci.util.MFXBuilderUtils.createTableColumn;
import static cz.masci.springfx.mvci.util.MFXBuilderUtils.initSelectionModel;

import cz.masci.springfx.demo.model.BookDetailModel;
import cz.masci.springfx.demo.model.BookListModel;
import cz.masci.springfx.mvci.view.impl.DirtyMFXTableRow;
import io.github.palexdev.materialfx.controls.MFXTableView;
import java.util.List;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BookListViewBuilder implements Builder<Region> {

  private final BookListModel viewModel;

  @Override
  public Region build() {
    // create table view
    var result = new MFXTableView<>(viewModel.getElements());
    result.setMaxHeight(Double.MAX_VALUE);
    result.setMaxWidth(Double.MAX_VALUE);

    // create columns with row cell factory
    var titleColumn = createTableColumn("Title", BookDetailModel::getTitle);
    titleColumn.setPrefWidth(300);
    var authorColumn = createTableColumn("Author", BookDetailModel::getAuthor);

    // add columns to the table
    result.getTableColumns().addAll(List.of(titleColumn, authorColumn));
    // set table row factory to change row style class when the row becomes dirty
    result.setTableRowFactory(data -> new DirtyMFXTableRow<>(result, data, "dirty-row"));
    result.getSelectionModel().setAllowsMultipleSelection(false);

    // set what should be done when an item in the table is selected
    // whenever the selection is made it feeds the list view model selected property,
    // what should happen when any selected property attribute changes
    // what should happen when list view model selectItem() is called
    initSelectionModel(result.getSelectionModel(), result::update, viewModel);

    return result;
  }
}
