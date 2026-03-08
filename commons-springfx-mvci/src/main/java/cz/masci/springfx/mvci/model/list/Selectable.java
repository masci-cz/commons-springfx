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

import javafx.beans.property.Property;

/**
 * Ability to select element in elements observable list. Mostly used to explicitly {@code select} an element in the list view from list-detail view pattern.
 * Also provides selected element property
 *
 * @param <E> The type of the element to select
 */
public interface Selectable<E> {

  /**
   * Selects the specified element.
   *
   * @param element the element to select
   */
  void select(E element);

  /**
   * Returns selected element property
   *
   * @return the property holding the currently selected element
   */
  Property<E> selectedElementProperty();

  /**
   * Returns the currently selected element.
   *
   * @return the currently selected element, or {@code null} if none is selected
   */
  default E getSelectedElement() {
    return selectedElementProperty().getValue();
  }
}
