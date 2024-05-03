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

public class DetailViewBuilder<E> {
  protected final ListModel<E> viewModel;
  private final ChangeListener<String> changeListener;

  public DetailViewBuilder(ListModel<E> viewModel) {
    this.viewModel = viewModel;
    changeListener = (observable, oldValue, newValue) -> viewModel.update();
  }

  protected void bindBidirectional(StringProperty textFieldProperty, Function<E, Property<String>> detailModelProperty) {
    Var<String> property = selectVarOrElseConst(viewModel.selectedElementProperty(), detailModelProperty, "");
    textFieldProperty.bindBidirectional(property);
    property.observeChanges(changeListener);
  }
}