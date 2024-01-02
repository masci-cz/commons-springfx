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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.junit.jupiter.api.Test;

class BindingUtilsTest {

  @Test
  void bindNullableBidirectional_nullSource() {
    ObservableValue<TestProperty> source = new SimpleObjectProperty<>();
    IntegerProperty idProperty = new SimpleIntegerProperty(1);

    // check before binding
    assertEquals(1, idProperty.get());

    BindingUtils.bindNullableBidirectional(source, idProperty, TestProperty::idProperty, 0);

    // check after binding
    assertEquals(0, idProperty.get());
  }

  @Test
  void bindNullableBidirectional_singleBinding() {
    TestProperty sourceProperty = new TestProperty(2, 12);
    ObjectProperty<TestProperty> source = new SimpleObjectProperty<>(sourceProperty);
    IntegerProperty idProperty = new SimpleIntegerProperty(1);

    // check before binding
    assertEquals(1, idProperty.get());

    BindingUtils.bindNullableBidirectional(source, idProperty, TestProperty::idProperty, 0);

    // check after binding
    assertEquals(2, idProperty.get());

    // update source property
    sourceProperty.setId(3);
    assertEquals(3, idProperty.get());

    // update dest property
    idProperty.set(4);
    assertEquals(4, sourceProperty.getId());

    // update not bound source property
    sourceProperty.setValue(10);
    assertEquals(4, idProperty.get());

    // remove source property from the source
    source.set(null);
    assertEquals(0, idProperty.get());

    // change source property when not bound
    sourceProperty.setId(5);
    assertEquals(0, idProperty.get());

    // change dest property when not bound
    idProperty.set(6);
    assertEquals(5, sourceProperty.getId());

    // rebind
    source.set(sourceProperty);
    assertEquals(5, idProperty.get());
  }

  @Test
  void bindNullableBidirectional_multiBinding() {
    TestProperty sourceProperty = new TestProperty(2, 12);
    ObjectProperty<TestProperty> source = new SimpleObjectProperty<>(sourceProperty);
    IntegerProperty idProperty = new SimpleIntegerProperty(1);
    IntegerProperty valueProperty = new SimpleIntegerProperty(11);

    // check before binding
    assertEquals(1, idProperty.get());
    assertEquals(11, valueProperty.get());

    BindingUtils.bindNullableBidirectional(source,
        List.of(
            ImmutableTriple.of(idProperty, TestProperty::idProperty, 0),
            ImmutableTriple.of(valueProperty, TestProperty::valueProperty, 0)
        )
    );

    // check after binding
    assertEquals(2, idProperty.get());
    assertEquals(12, valueProperty.get());

    // update source property
    sourceProperty.setId(3);
    assertEquals(3, idProperty.get());
    assertEquals(12, valueProperty.get());

    // update dest property
    idProperty.set(4);
    assertEquals(4, sourceProperty.getId());
    assertEquals(12, sourceProperty.getValue());

    // update second bound source property
    sourceProperty.setValue(13);
    assertEquals(4, idProperty.get());
    assertEquals(13, valueProperty.get());

    // update second dest property
    valueProperty.set(14);
    assertEquals(4, sourceProperty.getId());
    assertEquals(14, sourceProperty.getValue());

    // remove source property from the source
    source.set(null);
    assertEquals(0, idProperty.get());
    assertEquals(0, valueProperty.get());

    // change source property when not bound
    sourceProperty.setId(5);
    assertEquals(0, idProperty.get());
    assertEquals(0, valueProperty.get());

    // change source property when not bound
    sourceProperty.setValue(15);
    assertEquals(0, idProperty.get());
    assertEquals(0, valueProperty.get());

    // change dest property when not bound
    idProperty.set(6);
    assertEquals(5, sourceProperty.getId());
    valueProperty.set(16);
    assertEquals(15, sourceProperty.getValue());

    // rebind
    source.set(sourceProperty);
    assertEquals(5, idProperty.get());
    assertEquals(15, valueProperty.get());

  }


  private static class TestProperty {
    IntegerProperty id = new SimpleIntegerProperty();
    IntegerProperty value = new SimpleIntegerProperty();

    public TestProperty(Integer id, Integer value) {
      setId(id);
      setValue(value);
    }

    public int getId() {
      return id.get();
    }

    public IntegerProperty idProperty() {
      return id;
    }

    public void setId(int id) {
      this.id.set(id);
    }

    public int getValue() {
      return value.get();
    }

    public IntegerProperty valueProperty() {
      return value;
    }

    public void setValue(int value) {
      this.value.set(value);
    }
  }
}