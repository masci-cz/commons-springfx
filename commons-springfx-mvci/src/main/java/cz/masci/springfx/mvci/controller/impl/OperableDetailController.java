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

package cz.masci.springfx.mvci.controller.impl;

import cz.masci.springfx.mvci.model.detail.DetailModel;
import cz.masci.springfx.mvci.model.detail.DirtyModel;
import cz.masci.springfx.mvci.model.detail.ValidModel;
import cz.masci.springfx.mvci.model.list.Removable;
import cz.masci.springfx.mvci.model.list.Selectable;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.reactfx.value.Val;

public class OperableDetailController<I, E extends DetailModel<I>, T extends Removable<E> & Selectable<E>> {

  private final Val<E> selectedElement;
  private final T viewModel;

  private final BooleanProperty saveDisabled = new SimpleBooleanProperty(true);
  private final BooleanProperty discardDisabled = new SimpleBooleanProperty(true);
  private final BooleanProperty deleteDisabled = new SimpleBooleanProperty(true);

  // TODO use as input parameters only selectedElementProperty <E> and remove consumer<E>
  public OperableDetailController(T viewModel) {
    this.selectedElement = Val.wrap(viewModel.selectedElementProperty());
    this.viewModel = viewModel;

    initDisableProperties();
  }

  public BooleanProperty saveDisabledProperty() {
    return saveDisabled;
  }

  public BooleanProperty discardDisabledProperty() {
    return discardDisabled;
  }

  public BooleanProperty deleteDisabledProperty() {
    return deleteDisabled;
  }

  public void discard() {
    if (isDiscardEnabled()) {
      selectedElement.ifPresent(element -> {
        if (element.isTransient()) {
          viewModel.remove(element);
        } else {
          element.reset();
        }
      });
    }
  }

  public void update(BiConsumer<E, Consumer<E>> updateAction) {
    if (isSaveEnabled()) {
      selectedElement.ifPresent(element -> updateAction.accept(element, updatedElement -> {
        if (element.isTransient()) {
          element.setId(updatedElement.getId());
        }
        element.rebaseline();
      }));
    }
  }

  public void remove(BiConsumer<E, Runnable> removeAction) {
    if (isDeleteEnabled()) {
      selectedElement.ifPresent(element -> removeAction.accept(element, () -> viewModel.remove(element)));
    }
  }

  private void initDisableProperties() {
    // delete disabled => not selected
    Val<Boolean> dirtyProperty = selectedElement.flatMap(DirtyModel::isDirtyProperty);
    Val<Boolean> validProperty = selectedElement.flatMap(ValidModel::validProperty);
    Val<Boolean> saveDisable = Val.combine(dirtyProperty, validProperty, (dirty, valid) -> !dirty || !valid);
    deleteDisabled.bind(Bindings.createBooleanBinding(selectedElement::isEmpty, selectedElement));
    saveDisabled.bind(Bindings.createBooleanBinding(() -> selectedElement.isEmpty() || saveDisable.getOrElse(true), selectedElement, saveDisable));
    discardDisabled.bind(Bindings.createBooleanBinding(() -> selectedElement.isEmpty() || !dirtyProperty.getOrElse(true), selectedElement, dirtyProperty));
  }

  private boolean isSaveEnabled() {
    return !saveDisabled.get();
  }

  private boolean isDiscardEnabled() {
    return !discardDisabled.get();
  }

  private boolean isDeleteEnabled() {
    return !deleteDisabled.get();
  }
}
