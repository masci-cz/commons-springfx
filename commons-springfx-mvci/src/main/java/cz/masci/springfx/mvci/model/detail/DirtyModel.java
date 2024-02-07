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

package cz.masci.springfx.mvci.model.detail;

import static java.util.Objects.requireNonNull;

import javafx.beans.value.ObservableValue;
import org.jetbrains.annotations.NotNull;
import org.nield.dirtyfx.tracking.CompositeDirtyProperty;
import org.nield.dirtyfx.tracking.DirtyProperty;

public interface DirtyModel extends DirtyProperty {

  CompositeDirtyProperty getComposite();

  @Override
  default boolean isDirty() {
    requireNonNull(getComposite());

    return getComposite().isDirty();
  }

  @NotNull
  @Override
  default ObservableValue<Boolean> isDirtyProperty() {
    requireNonNull(getComposite());

    return getComposite().isDirtyProperty();
  }

  @Override
  default void rebaseline() {
    requireNonNull(getComposite());

    getComposite().rebaseline();
  }

  @Override
  default void reset() {
    requireNonNull(getComposite());

    getComposite().reset();
  }
}
