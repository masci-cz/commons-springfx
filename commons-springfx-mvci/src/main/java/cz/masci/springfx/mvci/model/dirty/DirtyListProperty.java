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

package cz.masci.springfx.mvci.model.dirty;

import cz.masci.springfx.mvci.util.ListChangeListenerBuilder;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import org.jetbrains.annotations.NotNull;
import org.nield.dirtyfx.tracking.DirtyProperty;

public class DirtyListProperty<E extends DirtyProperty> extends ListProperty<E> implements DirtyProperty {

  private final BooleanProperty isDirty = new SimpleBooleanProperty(false);
  private final ListProperty<E> delegate = new SimpleListProperty<>(FXCollections.observableArrayList());
  private final ChangeListener<Boolean> dirtyChangeListener = (observable, oldValue, newValue) -> {
    if (!newValue && isDirty.get()) {
      revalidateIsDirty();
    }
    if (newValue) {
      isDirty.set(true);
    }
  };

  public DirtyListProperty() {
    ListChangeListenerBuilder<E> listChangeListenerBuilder = new ListChangeListenerBuilder<>();
    listChangeListenerBuilder
        .onAdd(this::onAdd)
        .onUpdated(this::updateDirty)
        .onRemove(this::onRemove);
    addListener(new WeakListChangeListener<>(listChangeListenerBuilder.build()));
  }

  private void onAdd(E element) {
    updateDirty(element);
    element.isDirtyProperty().addListener(dirtyChangeListener);
  }

  private void onRemove(E element) {
    element.isDirtyProperty().removeListener(dirtyChangeListener);
    if (element.isDirty()) {
      revalidateIsDirty();
    }
  }

  private void updateDirty(E element) {
    if (!isDirty()) {
      isDirty.set(element.isDirty());
    }
  }

  private void revalidateIsDirty() {
    var stillDirty = get().stream().anyMatch(DirtyProperty::isDirty);
    if (!stillDirty) {
      isDirty.set(false);
    }
  }

  @Override
  public ReadOnlyIntegerProperty sizeProperty() {
    return delegate.sizeProperty();
  }

  @Override
  public ReadOnlyBooleanProperty emptyProperty() {
    return delegate.emptyProperty();
  }

  @Override
  public void bind(ObservableValue<? extends ObservableList<E>> observable) {
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
  public ObservableList<E> get() {
    return delegate.get();
  }

  @Override
  public void set(ObservableList<E> value) {
    delegate.set(value);
  }

  @Override
  public void addListener(ChangeListener<? super ObservableList<E>> listener) {
    delegate.addListener(listener);
  }

  @Override
  public void removeListener(ChangeListener<? super ObservableList<E>> listener) {
    delegate.removeListener(listener);
  }

  @Override
  public void addListener(ListChangeListener<? super E> listener) {
    delegate.addListener(listener);
  }

  @Override
  public void removeListener(ListChangeListener<? super E> listener) {
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
    get().stream().filter(DirtyProperty::isDirty).forEach(DirtyProperty::rebaseline);
    isDirty.set(false);
  }

  @Override
  public void reset() {
    get().stream().filter(DirtyProperty::isDirty).forEach(DirtyProperty::reset);
    isDirty.set(false);
  }
}
