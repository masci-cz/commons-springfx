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

package cz.masci.springfx.mvci.model;

import cz.masci.springfx.mvci.model.dirty.DirtyListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import org.nield.dirtyfx.tracking.DirtyProperty;

/**
 * This class represents base list model with observable list to observe changes.
 */
public class ListModel<E extends DirtyProperty> {
  protected final DirtyListProperty<E> items = new DirtyListProperty<>();

  protected final ObjectProperty<E> selectedItem = new SimpleObjectProperty<>();

  public ObservableList<E> getItems() {
    return items.get();
  }

  public E getSelectedItem() {
    return selectedItem.getValue();
  }

  public ObjectProperty<E> selectedItemProperty() {
    return selectedItem;
  }
}
