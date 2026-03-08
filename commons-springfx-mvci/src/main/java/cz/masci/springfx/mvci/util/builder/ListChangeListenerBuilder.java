/*
 * Copyright (c) 2023-2024
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

package cz.masci.springfx.mvci.util.builder;

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
  /** Handler called for each element added to the list. */
  private Consumer<E> onAdd;
  /** Handler called for each element removed from the list. */
  private Consumer<E> onRemove;
  /** Handler called for each element updated in the list. */
  private Consumer<E> onUpdated;
  /** Handler called for each element permutated in the list. */
  private Consumer<E> onPermutated;

  /**
   * Creates a new {@code ListChangeListenerBuilder} with no handlers set.
   */
  public ListChangeListenerBuilder() {
  }

  /**
   * Sets the handler to invoke for each element added to the list.
   *
   * @param onAdd consumer receiving each added element
   * @return this builder
   */
  public ListChangeListenerBuilder<E> onAdd(Consumer<E> onAdd) {
    this.onAdd = onAdd;
    return this;
  }

  /**
   * Sets the handler to invoke for each element removed from the list.
   *
   * @param onRemove consumer receiving each removed element
   * @return this builder
   */
  public ListChangeListenerBuilder<E> onRemove(Consumer<E> onRemove) {
    this.onRemove = onRemove;
    return this;
  }

  /**
   * Sets the handler to invoke for each element updated in the list.
   *
   * @param onUpdated consumer receiving each updated element
   * @return this builder
   */
  public ListChangeListenerBuilder<E> onUpdated(Consumer<E> onUpdated) {
    this.onUpdated = onUpdated;
    return this;
  }

  /**
   * Sets the handler to invoke for each element permutated in the list.
   *
   * @param onPermutated consumer receiving each permutated element
   * @return this builder
   */
  public ListChangeListenerBuilder<E> onPermutated(Consumer<E> onPermutated) {
    this.onPermutated = onPermutated;
    return this;
  }

  /**
   * Builds and returns the configured {@link ListChangeListener}.
   *
   * @return a {@link ListChangeListener} that delegates to the registered handlers
   */
  public ListChangeListener<E> build() {
    return createListChangeListener(onAdd, onRemove, onUpdated, onPermutated);
  }

  /**
   * Creates a {@link ListChangeListener} dispatching to the given handler consumers.
   *
   * @param onAddItem       handler for added items, may be {@code null}
   * @param onRemoveItem    handler for removed items, may be {@code null}
   * @param onUpdatedItem   handler for updated items, may be {@code null}
   * @param onPermutatedItem handler for permutated items, may be {@code null}
   * @param <T>             the type of list elements
   * @return the constructed {@link ListChangeListener}
   */
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
