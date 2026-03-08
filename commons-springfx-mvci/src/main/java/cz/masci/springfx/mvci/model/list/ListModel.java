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

import java.util.function.Consumer;

/**
 * This list interface groups {@link Selectable}, {@link Updatable}, {@link  Removable} and {@link Focusable} interfaces. Adding methods to externally define
 * commands for {@link Focusable#focus}, {@link Removable#remove}, {@link Selectable#select} and {@link Updatable#update}
 *
 * @param <E> The type of the elements in the list
 */
public interface ListModel<E> extends Selectable<E>, Updatable, Removable<E>, Focusable {
  /**
   * Sets the command which is executed in {@link Focusable#focus}.
   *
   * @param command the runnable to execute when focusing the view
   */
  void setOnFocusView(Runnable command);

  /**
   * Sets the command which is executed in {@link Removable#remove}
   *
   * @param command the consumer to execute when removing an element
   */
  void setOnRemoveElement(Consumer<E> command);

  /**
   * Sets the command which is executed in {@link Selectable#select}
   *
   * @param command the consumer to execute when selecting an element
   */
  void setOnSelectElement(Consumer<E> command);

  /**
   * Sets the command which is executed in {@link Updatable#update}
   *
   * @param command the runnable to execute when updating the elements property
   */
  void setOnUpdateElementsProperty(Runnable command);
}
