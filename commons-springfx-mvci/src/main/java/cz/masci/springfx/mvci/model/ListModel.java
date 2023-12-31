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

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class represents base list model with observable list to observe changes.
 */
public class ListModel<T> {
  protected final ListProperty<T> items = new SimpleListProperty<>(FXCollections.observableArrayList());

  protected final ObjectProperty<T> selectedItem = new SimpleObjectProperty<>();

  public ObservableList<T> getItems() {
    return items.get();
  }

  public T getSelectedItem() {
    return selectedItem.getValue();
  }

  public ObjectProperty<T> selectedItemProperty() {
    return selectedItem;
  }
}
