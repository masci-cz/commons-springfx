/*
 * Copyright (c) 2024
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

package cz.masci.springfx.mvci.util.reactfx;

import java.util.function.Function;
import javafx.beans.property.Property;
import lombok.experimental.UtilityClass;
import org.reactfx.value.Val;
import org.reactfx.value.Var;

/**
 * Utility class providing helper methods for working with ReactFX {@link Val} and {@link Var} bindings.
 */
@UtilityClass
public class ReactFxUtils {

  /**
   * Creates a bidirectional {@link Var} that selects a nested property from the source,
   * or falls back to a constant value when the source is empty.
   *
   * @param src        the source property that may hold a value of type {@code T}
   * @param property   function extracting the nested {@link Property} from a {@code T} value
   * @param constValue the constant fallback value when the source is empty
   * @param <T>        the type of the source value
   * @param <U>        the type of the nested property value
   * @return a {@link Var} reflecting the nested property or the constant fallback
   */
  public static <T, U> Var<U> selectVarOrElseConst(Property<T> src, Function<T, Property<U>> property, U constValue) {
    Val<T> srcVal = Val.wrap(src);
    return srcVal.flatMap(property)
                 .orElseConst(constValue)
                 .asVar(newValue -> srcVal.ifPresent(srcProperty -> property.apply(srcProperty)
                                                                            .setValue(newValue)));
  }
}
