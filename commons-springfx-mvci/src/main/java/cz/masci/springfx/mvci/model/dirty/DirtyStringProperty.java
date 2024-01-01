package cz.masci.springfx.mvci.model.dirty;

import java.util.Objects;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import org.jetbrains.annotations.NotNull;
import org.nield.dirtyfx.tracking.DirtyProperty;

// TODO: Create DirtyObservableList where dirty means that one of items is dirty
public class DirtyStringProperty extends StringProperty implements DirtyProperty {

  private final StringProperty originalValue;
  private final BooleanProperty isDirty;
  private final StringProperty delegate;

  public DirtyStringProperty(@NotNull String initialValue) {
    originalValue = new SimpleStringProperty(initialValue);
    delegate = new SimpleStringProperty(initialValue);
    isDirty = new SimpleBooleanProperty(false);
    addListener(new WeakChangeListener<>((observable, oldValue, newValue) -> isDirty.set(!Objects.equals(getOriginalValue(), newValue))));
  }

  public String getOriginalValue() {
    return originalValue.get();
  }

  public ObservableValue<String> originalValueProperty() {
    return originalValue;
  }

  @Override
  public void bind(ObservableValue<? extends String> observable) {
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
  public String get() {
    return delegate.get();
  }

  @Override
  public void set(String value) {
    delegate.set(value);
  }

  @Override
  public String getValue() {
    return delegate.getValue();
  }

  @Override
  public void setValue(String value) {
    delegate.setValue(value);
  }

  @Override
  public void addListener(ChangeListener<? super String> listener) {
    delegate.addListener(listener);
  }

  @Override
  public void removeListener(ChangeListener<? super String> listener) {
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
