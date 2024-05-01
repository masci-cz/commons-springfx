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

  public void add(E element) {
    elements.add(element);
    selectable.select(element);
    focusable.focus();
  }

  public void addAll(List<E> newElements) {
    selectable.select(null);
    elements.clear();
    elements.addAll(newElements);
  }

  public void update(BiConsumer<E, Consumer<E>> updateAction) {
   getDirtyElements()
       .filter(Validated::isValid)
       .forEach(element -> updateAction.accept(element, updatedElement -> {
      if (element.isTransient()) {
        element.setId(updatedElement.getId());
      }
      element.rebaseline();
    }));
  }

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
