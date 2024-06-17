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

package cz.masci.springfx.mvci;

import static cz.masci.springfx.mvci.TestConstants.INITIAL_TEXT;
import static org.junit.jupiter.api.Assertions.assertEquals;

import cz.masci.springfx.mvci.model.detail.impl.BaseDetailModel;
import cz.masci.springfx.mvci.model.dirty.DirtyStringProperty;
import io.github.palexdev.materialfx.validation.Constraint;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtils {

  public static <T, U, V> void assertExpressions(Stream<TripleArgument<T, U, V>> arguments,
                                                 ObjectProperty<T> parentProperty,
                                                 Function<T, ObjectProperty<U>> detailFromParent,
                                                 Function<U, Consumer<V>> detailValueSetter,
                                                 BooleanExpression expression
  ) {
    arguments.forEach(argument -> {
      ObjectProperty<U> detailPropertyOfParentObject = argument.val1() != null ? detailFromParent.apply(argument.val1()) : null;

      parentProperty.setValue(argument.val1());
      if (detailPropertyOfParentObject != null) {
        detailPropertyOfParentObject.setValue(argument.val2());
      }
      if (argument.val2() != null) {
        detailValueSetter.apply(argument.val2())
                         .accept(argument.val3());
      }

      assertEquals(argument.result(), expression.getValue());
    });
  }

  public static <T, U extends Property<T>> void assertExpressions(Stream<Argument<T>> arguments, U property, BooleanExpression expression) {
    arguments.forEach(argument -> {
      property.setValue(argument.value());
      assertEquals(argument.result(), expression.getValue(), String.format("Check of %s failed", argument.value()));
    });
  }

  public static <T, U, V extends Property<T>, W extends Property<U>> void assertExpressions(Stream<TupleArgument<T, U>> arguments, V property,
                                                                                            W nullableProperty,
                                                                                            BooleanExpression expression) {
    arguments.forEach(argument -> {
      property.setValue(argument.val1());
      nullableProperty.setValue(argument.val2());
      assertEquals(argument.result(), expression.getValue(), String.format("Check of v1=%s failed when v2=%s", argument.val1(), argument.val2()));
    });
  }

  public record Argument<T>(T value, boolean result) {
    public static <T> Argument<T> of(T value, boolean result) {
      return new Argument<>(value, result);
    }
  }

  public record TupleArgument<T, U>(T val1, U val2, boolean result) {
    public static <T, U> TupleArgument<T, U> of(T val1, U val2, boolean result) {
      return new TupleArgument<>(val1, val2, result);
    }
  }

  public record TripleArgument<T, U, V>(T val1, U val2, V val3, boolean result) {
    public static <T, U, V> TripleArgument<T, U, V> of(T val1, U val2, V val3, boolean result) {
      return new TripleArgument<>(val1, val2, val3, result);
    }
  }


  @Value
  public static class TestParentModel {
    ObjectProperty<TestDetailModel> detail = new SimpleObjectProperty<>();
  }

  public static class TestDetailModel extends BaseDetailModel<Integer> {
    private final DirtyStringProperty text = new DirtyStringProperty(INITIAL_TEXT);
    @Setter
    @Getter
    private boolean isTransient;

    public TestDetailModel() {
      addComposites(text);
      addConstraints(Constraint.of("Text should be not empty", text.isNotEmpty()));
    }

    public String getText() {
      return text.get();
    }

    public DirtyStringProperty textProperty() {
      return text;
    }

    public void setText(String text) {
      this.text.set(text);
    }
  }
}
