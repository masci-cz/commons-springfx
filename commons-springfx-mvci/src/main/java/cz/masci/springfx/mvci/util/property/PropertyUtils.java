/*
 * Copyright (c) 2023-2024
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

package cz.masci.springfx.mvci.util.property;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PropertyUtils {
  public static final BooleanProperty FALSE_PROPERTY = new SimpleBooleanProperty(false);

  public static final BooleanProperty TRUE_PROPERTY = new SimpleBooleanProperty(true);

  /**
   * This method returns new boolean property which listen to source property change and set own value to the negation of the source property change.
   *
   * @param source Source boolean property this property is listening to
   * @return Created boolean property
   */
  public BooleanProperty not(ReadOnlyBooleanProperty source) {
    var result = new SimpleBooleanProperty();
    source.addListener((observable, oldValue, newValue) -> result.set(!newValue));
    return result;
  }
}
