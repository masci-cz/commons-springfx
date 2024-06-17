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

import static cz.masci.springfx.mvci.TestUtils.assertExpressions;

import cz.masci.springfx.mvci.TestUtils.Argument;
import cz.masci.springfx.mvci.TestUtils.TestDetailModel;
import cz.masci.springfx.mvci.TestUtils.TestParentModel;
import cz.masci.springfx.mvci.TestUtils.TripleArgument;
import cz.masci.springfx.mvci.TestUtils.TupleArgument;
import java.util.stream.Stream;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.jupiter.api.Test;
import org.reactfx.value.Val;

class ConditionUtilsTest {

  @Test
  void isEmpty() {
    StringProperty property = new SimpleStringProperty();

    var expression = ConditionUtils.isEmpty(property);

    assertExpressions(
        Stream.of(
            Argument.of(null, true),
            Argument.of("test", false),
            Argument.of("", true),
            Argument.of("  ", false)
        ), property, expression);
  }

  @Test
  void isEmpty_string() {
    StringProperty property = new SimpleStringProperty();

    var expression = ConditionUtils.isEmpty(property);

    assertExpressions(
        Stream.of(
            Argument.of(null, true),
            Argument.of("test", false),
            Argument.of("", true),
            Argument.of("  ", false)
        ), property, expression);
  }

  @Test
  void isEmpty_val() {
    StringProperty property = new SimpleStringProperty();
    Val<String> val = Val.wrap(property);

    var expression = ConditionUtils.isEmpty(val);

    assertExpressions(
        Stream.of(
            Argument.of(null, true),
            Argument.of("test", false),
            Argument.of("", false),
            Argument.of("  ", false)
        ), property, expression);
  }

  @Test
  void isNotEmpty_string() {
    StringProperty property = new SimpleStringProperty();

    var expression = ConditionUtils.isNotEmpty(property);

    assertExpressions(
        Stream.of(
            Argument.of(null, false),
            Argument.of("test", true),
            Argument.of("", false),
            Argument.of("  ", true)
        ), property, expression);
  }

  @Test
  void isNotEmpty_val() {
    StringProperty property = new SimpleStringProperty();
    Val<String> val = Val.wrap(property);

    var expression = ConditionUtils.isNotEmpty(val);

    assertExpressions(
        Stream.of(
            Argument.of(null, false),
            Argument.of("test", true),
            Argument.of("", true),
            Argument.of("  ", true)
        ), property, expression);
  }

  @Test
  void isBlank() {
    StringProperty property = new SimpleStringProperty();

    var expression = ConditionUtils.isBlank(property);

    assertExpressions(
        Stream.of(
            Argument.of(null, true),
            Argument.of("test", false),
            Argument.of("", true),
            Argument.of("  ", true)
        ), property, expression);
  }

  @Test
  void isNotBlank() {
    StringProperty property = new SimpleStringProperty();

    var expression = ConditionUtils.isNotBlank(property);

    assertExpressions(
        Stream.of(
            Argument.of(null, false),
            Argument.of("test", true),
            Argument.of("", false),
            Argument.of("  ", false)
        ), property, expression);
  }

  @Test
  void isInRange() {
    IntegerProperty property = new SimpleIntegerProperty();

    var expression = ConditionUtils.isInRange(property, 0, 10);

    assertExpressions(
        Stream.of(
            Argument.of(-5, false),
            Argument.of(5, true),
            Argument.of(15, false)
        ), property, expression
    );
  }

  @Test
  void isNumber() {
    StringProperty property = new SimpleStringProperty();

    var expression = ConditionUtils.isNumber(property);

    assertExpressions(
        Stream.of(
            Argument.of(null, false),
            Argument.of("", false),
            Argument.of("  ", false),
            Argument.of("5", true),
            Argument.of("5.9", false),
            Argument.of("-5", true),
            Argument.of("5+5", false),
            Argument.of("+5", true)
        ), property, expression
    );
  }

  @Test
  void isNumberOrEmpty() {
    StringProperty property = new SimpleStringProperty();

    var expression = ConditionUtils.isNumberOrEmpty(property);

    assertExpressions(
        Stream.of(
            Argument.of(null, true),
            Argument.of("  ", false),
            Argument.of("", true),
            Argument.of("5.9", false),
            Argument.of("5", true),
            Argument.of("5+5", false),
            Argument.of("-5", true),
            Argument.of("+5", true)
        ), property, expression
    );
  }

  @Test
  void isNumberOrBlank() {
    StringProperty property = new SimpleStringProperty();

    var expression = ConditionUtils.isNumberOrBlank(property);

    assertExpressions(
        Stream.of(
            Argument.of(null, true),
            Argument.of("5.9", false),
            Argument.of("", true),
            Argument.of("5", true),
            Argument.of("-5", true),
            Argument.of("5+5", false),
            Argument.of("  ", true),
            Argument.of("+5", true)
        ), property, expression
    );
  }

  @Test
  void isNotBlankWhenPropertyIsNotEmpty() {
    StringProperty property = new SimpleStringProperty();
    StringProperty nullableProperty = new SimpleStringProperty();

    var expression = ConditionUtils.isNotBlankWhenPropertyIsNotEmpty(property, nullableProperty);

    assertExpressions(
        Stream.of(
            TupleArgument.of(null, null, false),
            TupleArgument.of("", null, false),
            TupleArgument.of("  ", null, false),
            TupleArgument.of("test", null, false),
            TupleArgument.of(null, "test", false),
            TupleArgument.of("", "test", false),
            TupleArgument.of("  ", "test", false),
            TupleArgument.of("test", "test", true),
            TupleArgument.of(null, "", false),
            TupleArgument.of("", "", false),
            TupleArgument.of("  ", "", false),
            TupleArgument.of("test", "", true)

        ), property, nullableProperty, expression
    );
  }

  @Test
  void isNumberWhenPropertyIsNotEmpty() {
    StringProperty property = new SimpleStringProperty();
    StringProperty nullableProperty = new SimpleStringProperty();

    var expression = ConditionUtils.isNumberWhenPropertyIsNotEmpty(property, nullableProperty);

    assertExpressions(
        Stream.of(
            TupleArgument.of(null, null, false),
            TupleArgument.of("", null, false),
            TupleArgument.of("  ", null, false),
            TupleArgument.of("5", null, false),
            TupleArgument.of("5.9", null, false),
            TupleArgument.of("-5", null, false),
            TupleArgument.of("5+5", null, false),
            TupleArgument.of("+5", null, false),
            TupleArgument.of(null, "test", false),
            TupleArgument.of("", "test", false),
            TupleArgument.of("  ", "test", false),
            TupleArgument.of("5", "test", true),
            TupleArgument.of("5.9", "test", false),
            TupleArgument.of("-5", "test", true),
            TupleArgument.of("5+5", "test", false),
            TupleArgument.of("+5", "test", true),
            TupleArgument.of(null, "", false),
            TupleArgument.of("", "", false),
            TupleArgument.of("  ", "", false),
            TupleArgument.of("5", "", true),
            TupleArgument.of("5.9", "", false),
            TupleArgument.of("-5", "", true),
            TupleArgument.of("5+5", "", false),
            TupleArgument.of("+5", "", true)
        ), property, nullableProperty, expression
    );
  }

  @Test
  void isNumberOrBlankWhenPropertyIsNotEmpty() {
    StringProperty property = new SimpleStringProperty();
    StringProperty nullableProperty = new SimpleStringProperty();

    var expression = ConditionUtils.isNumberOrBlankWhenPropertyIsNotEmpty(property, nullableProperty);

    assertExpressions(
        Stream.of(
            TupleArgument.of(null, null, false),
            TupleArgument.of("", null, false),
            TupleArgument.of("  ", null, false),
            TupleArgument.of("5", null, false),
            TupleArgument.of("5.9", null, false),
            TupleArgument.of("-5", null, false),
            TupleArgument.of("5+5", null, false),
            TupleArgument.of("+5", null, false),
            TupleArgument.of(null, "test", true),
            TupleArgument.of("", "test", true),
            TupleArgument.of("  ", "test", true),
            TupleArgument.of("5", "test", true),
            TupleArgument.of("5.9", "test", false),
            TupleArgument.of("-5", "test", true),
            TupleArgument.of("5+5", "test", false),
            TupleArgument.of("+5", "test", true),
            TupleArgument.of(null, "", true),
            TupleArgument.of("", "", true),
            TupleArgument.of("  ", "", true),
            TupleArgument.of("5", "", true),
            TupleArgument.of("5.9", "", false),
            TupleArgument.of("-5", "", true),
            TupleArgument.of("5+5", "", false),
            TupleArgument.of("+5", "", true)
        ), property, nullableProperty, expression
    );
  }

  @Test
  void isValid() {
    ObjectProperty<TestParentModel> parentProperty = new SimpleObjectProperty<>();
    var parentObject = new TestParentModel();
    var detailObject = new TestDetailModel();

    var expression = ConditionUtils.isValid(parentProperty, TestParentModel::getDetail);

    assertExpressions(
        Stream.of(
            TripleArgument.of(null, null, null, false),
            TripleArgument.of(parentObject, null, null, false),
            TripleArgument.of(parentObject, null, "Name", false),
            TripleArgument.of(parentObject, detailObject, "Name", true),
            TripleArgument.of(parentObject, detailObject, null, false),
            TripleArgument.of(null, detailObject, "Name", false)
        ), parentProperty, TestParentModel::getDetail, detail -> detail::setText, expression
    );
  }

}