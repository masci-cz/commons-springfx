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

package cz.masci.springfx.mvci.model.dirty;

import java.util.Objects;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import org.jetbrains.annotations.NotNull;
import org.nield.dirtyfx.tracking.DirtyProperty;

public class DirtyBooleanProperty extends BooleanProperty implements DirtyProperty {

  private final BooleanProperty originalValue;
  private final BooleanProperty isDirty;
  private final BooleanProperty delegate;

  public DirtyBooleanProperty(@NotNull Boolean initialValue) {
    originalValue = new SimpleBooleanProperty(initialValue);
    delegate = new SimpleBooleanProperty(initialValue);
    isDirty = new SimpleBooleanProperty(false);
    addListener(new WeakChangeListener<>((observable, oldValue, newValue) -> isDirty.set(!Objects.equals(getOriginalValue(), newValue))));
  }

  public Boolean getOriginalValue() {
    return originalValue.get();
  }

  public ObservableValue<Boolean> originalValueProperty() {
    return originalValue;
  }

  @Override
  public void bind(ObservableValue<? extends Boolean> observable) {
    delegate.bind(observable);
  }

  @Override
  public void unbind() {
    delegate.unbind();
  }

  @Override
  public boolean isBound() {
    return delegate.isBound();
  }

  @Override
  public Object getBean() {
    return delegate.getBean();
  }

  @Override
  public String getName() {
    return delegate.getName();
  }

  @Override
  public boolean get() {
    return delegate.get();
  }

  @Override
  public void set(boolean value) {
    delegate.set(value);
  }

  @Override
  public Boolean getValue() {
    return delegate.getValue();
  }

  @Override
  public void setValue(Boolean value) {
    delegate.setValue(value);
  }

  @Override
  public void addListener(ChangeListener<? super Boolean> listener) {
    delegate.addListener(listener);
  }

  @Override
  public void removeListener(ChangeListener<? super Boolean> listener) {
    delegate.removeListener(listener);
  }

  @Override
  public void addListener(InvalidationListener listener) {
    delegate.addListener(listener);
  }

  @Override
  public void removeListener(InvalidationListener listener) {
    delegate.removeListener(listener);
  }

  @Override
  public boolean isDirty() {
    return isDirty.get();
  }

  @NotNull
  @Override
  public ObservableValue<Boolean> isDirtyProperty() {
    return isDirty;
  }

  @Override
  public void rebaseline() {
    originalValue.set(get());
    isDirty.set(false);
  }

  @Override
  public void reset() {
    set(originalValue.get());
    isDirty.set(false);
  }

}
