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
 * <pre>
 *   Ability to remove element from list. Mostly used to remove element in the list view in list-detail view pattern.
 *   Setting the {@code setOnRemoveElement} is not necessary even is used when calling {@code removeElement}.
 * </pre>
 *
 * <pre>
 *   class RemovableImpl implements Removable<Model> {
 *     private Consumer<Model> onRemoveElement;
 *
 *     public void removeElement(Model element) {
 *       if (onRemoveElement != null) {
 *         onRemoveElement.accept(element);
 *       }
 *     }
 *
 *     public void setOnRemoveElement(Consumer<Model> command) {
 *       onRemoveElement = command;
 *     }
 *   }
 * </pre>
 */
public interface Removable<E> {
  void removeElement(E element);
  void setOnRemoveElement(Consumer<E> command);
}
