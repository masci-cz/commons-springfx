/*
 * Copyright (c) 2024
 *
 * This file is part of DrD.
 *
 * DrD is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option)
 *   any later version.
 *
 * DrD is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *   FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 *    License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *  along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */

package cz.masci.springfx.mvci.model.list;

import cz.masci.springfx.mvci.model.detail.DetailModel;
import cz.masci.springfx.mvci.model.dirty.DirtyListProperty;
import java.util.function.Consumer;
import javafx.collections.ObservableList;
import lombok.Setter;
import org.reactfx.value.Var;

public class SimpleListModel<T, E extends DetailModel<T>> implements ListModel<E>, Focusable {
  protected final DirtyListProperty<E> elements = new DirtyListProperty<>();
  protected final Var<E> selectedElement = Var.newSimpleVar(null);
  @Setter
  protected Consumer<E> onSelectElement;
  @Setter
  protected Runnable onUpdateElementsProperty;
  @Setter
  protected Consumer<E> onRemoveElement;
  @Setter
  protected Runnable onFocusView;

  @Override
  public ObservableList<E> getElements() {
    return elements.get();
  }

  @Override
  public Var<E> selectedElementProperty() {
    return selectedElement;
  }

  @Override
  public void removeElement(E element) {
    selectedElement.setValue(null);
    elements.remove(element);
    if (onRemoveElement != null) {
      onRemoveElement.accept(element);
    }
  }

  @Override
  public void updateElementsProperty() {
    if (onUpdateElementsProperty != null) {
      onUpdateElementsProperty.run();
    }
  }

  @Override
  public void selectElement(E item) {
    if (onSelectElement != null) {
      onSelectElement.accept(item);
    }
  }

  @Override
  public void focusView() {
    if (onFocusView != null) {
      onFocusView.run();
    }
  }
}
