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

import java.util.Optional;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.css.Styleable;
import org.nield.dirtyfx.tracking.DirtyProperty;

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
  String getDirtyStyleClass();

  ReadOnlyObjectProperty<T> itemProperty();

  default Optional<T> getItemOptional() {
    return Optional.ofNullable(itemProperty()).map(ReadOnlyObjectProperty::get);
  }

  default ChangeListener<? super Boolean> getDirtyPropertyChangeListener() {
    return  (dirtyObservable, dirtyOldValue, dirtyNewValue) -> updateDirtyClassStyle(dirtyNewValue);
  }

  default void initDirtyPropertyChangeListener() {
    updateDirtyClassStyle(getItemOptional().map(DirtyProperty::isDirty).orElse(false));

    // create dirty property change listener to be able to add/remove it in dirtyProperty
    ChangeListener<? super Boolean> dirtyPropertyChangeListener = getDirtyPropertyChangeListener();

    itemProperty().addListener((observable, oldValue, newValue) -> {
      if (oldValue != null) {
        updateDirtyClassStyle(null);
        oldValue.isDirtyProperty().removeListener(dirtyPropertyChangeListener);
      }
      if (newValue == null) {
        updateDirtyClassStyle(null);
      } else {
        updateDirtyClassStyle(newValue.isDirty());
        newValue.isDirtyProperty().addListener(dirtyPropertyChangeListener);
      }
    });
  }

  default void updateDirtyClassStyle(Boolean isDirty) {
    if (Boolean.TRUE.equals(isDirty)) {
      getStyleClass().add(getDirtyStyleClass());
    } else {
      getStyleClass().remove(getDirtyStyleClass());
    }
  }
}
