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

package cz.masci.springfx.mvci.view;

import javafx.css.Styleable;
import org.nield.dirtyfx.tracking.DirtyProperty;
import org.reactfx.value.Val;

/**
 * <pre>
 *   Interface for listening changes on dirty property.
 *   Add or remove dirty style class in this {@link Styleable} based on dirty property value.
 *   Adds when the property is dirty.
 *   Removes when the property is not dirty.
 * </pre>
 *
 * @param <T> Property implementing {@link DirtyProperty} interface
 */
public interface DirtyStyleable<T extends DirtyProperty> extends Styleable {

  default void initDirtyPropertyChangeListener(Val<T> itemProperty, String dirtyClassStyle) {
    System.out.format("Initializing dirty property change listener for property: %s\n", itemProperty);
    Val<Boolean> dirtyProperty = itemProperty.flatMap(DirtyProperty::isDirtyProperty);
    System.out.format("Got dirty property from item property: %s\n", dirtyProperty);
//    itemProperty.addListener((unused, oldValue, newValue) -> {
//      System.out.format("Item property has changed of observable: %s\n", unused);
//      updateDirtyClassStyle(newValue != null && newValue.isDirty(), dirtyClassStyle);
//    });
    dirtyProperty.addListener((unused, oldValue, newValue) -> {
      System.out.format("Dirty property has changed of observable: %s\n", unused);
      updateDirtyClassStyle(newValue, dirtyClassStyle);
    });

    updateDirtyClassStyle(dirtyProperty.getOrElse(false), dirtyClassStyle);
  }

  default void updateDirtyClassStyle(Boolean dirtyProperty, String dirtyClassStyle) {
    if (Boolean.TRUE.equals(dirtyProperty)) {
      getStyleClass().add(dirtyClassStyle);
    } else {
      getStyleClass().remove(dirtyClassStyle);
    }
  }
}
