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

import java.util.List;
import java.util.function.Function;
import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

@UtilityClass
public class BindingUtils {

  /**
   * Create bidirectional bindings between property of nullable object and another property. It solves bidirectional bindings problem when trying bind property in observable value
   * which could be null with another property.
   *
   * @param source Nullable observable value object. Means that the result of {@link ObservableValue#getValue()} could return null.
   * @param property Another property the source property will be bind to if the source observable value is not null. Otherwise, property value is set to the default value.
   * @param sourceProperty Source value property which will be bind to the destination property if the source value is not null.
   * @param defaultValue Default value when the source value is null.
   * @param <T> Property value type. Is same for source property and property.
   * @param <U> Property type of source property and property.
   * @param <V> Source value type.
   */
  public <T, U extends Property<T>, V> void bindNullableBidirectional(ObservableValue<V> source, U property, Function<V, U> sourceProperty, T defaultValue) {
    bindNullableBidirectional(source, List.of(ImmutableTriple.of(property, sourceProperty, defaultValue)));
  }

  /**
   * Create bidirectional bindings between properties of nullable object and another properties. It solves bidirectional bindings problem when trying bind property in observable value
   * which could be null with another property.
   * <p>
   *   The list could contain values with the same type only.
   * </p>
   *
   * @param source Nullable observable value object. Means that the result of {@link ObservableValue#getValue()} could return null.
   * @param propertyList List of properties which should be bound {@link BindingUtils#bindNullableBidirectional(ObservableValue, Property, Function, Object)}.<br>
   *        Each item contains: <code>property, sourceProperty, defaultValue</code>
   * @param <T> Property value type. Is same for source property and property.
   * @param <U> Property type of source property and property.
   * @param <V> Source value type.
   */
  public <T, U extends Property<T>, V> void bindNullableBidirectional(ObservableValue<V> source, List<Triple<U, Function<V, U>, T>> propertyList) {
    // check if the source is already set
    if (source.getValue() != null) {
      propertyList.forEach(triplet -> triplet.getLeft().bindBidirectional(triplet.getMiddle().apply(source.getValue())));
    } else {
      propertyList.forEach(triplet -> triplet.getLeft().setValue(triplet.getRight()));
    }

    // initialize listener
    source.addListener(((observable, oldValue, newValue) -> {
      // set old value listener
      if (oldValue != null) {
        propertyList.forEach(triplet -> triplet.getLeft().unbindBidirectional(triplet.getMiddle().apply(oldValue)));
      }
      // set new value listener or set default value
      if (newValue != null) {
        propertyList.forEach(triplet -> triplet.getLeft().bindBidirectional(triplet.getMiddle().apply(newValue)));
      } else {
        propertyList.forEach(triplet -> triplet.getLeft().setValue(triplet.getRight()));
      }
    }));
  }

}
