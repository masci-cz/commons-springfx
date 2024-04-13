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

import java.util.function.Function;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FunctionUtils {

  /**
   * <p>
   *   Accepts not null source and applies the transform function on it.
   *   If the source is null returns null.
   * </p>
   *
   * @param source Source object
   * @param transform Transformation function
   * @return Transformed value or null
   * @param <T> Type of source object
   * @param <V> Type of transformed object
   */
  public static <T, V> V applyNotNull(T source, Function<T, V> transform) {
    return source != null ? transform.apply(source) : null;
  }

  /**
   * <p>
   *   Accepts not null source and applies the transform function on it.
   *   If the source is null returns value.
   * </p>
   *
   * @param source Source object
   * @param transform Transformation function
   * @param value Default value if source is null
   * @return Transformed value or default value
   * @param <T> Type of source object
   * @param <V> Type of transformed object
   */
  public static <T, V> V applyNotNullElse(T source, Function<T, V> transform, V value) {
    return source != null ? transform.apply(source) : value;
  }

  /**
   * <p>
   *   Accepts not null source and applies the transform function on it.
   *   If the source is null returns supplied value.
   * </p>
   *
   * @param source Source object
   * @param transform Transformation function
   * @param value Supplier for default value if source is null
   * @return Transformed value or default value
   * @param <T> Type of source object
   * @param <V> Type of transformed object
   */
  public static <T, V> V applyNotNullElseGet(T source, Function<T, V> transform, Supplier<V> value) {
    return source != null ? transform.apply(source) : value.get();
  }
}
