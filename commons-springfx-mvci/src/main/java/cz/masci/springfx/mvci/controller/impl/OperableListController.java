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
import cz.masci.springfx.mvci.model.list.Elements;
import cz.masci.springfx.mvci.model.list.Focusable;
import cz.masci.springfx.mvci.model.list.Removable;
import cz.masci.springfx.mvci.model.list.Selectable;
import cz.masci.springfx.mvci.model.list.Updatable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OperableListController<I, E extends DetailModel<I>, T extends Elements<E> & Selectable<E> & Updatable & Removable<E> & Focusable> {
  private final T viewModel;

  public void add(E element) {
    viewModel.getElements().add(element);
    viewModel.select(element);
    viewModel.focus();
  }

  public void addAll(List<E> elements) {
    viewModel.select(null);
    viewModel.getElements().clear();
    viewModel.getElements().addAll(elements);
  }

  public void update(BiConsumer<E, Consumer<E>> updateAction) {
    viewModel.getElements().forEach(element -> updateAction.accept(element, updatedElement -> {
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
    elementsToRemove.forEach(viewModel::remove);
  }

  protected Stream<E> getDirtyElements() {
    return viewModel.getElements().stream().filter(DirtyModel::isDirty);
  }
}
