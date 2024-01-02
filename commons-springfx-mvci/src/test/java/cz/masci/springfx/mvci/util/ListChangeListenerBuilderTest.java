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

import static org.junit.jupiter.api.Assertions.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

class ListChangeListenerBuilderTest {
  private static final String VALUE = "VALUE";

  @Test
  void createListChangeListener() {
    ObservableList<String> list = FXCollections.observableArrayList();
    StringProperty onAddItem = new SimpleStringProperty();
    StringProperty onRemoveItem = new SimpleStringProperty();
    StringProperty onUpdatedItem = new SimpleStringProperty();
    StringProperty onPermutatedItem = new SimpleStringProperty();

    ListChangeListener<String> listChangeListener = new ListChangeListenerBuilder<String>()
        .onAdd(onAddItem::set)
        .onRemove(onRemoveItem::set)
        .onUpdated(onUpdatedItem::set)
        .onPermutated(onPermutatedItem::set)
        .build();

    list.addListener(listChangeListener);

    // check add item
    list.add(VALUE);
    assertAll("Check add item",
        () -> assertEquals(VALUE, onAddItem.get()),
        () -> assertNull(onRemoveItem.get()),
        () -> assertEquals(1, list.size())
    );

    // clear
    onAddItem.set(null);

    // check remove item
    list.remove(VALUE);
    assertAll("Check remove item",
        () -> assertNull(onAddItem.get()),
        () -> assertEquals(VALUE, onRemoveItem.get()),
        () -> assertEquals(0, list.size())
    );

  }

}