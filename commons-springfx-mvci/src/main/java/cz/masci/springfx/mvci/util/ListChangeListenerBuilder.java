/*
 * Copyright (c) 2023
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

package cz.masci.springfx.mvci.util;

import java.util.function.Consumer;
import javafx.collections.ListChangeListener;

/**
 * Builder for ListChangeListener. See {@link ListenerUtils}
 *
 * @param <E> Tne type of <code>List</code> elements
 */
public class ListChangeListenerBuilder<E> {
  private Consumer<E> onAdd;
  private Consumer<E> onRemove;
  private Consumer<E> onUpdated;
  private Consumer<E> onPermutated;

  public ListChangeListenerBuilder() {
  }

  public ListChangeListenerBuilder<E> onAdd(Consumer<E> onAdd) {
    this.onAdd = onAdd;
    return this;
  }

  public ListChangeListenerBuilder<E> onRemove(Consumer<E> onRemove) {
    this.onRemove = onRemove;
    return this;
  }

  public ListChangeListenerBuilder<E> onUpdated(Consumer<E> onUpdated) {
    this.onUpdated = onUpdated;
    return this;
  }

  public ListChangeListenerBuilder<E> onPermutated(Consumer<E> onPermutated) {
    this.onPermutated = onPermutated;
    return this;
  }

  public ListChangeListener<E> build() {
    return ListenerUtils.createListChangeListener(onAdd, onRemove, onUpdated, onPermutated);
  }
}
