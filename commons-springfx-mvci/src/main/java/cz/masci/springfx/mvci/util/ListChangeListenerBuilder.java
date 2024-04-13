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
 * Builder class for creating a ListChangeListener with different event handlers.
 * Usage example:
 * <pre>{@code
 * ObservableList<String> list = FXCollections.observableArrayList();
 * ListChangeListener<String> listener = new ListChangeListenerBuilder<String>()
 *        .onAdd(item -> System.out.println("Item added: " + item))
 *        .onRemove(item -> System.out.println("Item removed: " + item))
 *        .build();
 * list.addListener(listener);
 * }</pre>
 *
 * @param <E> The type of elements in the list
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
    return createListChangeListener(onAdd, onRemove, onUpdated, onPermutated);
  }

  private static <T> ListChangeListener<T> createListChangeListener(Consumer<T> onAddItem, Consumer<T> onRemoveItem, Consumer<T> onUpdatedItem, Consumer<T> onPermutatedItem) {
    return c -> {
      while (c.next()) {
        if (c.wasPermutated()) {
          if (onPermutatedItem != null) {
            for (int i = c.getFrom(); i < c.getTo(); ++i) {
              onPermutatedItem.accept(c.getList().get(i));
            }
          }
        } else if (c.wasUpdated()) {
          if (onUpdatedItem != null) {
            for (int i = c.getFrom(); i < c.getTo(); ++i) {
              onUpdatedItem.accept(c.getList().get(i));
            }
          }
        } else {
          if (onRemoveItem != null) {
            for (T remItem : c.getRemoved()) {
              onRemoveItem.accept(remItem);
            }
          }
          if (onAddItem != null) {
            for (T addItem : c.getAddedSubList()) {
              onAddItem.accept(addItem);
            }
          }
        }
      }
    };
  }

}
