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

package cz.masci.springfx.mvci.model.list.impl;

import cz.masci.springfx.mvci.model.detail.DetailModel;
import cz.masci.springfx.mvci.model.list.Elements;
import cz.masci.springfx.mvci.model.list.ListModel;
import java.util.function.Consumer;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import lombok.Setter;

/**
 * BaseListModel is a generic implementation of the {@link ListModel} and {@link Elements} interfaces.
 * It provides basic functionality for managing a list of elements and interacting with them.
 *
 * @param <I> The type of the element id identifier.
 * @param <E> The type of the elements in the list extending {@link DetailModel<I>}.
 */
public class BaseListModel<I, E extends DetailModel<I>> implements ListModel<E>, Elements<E> {
  /** The observable list of elements. */
  protected final ObservableList<E> elements;
  /** The property holding the currently selected element. */
  protected final ObjectProperty<E> selectedElement = new SimpleObjectProperty<>();
  /** Command executed when an element is selected. */
  @Setter
  protected Consumer<E> onSelectElement;
  /** Command executed when the elements property is updated. */
  @Setter
  protected Runnable onUpdateElementsProperty;
  /** Command executed when an element is removed. */
  @Setter
  protected Consumer<E> onRemoveElement;
  /** Command executed when the view should be focused. */
  @Setter
  protected Runnable onFocusView;

  /**
   * Creates a new {@code BaseListModel} with a plain observable list.
   */
  public BaseListModel() {
    elements = FXCollections.observableArrayList();
  }

  /**
   * Creates a new {@code BaseListModel} with an observable list using the given extractor.
   *
   * @param extractor callback that returns the observable values for each element to observe for changes
   */
  public BaseListModel(Callback<E, Observable[]> extractor) {
    elements = FXCollections.observableArrayList(extractor);
  }

  @Override
  public ObservableList<E> getElements() {
    return elements;
  }

  @Override
  public Property<E> selectedElementProperty() {
    return selectedElement;
  }

  @Override
  public void remove(E element) {
    selectedElement.setValue(null);
    if (elements.remove(element) && onRemoveElement != null) {
      onRemoveElement.accept(element);
    }
  }

  @Override
  public void update() {
    if (onUpdateElementsProperty != null) {
      onUpdateElementsProperty.run();
    }
  }

  @Override
  public void select(E item) {
    if (onSelectElement != null) {
      onSelectElement.accept(item);
    }
  }

  @Override
  public void focus() {
    if (onFocusView != null) {
      onFocusView.run();
    }
  }
}
