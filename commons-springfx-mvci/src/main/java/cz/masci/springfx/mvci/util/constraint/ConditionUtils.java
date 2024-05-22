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

package cz.masci.springfx.mvci.util.constraint;

import io.github.palexdev.materialfx.validation.Validated;
import java.util.function.Function;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.binding.NumberExpression;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.reactfx.value.Val;

@UtilityClass
public class ConditionUtils {

  private final static String NUMBER_REGEX = "[-+]?\\d+";

  public static BooleanExpression isEmpty(ObservableStringValue value) {
    return Bindings.isEmpty(value);
  }

  public static <T> BooleanExpression isEmpty(Val<T> value) {
    return Bindings.createBooleanBinding(value::isEmpty);
  }

  public static BooleanExpression isNotEmpty(ObservableStringValue value) {
    return Bindings.isNotEmpty(value);
  }

  public static <T> BooleanExpression isNotEmpty(Val<T> value) {
    return Bindings.createBooleanBinding(() -> !value.isEmpty());
  }

  public static BooleanExpression isBlank(ObservableStringValue value) {
    return Bindings.createBooleanBinding(() -> StringUtils.isBlank(value.getValue()), value);
  }

  public static BooleanExpression isNotBlank(ObservableStringValue value) {
    return Bindings.createBooleanBinding(() -> StringUtils.isNotBlank(value.getValue()), value);
  }

  public static BooleanExpression isInRange(NumberExpression value, int min, int max) {
    return Bindings.and(value.greaterThanOrEqualTo(min), value.lessThanOrEqualTo(max));
  }

  public static BooleanExpression isNumber(ObservableStringValue value) {
    return Bindings.createBooleanBinding(() -> value.getValue() != null && value.getValue().matches(NUMBER_REGEX), value);
  }

  public static BooleanExpression isNumberOrEmpty(ObservableStringValue value) {
    return Bindings.or(Bindings.isEmpty(value), isNumber(value));
  }

  public static BooleanExpression isNumberOrBlank(ObservableStringValue value) {
    return Bindings.or(isBlank(value), isNumber(value));
  }

  public static <T> BooleanExpression isNotBlankWhenPropertyIsNotEmpty(ObservableStringValue value, ObservableValue<T> property) {
    Val<T> val = Val.wrap(property);
    return Bindings.and(isNotEmpty(val), isNotBlank(value));
  }

  public static <T> BooleanExpression isNumberWhenPropertyIsNotEmpty(ObservableStringValue value, ObservableValue<T> property) {
    Val<T> val = Val.wrap(property);
    return Bindings.and(isNotEmpty(val), isNumber(value));
  }

  public static <T> BooleanExpression isNumberOrBlankWhenPropertyIsNotEmpty(ObservableStringValue value, ObservableValue<T> property) {
    Val<T> val = Val.wrap(property);
    return Bindings.and(isNotEmpty(val), isNumberOrBlank(value));
  }

  public static <T, U extends Validated> BooleanExpression isValid(ObservableValue<T> parent, Function<T, ObservableValue<U>> value) {
    Val<Boolean> valid = Val.flatMap(parent, value)
        .flatMap(child -> child.getValidator()
                               .validProperty());
    return Bindings.createBooleanBinding(() -> valid.getOrElse(false), valid);
  }
}
