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

  ObjectProperty<T> idProperty();
  /** Returns the id */
  default T getId() {
    return idProperty().getValue();
  }
  /** Set the id */
  default void setId(T id) {
    idProperty().setValue(id);
  }
}
