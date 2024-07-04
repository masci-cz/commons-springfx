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
import io.github.palexdev.materialfx.validation.Validated;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Function;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConstraintUtils {

  private static final String FIELD_IS_REQUIRED = "FIELD_IS_REQUIRED";
  private static final String FIELD_IN_RANGE = "FIELD_IN_RANGE";
  private static final String FIELD_IS_NUMBER = "FIELD_IS_NUMBER";

  private static Locale locale = Locale.getDefault();

  private static String getMessage(String type, Object ...args) {
    ResourceBundle bundle = ResourceBundle.getBundle("constraint_messages", locale);
    return String.format(bundle.getString(type), args);
  }

  public static void setLocale(Locale locale) {
    ConstraintUtils.locale = locale;
  }
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
                             .setMessage(getMessage(FIELD_IS_REQUIRED, fieldName))
                             .setCondition(ConditionUtils.isNotEmpty(stringProperty))
                             .get();
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
                             .setMessage(getMessage(FIELD_IN_RANGE, fieldName, min, max))
                             .setCondition(ConditionUtils.isInRange(integerProperty, min, max))
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
                  .setMessage(getMessage(FIELD_IS_NUMBER, fieldName))
                  .setCondition(ConditionUtils.isNumber(stringProperty))
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
                  .setMessage(getMessage(FIELD_IS_NUMBER, fieldName))
                  .setCondition(ConditionUtils.isNumberOrEmpty(stringProperty))
                  .get();
  }

  /**
   * Returns a constraint that validates whether the given string property is not empty when the nullable property is not empty. It is {@code TRUE} when nullable property is empty.
   *
   * @param stringProperty the string property to validate
   * @param nullableProperty the nullable property
   * @param fieldName the field name used in error message for the constraint
   * @param <T> Type of the nullable property
   * @return a constraint that validates whether the given string property is not empty
   */
  public static <T> Constraint isNotEmptyWhenPropertyIsNotEmpty(StringProperty stringProperty, Property<T> nullableProperty, String fieldName) {
    requireNonNull(nullableProperty);
    requireNonNull(stringProperty);

    return Builder.build()
                  .setSeverity(Severity.ERROR)
                  .setMessage(getMessage(FIELD_IS_REQUIRED, fieldName))
                  .setCondition(ConditionUtils.isNotBlankWhenPropertyIsNotEmpty(stringProperty, nullableProperty))
                  .get();
  }

  /**
   * Returns a constraint that validates whether the given string property contains only a number and the nullable property is not empty. It is {@code TRUE} when nullable property is empty.
   *
   * @param stringProperty the string property to validate
   * @param nullableProperty the nullable property
   * @param fieldName the field name used in the error message for the constraint
   * @param <T> Type of the nullable property
   * @return a constraint that validates whether the given string property contains only a number
   */
  public static <T> Constraint isNumberWhenPropertyIsNotEmpty(StringProperty stringProperty, Property<T> nullableProperty, String fieldName) {
    requireNonNull(nullableProperty);
    requireNonNull(stringProperty);

    return Builder.build()
                  .setSeverity(Severity.ERROR)
                  .setMessage(getMessage(FIELD_IS_NUMBER, fieldName))
                  .setCondition(ConditionUtils.isNumberWhenPropertyIsNotEmpty(stringProperty, nullableProperty))
                  .get();
  }

  /**
   * Returns a constraint that validates whether the given string property contains only a number and the nullable property is not empty. It is {@code TRUE} when nullable property is empty.
   *
   * @param stringProperty the string property to validate
   * @param nullableProperty the nullable property
   * @param fieldName the field name used in the error message for the constraint
   * @param <T> Type of the nullable property
   * @return a constraint that validates whether the given string property contains only a number
   */
  public static <T> Constraint isNumberOrEmptyWhenPropertyIsNotEmpty(StringProperty stringProperty, Property<T> nullableProperty, String fieldName) {
    requireNonNull(nullableProperty);
    requireNonNull(stringProperty);

    return Builder.build()
                  .setSeverity(Severity.ERROR)
                  .setMessage(getMessage(FIELD_IS_NUMBER, fieldName))
                  .setCondition(ConditionUtils.isNumberOrBlankWhenPropertyIsNotEmpty(stringProperty, nullableProperty))
                  .get();
  }

  /**
   * Returns a constraint that validates whether the child property is valid when the parent is set.
   *
   * @param fieldName error message for the constraint
   * @param parent the parent object from which the child property is taken
   * @param childMap the child object mapper
   * @param <T> Type of the parent observable value
   * @param <U> Type of the child observable value
   * @return a constraint that validates whether the child property satisfies to the test
   */
  public static <T, U extends Validated> Constraint isValid(String fieldName, ObservableValue<T> parent, Function<T, ObservableValue<U>> childMap) {
    return Builder.build()
                  .setSeverity(Severity.ERROR)
                  .setMessage(fieldName)
                  .setCondition(ConditionUtils.isValid(parent, childMap))
                  .get();
  }
}
