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

package cz.masci.commons.springfx.data;

import javafx.beans.property.ObjectProperty;

/**
 * The Identifiable interface defines a contract for objects that have an identifier.
 *
 * @param <T> The type of the identifier
 */
public interface Identifiable<T> {

  /**
   * Returns the property holding the identifier.
   *
   * @return the {@link ObjectProperty} representing the identifier
   */
  ObjectProperty<T> idProperty();

  /**
   * Returns the identifier value.
   *
   * @return the current identifier, or {@code null} if not set
   */
  default T getId() {
    return idProperty().getValue();
  }

  /**
   * Sets the identifier value.
   *
   * @param id the new identifier to set
   */
  default void setId(T id) {
    idProperty().setValue(id);
  }
}
