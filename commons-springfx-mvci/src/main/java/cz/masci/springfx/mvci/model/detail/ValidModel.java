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

import io.github.palexdev.materialfx.validation.Validated;
import javafx.beans.property.ReadOnlyBooleanProperty;

/**
 * The ValidModel interface extends the {@link Validated} interface and provides additional methods
 * related to the validation status of a model.
 * Simplify get valid property for {@link Validated} interface.
 */
public interface ValidModel extends Validated {
  /** Returns valid property of the object */
  default ReadOnlyBooleanProperty validProperty() {
    return getValidator().validProperty();
  }
}
