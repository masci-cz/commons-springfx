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

import static java.util.Objects.requireNonNull;

import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Constraint.Builder;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.reactfx.value.Var;

@UtilityClass
public class ConstraintUtils {

  public final static int ABILITY_SCORE_MIN = 0;
  public final static int ABILITY_SCORE_MAX = 21;
  private final static String NUMBER_REGEX = "[-+]?\\d+";

  /**
   * Returns a constraint that validates whether the given string property is not empty.
   *
   * @param stringProperty the string property to validate
   * @param fieldName the field name used in error message for the constraint
   * @return a constraint that validates whether the given string property is not empty
   */
  public static Constraint isNotEmpty(StringProperty stringProperty, String fieldName) {
    requireNonNull(stringProperty);

    return Constraint.Builder.build()
        .setSeverity(Severity.ERROR)
        .setMessage(String.format("Pole %s je povinné", fieldName))
        .setCondition(stringProperty.isNotEmpty())
        .get();
  }

  /**
   * Returns a constraint that validates whether the given IntegerProperty is within the range of 1 to 10 (inclusive).
   *
   * @param property the IntegerProperty to validate
   * @param fieldName the field name used in error message for the constraint
   * @return a constraint that validates whether the given IntegerProperty is within the range of 1 to 10 (inclusive)
   */
  public static Constraint isInAbilityScoresRange(IntegerProperty property, String fieldName) {
    return isInRange(property, fieldName, ABILITY_SCORE_MIN, ABILITY_SCORE_MAX);
  }

  /**
   * Returns a constraint that validates whether the given integer property is within the specified range.
   *
   * @param integerProperty the integer property to validate
   * @param fieldName the field name used in the error message for the constraint
   * @param min the minimum value of the range (inclusive)
   * @param max the maximum value of the range (inclusive)
   * @return a constraint that validates whether the given integer property is within the specified range
   */
  public static Constraint isInRange(IntegerProperty integerProperty, String fieldName, int min, int max) {
    requireNonNull(integerProperty);

    return Constraint.Builder.build()
        .setSeverity(Severity.ERROR)
        .setMessage(String.format("Pole %s musí být v rozmezí %d až %d", fieldName, min, max))
        .setCondition(integerProperty.greaterThanOrEqualTo(min).and(integerProperty.lessThanOrEqualTo(max)))
        .get();
  }

  /**
   * Returns a constraint that validates whether the given string property contains only a number.
   *
   * @param stringProperty the string property to validate
   * @param fieldName the field name used in the error message for the constraint
   * @return a constraint that validates whether the given string property contains only a number
   */
  public static Constraint isNumber(StringProperty stringProperty, String fieldName) {
    requireNonNull(stringProperty);

    return Builder.build()
        .setSeverity(Severity.ERROR)
        .setMessage(String.format("Pole %s musí být číslo", fieldName))
        .setCondition(Bindings.createBooleanBinding(() -> stringProperty.get() != null && stringProperty.get().matches(NUMBER_REGEX), stringProperty))
        .get();
  }

  /**
   * Returns a constraint that validates whether the given string property contains a number or is empty.
   *
   * @param stringProperty the string property to validate
   * @param fieldName the field name used in the error message for the constraint
   * @return a constraint that validates whether the given string property contains only a number
   */
  public static Constraint isNumberOrEmpty(StringProperty stringProperty, String fieldName) {
    requireNonNull(stringProperty);

    return Builder.build()
        .setSeverity(Severity.ERROR)
        .setMessage(String.format("Pole %s musí být číslo", fieldName))
        .setCondition(Bindings.createBooleanBinding(() -> StringUtils.isBlank(stringProperty.get()) || stringProperty.get().matches(NUMBER_REGEX), stringProperty))
        .get();
  }

  /**
   * Returns a constraint that validates whether the given string property is not empty or the nullable property is empty.
   *
   * @param stringProperty the string property to validate
   * @param nullableProperty the nullable property
   * @param fieldName the field name used in error message for the constraint
   * @return a constraint that validates whether the given string property is not empty
   */
  public static <T> Constraint isNotEmptyWhenPropertyIsNotEmpty(StringProperty stringProperty, Var<T> nullableProperty, String fieldName) {
    requireNonNull(nullableProperty);
    requireNonNull(stringProperty);

    return Builder.build()
        .setSeverity(Severity.ERROR)
        .setMessage(String.format("Pole %s je povinné", fieldName))
        .setCondition(Bindings.createBooleanBinding(() ->
            nullableProperty.isEmpty() || StringUtils.isNotBlank(stringProperty.getValue()), nullableProperty, stringProperty)
        )
        .get();
  }

  /**
   * Returns a constraint that validates whether the given string property contains only a number or the nullable property is empty.
   *
   * @param stringProperty the string property to validate
   * @param nullableProperty the nullable property
   * @param fieldName the field name used in the error message for the constraint
   * @return a constraint that validates whether the given string property contains only a number
   */
  public static <T> Constraint isNumberWhenPropertyIsNotEmpty(StringProperty stringProperty, Var<T> nullableProperty, String fieldName) {
    requireNonNull(nullableProperty);
    requireNonNull(stringProperty);

    return Builder.build()
        .setSeverity(Severity.ERROR)
        .setMessage(String.format("Pole %s musí být číslo", fieldName))
        .setCondition(Bindings.createBooleanBinding(() ->
            nullableProperty.isEmpty() || (stringProperty.get() != null && stringProperty.get().matches(NUMBER_REGEX)), nullableProperty, stringProperty)
        )
        .get();
  }

  /**
   * Returns a constraint that validates whether the given string property contains only a number or the nullable property is empty.
   *
   * @param stringProperty the string property to validate
   * @param nullableProperty the nullable property
   * @param fieldName the field name used in the error message for the constraint
   * @return a constraint that validates whether the given string property contains only a number
   */
  public static <T> Constraint isNumberOrEmptyWhenPropertyIsNotEmpty(StringProperty stringProperty, Var<T> nullableProperty, String fieldName) {
    requireNonNull(nullableProperty);
    requireNonNull(stringProperty);

    return Builder.build()
        .setSeverity(Severity.ERROR)
        .setMessage(String.format("Pole %s musí být číslo", fieldName))
        .setCondition(Bindings.createBooleanBinding(() ->
            nullableProperty.isEmpty() || StringUtils.isBlank(stringProperty.get()) || stringProperty.get().matches(NUMBER_REGEX), nullableProperty, stringProperty)
        )
        .get();
  }

}
