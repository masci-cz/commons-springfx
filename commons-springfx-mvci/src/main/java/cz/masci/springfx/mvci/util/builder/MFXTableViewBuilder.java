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

import cz.masci.springfx.mvci.model.detail.DetailModel;
import cz.masci.springfx.mvci.model.list.impl.BaseListModel;
import cz.masci.springfx.mvci.util.MFXBuilderUtils;
import cz.masci.springfx.mvci.view.impl.DirtyMFXTableRow;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static cz.masci.springfx.mvci.util.MFXBuilderUtils.initSelectionModel;

/**
 * Builder for creating and configuring an {@link MFXTableView} bound to a {@link BaseListModel}.
 *
 * @param <I> the type of the element identifier
 * @param <E> the type of the detail model elements
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MFXTableViewBuilder<I, E extends DetailModel<I>> {

  /** The list view model to bind the table view to. */
  private final BaseListModel<I, E> viewModel;
  /** Maximum height of the table view. */
  private double maxHeight = Double.MAX_VALUE;
  /** Maximum width of the table view. */
  private double maxWidth = Double.MAX_VALUE;
  /** Metadata for each column to be added to the table. */
  private final List<ColumnData<I, E>> tableColumnsData = new ArrayList<>();
  /** Whether the table allows multiple row selections. */
  private boolean allowsMultipleSelection = false;

  /**
   * Creates a new {@code MFXTableViewBuilder} for the given view model.
   *
   * @param viewModel the list view model to bind to
   * @param <I>       the type of the element identifier
   * @param <E>       the type of the detail model elements
   * @return a new builder instance
   */
  public static <I, E extends DetailModel<I>> MFXTableViewBuilder<I, E> builder(BaseListModel<I, E> viewModel) {
    return new MFXTableViewBuilder<>(viewModel);
  }

  /**
   * Sets the maximum height of the table view.
   *
   * @param maxHeight the maximum height
   * @return this builder
   */
  public MFXTableViewBuilder<I, E> maxHeight(double maxHeight) {
    this.maxHeight = maxHeight;
    return this;
  }

  /**
   * Sets the maximum width of the table view.
   *
   * @param maxWidth the maximum width
   * @return this builder
   */
  public MFXTableViewBuilder<I, E> maxWidth(double maxWidth) {
    this.maxWidth = maxWidth;
    return this;
  }

  /**
   * Adds a column with the given title and property extractor.
   *
   * @param title    the column header title
   * @param property function extracting the display string from an element
   * @return this builder
   */
  public MFXTableViewBuilder<I, E> column(String title, Function<E, String> property) {
    return column(title, property, null);
  }

  /**
   * Adds a column with the given title, property extractor, and preferred width.
   *
   * @param title      the column header title
   * @param property   function extracting the display string from an element
   * @param prefWidth  preferred column width, or {@code null} to use the default
   * @return this builder
   */
  public MFXTableViewBuilder<I, E> column(String title, Function<E, String> property, Double prefWidth) {
    tableColumnsData.add(new ColumnData<>(title, property, prefWidth));
    return this;
  }

  /**
   * Sets whether the table allows multiple row selections.
   *
   * @param allowsMultipleSelection {@code true} to allow multiple selections
   * @return this builder
   */
  public MFXTableViewBuilder<I, E> allowsMultipleSelection(boolean allowsMultipleSelection) {
    this.allowsMultipleSelection = allowsMultipleSelection;
    return this;
  }

  // TODO Add Consumer<MFXTableView<E>> to let in build use the created table for something else
  /**
   * Builds and returns the configured {@link MFXTableView}.
   *
   * @return a fully configured {@link MFXTableView}
   */
  public MFXTableView<E> build() {
    List<MFXTableColumn<E>> tableColumns = tableColumnsData.stream().map(this::createTableColumn).toList();

    var result = new MFXTableView<>(viewModel.getElements());
    result.setMaxHeight(maxHeight);
    result.setMaxWidth(maxWidth);
    result.getTableColumns().addAll(tableColumns);
    result.setTableRowFactory(data -> new DirtyMFXTableRow<>(result, data, "dirty-row"));
    result.getSelectionModel().setAllowsMultipleSelection(allowsMultipleSelection);

    initSelectionModel(result.getSelectionModel(), result::update, viewModel);

    return result;
  }

  /**
   * Creates an {@link MFXTableColumn} from the given column metadata.
   *
   * @param data the column metadata
   * @return a configured {@link MFXTableColumn}
   */
  private MFXTableColumn<E> createTableColumn(ColumnData<I, E> data) {
    MFXTableColumn<E> tableColumn = MFXBuilderUtils.createTableColumn(data.title, data.property);
    if (data.prefWidth != null) {
      tableColumn.setPrefWidth(data.prefWidth);
    }
    return tableColumn;
  }

  // Add Alignment attribute
  private record ColumnData<I, E extends DetailModel<I>>(String title, Function<E, String> property, Double prefWidth) {}
}
