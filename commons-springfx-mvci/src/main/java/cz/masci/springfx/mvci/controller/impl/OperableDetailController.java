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

public class OperableDetailController<E extends DetailModel<?>, T extends Removable<E> & Selectable<E>> {

  private final Val<E> selectedElement;
  private final T viewModel;

  private final BooleanProperty saveDisabled = new SimpleBooleanProperty(true);
  private final BooleanProperty discardDisabled = new SimpleBooleanProperty(true);
  private final BooleanProperty deleteDisabled = new SimpleBooleanProperty(true);

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
      selectedElement.ifPresent(item -> {
        if (item.isTransient()) {
          viewModel.remove(item);
        } else {
          item.reset();
        }
      });
    }
  }

  public void save(BiConsumer<E, Consumer<E>> save) {
    if (isSaveEnabled()) {
      selectedElement.ifPresent(item -> save.accept(item, savedItem -> {
        if (item.isTransient()) {
          item.setId(savedItem.getId());
        }
        item.rebaseline();
      }));
    }
  }

  public void delete(BiConsumer<E, Runnable> delete) {
    if (isDeleteEnabled()) {
      selectedElement.ifPresent(item -> delete.accept(item, () -> viewModel.remove(item)));
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
