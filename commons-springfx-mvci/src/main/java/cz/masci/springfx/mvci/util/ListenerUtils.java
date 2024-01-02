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
import lombok.experimental.UtilityClass;

@UtilityClass
public class ListenerUtils {

  /**
   * <pre>
   *   Creates list change listener with consumers on add, remove, updated and permutated item.
   *   For null consumers no action is executed. 
   * </pre>
   *
   * @param onAddItem What should be done with added item
   * @param onRemoveItem What should be done with removed item
   * @param onUpdatedItem What should be done with updated item
   * @param onPermutatedItem What should be done with permutated item
   * @return Created list change listener
   * @param <T> Type of listener
   */
  public static <T> ListChangeListener<T> createListChangeListener(Consumer<T> onAddItem, Consumer<T> onRemoveItem, Consumer<T> onUpdatedItem, Consumer<T> onPermutatedItem) {
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
