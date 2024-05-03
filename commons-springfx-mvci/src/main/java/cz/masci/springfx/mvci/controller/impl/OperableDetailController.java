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
import cz.masci.springfx.mvci.model.detail.IdentifiableModel;
import cz.masci.springfx.mvci.model.detail.ValidModel;
import cz.masci.springfx.mvci.model.list.Removable;
import jakarta.annotation.Nonnull;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.reactfx.value.Val;
import org.reactfx.value.Var;

/**
 * The OperableDetailController class is responsible for controlling the operations and state of a detail view.
 * It handles actions such as saving, discarding, and deleting detail elements.
 *
 * @param <I> The type of the identifier for the detail element.
 * @param <E> The type of the detail element.
 */
public class OperableDetailController<I, E extends DetailModel<I>> {

  private final Val<E> selectedElement;
  private final Removable<E> removable;

  private final BooleanProperty saveDisabled = new SimpleBooleanProperty(true);
  private final BooleanProperty discardDisabled = new SimpleBooleanProperty(true);
  private final BooleanProperty deleteDisabled = new SimpleBooleanProperty(true);

  public OperableDetailController(Var<E> selectedElement, @Nonnull Removable<E> removable) {
    this.selectedElement = Val.wrap(selectedElement);
    this.removable = removable;

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

  /**
   * Discards the selected element. If discard is enabled, the method checks if the selected element is transient. If it is, the element is removed from the removable collection.
   *  If it is not transient, the element is reset to its initial state.
   */
  public void discard() {
    if (isDiscardEnabled()) {
      selectedElement.ifPresent(element -> {
        if (element.isTransient()) {
          removable.remove(element);
        } else {
          element.reset();
        }
      });
    }
  }

  /**
   * Updates the selected element with the given update action if save is enabled.
   *
   * @param updateAction the update action to perform on the selected element
   *                    The action takes two parameters:
   *                    - the selected element
   *                    - a consumer to accept the updated element, means to run in FX thread
   */
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

  /**
   * Removes the selected element if delete is enabled. The method executes the given remove action on the selected element and provides a runnable to remove the element from the
   *  removable collection.
   *
   * @param removeAction the remove action to perform on the selected element
   *                    The action takes two parameters:
   *                    - the selected element
   *                    - a runnable to remove the element, means to run in FX thread
   */
  public void remove(BiConsumer<E, Runnable> removeAction) {
    if (isDeleteEnabled()) {
      selectedElement.ifPresent(element -> removeAction.accept(element, () -> removable.remove(element)));
    }
  }

  private void initDisableProperties() {
    // delete disabled => not selected
    Val<Boolean> dirtyProperty = selectedElement.flatMap(DirtyModel::isDirtyProperty);
    Val<Boolean> validProperty = selectedElement.flatMap(ValidModel::validProperty);
    Val<Boolean> transientProperty = selectedElement.flatMap(IdentifiableModel::transientProperty);
    Val<Boolean> saveDisable = Val.combine(dirtyProperty, validProperty, (dirty, valid) -> !dirty || !valid);
    deleteDisabled.bind(Bindings.createBooleanBinding(() -> selectedElement.isEmpty() || transientProperty.getOrElse(true), selectedElement, transientProperty));
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
