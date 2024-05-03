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

import static cz.masci.springfx.mvci.util.MFXBuilderUtils.initSelectionModel;

import cz.masci.springfx.mvci.model.detail.DetailModel;
import cz.masci.springfx.mvci.model.list.impl.BaseListModel;
import cz.masci.springfx.mvci.util.MFXBuilderUtils;
import cz.masci.springfx.mvci.view.impl.DirtyMFXTableRow;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MFXTableViewBuilder<I, E extends DetailModel<I>> {

  private final BaseListModel<I, E> viewModel;
  private double maxHeight = Double.MAX_VALUE;
  private double maxWidth = Double.MAX_VALUE;
  private final List<ColumnData<I, E>> tableColumnsData = new ArrayList<>();
  private boolean allowsMultipleSelection = false;

  public static <I, E extends DetailModel<I>> MFXTableViewBuilder<I, E> builder(BaseListModel<I, E> viewModel) {
    return new MFXTableViewBuilder<>(viewModel);
  }

  public MFXTableViewBuilder<I, E> maxHeight(double maxHeight) {
    this.maxHeight = maxHeight;
    return this;
  }

  public MFXTableViewBuilder<I, E> maxWidth(double maxWidth) {
    this.maxWidth = maxWidth;
    return this;
  }

  public MFXTableViewBuilder<I, E> column(String title, Function<E, String> property) {
    return column(title, property, null);
  }

  public MFXTableViewBuilder<I, E> column(String title, Function<E, String> property, Double prefWidth) {
    tableColumnsData.add(new ColumnData<>(title, property, prefWidth));
    return this;
  }

  public MFXTableViewBuilder<I, E> allowsMultipleSelection(boolean allowsMultipleSelection) {
    this.allowsMultipleSelection = allowsMultipleSelection;
    return this;
  }

  public MFXTableView<E> build() {
    List<MFXTableColumn<E>> tableColumns = tableColumnsData.stream().map(this::createTableColumn).collect(Collectors.toList());

    var result = new MFXTableView<>(viewModel.getElements());
    result.setMaxHeight(maxHeight);
    result.setMaxWidth(maxWidth);
    result.getTableColumns().addAll(tableColumns);
    result.setTableRowFactory(data -> new DirtyMFXTableRow<>(result, data, "dirty-row"));
    result.getSelectionModel().setAllowsMultipleSelection(allowsMultipleSelection);

    initSelectionModel(result.getSelectionModel(), result::update, viewModel);

    return result;
  }

  private MFXTableColumn<E> createTableColumn(ColumnData<I, E> data) {
    MFXTableColumn<E> tableColumn = MFXBuilderUtils.createTableColumn(data.title, data.property);
    if (data.prefWidth != null) {
      tableColumn.setPrefWidth(data.prefWidth);
    }
    return tableColumn;
  }

  private record ColumnData<I, E extends DetailModel<I>>(String title, Function<E, String> property, Double prefWidth) {}
}
