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
import cz.masci.springfx.mvci.model.list.Focusable;
import cz.masci.springfx.mvci.model.list.Removable;
import cz.masci.springfx.mvci.model.list.Selectable;
import io.github.palexdev.materialfx.validation.Validated;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javafx.collections.ObservableList;
import lombok.RequiredArgsConstructor;


/**
 * The OperableManagerController class is a generic class that provides operations for managing objects in a list.
 * It supports adding and removing elements, selecting and focusing elements, updating dirty elements, and discarding changes.
 *
 * @param <I> the type of the id of the elements
 * @param <E> the type of the elements that can be managed
 */
@RequiredArgsConstructor
public class OperableManagerController<I, E extends DetailModel<I>> {
  private final Selectable<E> selectable;
  private final Focusable focusable;
  private final Removable<E> removable;
  private final ObservableList<E> elements;

  public <T extends Selectable<E> & Focusable & Removable<E>> OperableManagerController(T model, ObservableList<E> elements) {
    this.elements = elements;
    this.selectable = model;
    this.focusable = model;
    this.removable = model;
  }

  /**
   * Adds an element to the list of elements managed by the OperableManagerController.
   * Does the following:
   * 1. Adds the element to the elements list.
   * 2. Selects the element using the Selectable interface.
   * 3. Focuses on the element using the Focusable interface.
   *
   * @param element the element to be added
   */
  public void add(E element) {
    elements.add(element);
    selectable.select(element);
    focusable.focus();
  }

  /**
   * Adds all elements from the given list to the elements managed by the OperableManagerController.
   * This method performs the following operations:
   *
   * 1. Unselects the currently selected element by calling the select() method of the Selectable interface with a null argument.
   * 2. Clears the elements list.
   * 3. Adds all elements from the given list to the elements list.
   *
   * @param newElements the list of new elements to be added
   */
  public void addAll(List<E> newElements) {
    selectable.select(null);
    elements.clear();
    elements.addAll(newElements);
  }

  /**
   * Updates the elements in the list of elements managed by the OperableManagerController.
   *
   * 1. Filters the dirty elements that are valid.
   * 2. For each valid dirty element, the updateAction is executed by accepting the element and the updatedElement consumer.
   * 3. If the element is transient, the id of the element is set to the id of the updatedElement.
   * 4. The element is rebaselined.
   *
   * @param updateAction the action to update the element
   *                    Accepts the element to be updated and the consumer to accept the updated element
   *                    The consumer is responsible for updating the element and can provide the updated element
   */
  public void update(BiConsumer<E, Consumer<E>> updateAction) {
    getDirtyElements().filter(Validated::isValid)
                      .forEach(element -> updateAction.accept(element, updatedElement -> {
                        if (element.isTransient()) {
                          element.setId(updatedElement.getId());
                        }
                        element.rebaseline();
                      }));
  }

  /**
   * Discards changes made to the dirty elements in the list of elements managed by the OperableManagerController.
   * If an element is transient, it is removed from the list.
   * If an element is not transient, it is reset to its original state.
   */
  public void discard() {
    var elementsToRemove = new ArrayList<E>();
    getDirtyElements().forEach(element -> {
      if (element.isTransient()) {
        elementsToRemove.add(element);
      } else {
        element.reset();
      }
    });
    elementsToRemove.forEach(removable::remove);
  }

  protected Stream<E> getDirtyElements() {
    return elements.stream().filter(DirtyModel::isDirty);
  }
}
