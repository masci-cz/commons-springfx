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

package cz.masci.springfx.mvci.util;

import cz.masci.springfx.mvci.model.detail.DetailModel;
import cz.masci.springfx.mvci.model.list.SimpleListModel;
import io.github.palexdev.materialfx.builders.control.TextFieldBuilder;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.enums.FloatMode;
import io.github.palexdev.materialfx.selection.base.IMultipleSelectionModel;
import java.util.Comparator;
import java.util.function.Function;
import javafx.collections.MapChangeListener.Change;
import javafx.collections.ObservableMap;
import lombok.experimental.UtilityClass;
import org.reactfx.EventStreams;

@UtilityClass
public class MFXBuilderUtils {

  /**
   * Creates and returns a MFXTextField with the specified floating text and max width.
   *
   * @param floatingText the floating text to be displayed in the text field
   * @param maxWidth the max width used for display
   * @return a new MFXTextField object with the specified floating text
   */
  public static MFXTextField createTextField(String floatingText, Double maxWidth) {
    return TextFieldBuilder.textField()
        .setFloatMode(FloatMode.BORDER)
        .setFloatingText(floatingText)
        .setMaxWidth(maxWidth)
        .getNode();
  }

  public static <T> MFXTableColumn<T> createTableColumn(String title, Function<T, String> extractor) {
    var result = new MFXTableColumn<>(title, Comparator.comparing(extractor));
    result.setRowCellFactory(item -> new MFXTableRowCell<>(extractor));

    return result;
  }

  public static <T, E extends DetailModel<T>> void initSelectionModel(IMultipleSelectionModel<E> selectionModel, Runnable update,
                                                                      SimpleListModel<T, E> viewModel) {
    ObservableMap<Integer, E> selectionProperty = selectionModel.selectionProperty();
    EventStreams.changesOf(selectionProperty)
                .filter(Change::wasAdded)
                .map(Change::getValueAdded)
                .feedTo(viewModel.selectedElementProperty());
    viewModel.setOnUpdateElementsProperty(update);
    viewModel.setOnSelectElement(selectionModel::selectItem);
  }

}
