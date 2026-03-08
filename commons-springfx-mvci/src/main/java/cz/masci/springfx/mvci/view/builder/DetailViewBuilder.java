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

package cz.masci.springfx.mvci.view.builder;

import static cz.masci.springfx.mvci.util.reactfx.ReactFxUtils.selectVarOrElseConst;

import cz.masci.springfx.mvci.model.list.ListModel;
import java.util.function.Function;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import org.reactfx.value.Var;

/**
 * Base builder for detail views that binds text field properties bidirectionally to the
 * selected element's properties in the provided list view model.
 *
 * @param <E> the type of the detail model element
 */
public class DetailViewBuilder<E> {
  /** The list view model providing the selected element and update callback. */
  protected final ListModel<E> viewModel;
  /** Listener that triggers an update on the view model whenever a bound property changes. */
  private final ChangeListener<String> changeListener;

  /**
   * Creates a new {@code DetailViewBuilder} for the given list view model.
   *
   * @param viewModel the list model providing the selected element
   */
  public DetailViewBuilder(ListModel<E> viewModel) {
    this.viewModel = viewModel;
    changeListener = (observable, oldValue, newValue) -> viewModel.update();
  }

  /**
   * Binds a text field's string property bidirectionally to the corresponding property
   * of the currently selected detail model element.
   *
   * @param textFieldProperty   the string property of the text field to bind
   * @param detailModelProperty function extracting the target property from the detail model
   */
  protected void bindBidirectional(StringProperty textFieldProperty, Function<E, Property<String>> detailModelProperty) {
    Var<String> property = selectVarOrElseConst(viewModel.selectedElementProperty(), detailModelProperty, "");
    textFieldProperty.bindBidirectional(property);
    property.observeChanges(changeListener);
  }
}