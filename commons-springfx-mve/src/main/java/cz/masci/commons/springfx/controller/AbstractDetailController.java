package cz.masci.commons.springfx.controller;

import cz.masci.commons.springfx.data.Modifiable;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Abstract controller for item detail view.
 * <p>
 * It is responsible for hooking listeners on every observable value defined by
 * child class. When any change is risen on observable values it adds the item
 * to changed item list where it can be later taken from. Specifically by master controller.
 * </p>
 *
 * @author Daniel
 *
 * @param <T> Type of displayed item
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractDetailController<T extends Modifiable> {

  /**
   * Global observable list map
   */
  private ObservableList<T> changedItemList;

  /**
   * List of observable values for which the change event should be risen
   */
  private List<ObservableValue<String>> observableValues;
  
  /**
   * Observable values change listener
   */
  private ChangeListener<String> listener;
  
  /**
   * Controlled set item
   * -- GETTER --
   *  Returns set item. Could be null
   *
   */
  @Getter
  private T item;
  
  /**
   * Initiate observable values list
   *
   * @return List of observable values
   */
  protected abstract List<ObservableValue<String>> initObservableValues();

  /**
   * Fill nodes with set item
   *
   * @param item Set item
   */
  protected abstract void fillInputs(T item);

  /**
   * React on change of observable value
   *
   * @param observable Changed observable value
   * @param oldValue Old value
   * @param newValue New value
   */
  protected abstract void changed(ObservableValue<? extends String> observable, String oldValue, String newValue);

  /**
   * Set changed item list. When some observable values change, the values is added to this list.
   *
   * @param changedItemList Observable changed item list
   */
  public void setChangedItemList(ObservableList<T> changedItemList) {
    this.changedItemList = changedItemList;
  }

  /**
   * Set item to be controlled
   *
   * @param item Set item
   */
  public void setItem(T item) {
    log.trace("Set item: {}", item);
    
    if (this.item != null) {
      unhookListener();
    }
    this.item = item;
    hookTo(this.item);
  }

  /**
   * Unhook listener from all observable values
   */
  private void unhookListener() {
    getObservableValues().forEach(t -> t.removeListener(listener));
  }

  /**
   * Fill inputs. Hook listener to all observable values.
   *
   * @param item Item to hook
   */
  private void hookTo(T item) {
    fillInputs(item);
    if (item == null) {
      listener = null;
    } else {
      listener = (observable, oldValue, newValue) -> {
        log.trace("{} value changed from {} to {}", observable, oldValue, newValue);
        
        changed(observable, oldValue, newValue);
        if (changedItemList != null) {
          changedItemList.add(item);
        }
      };
      getObservableValues().forEach(t -> t.addListener(listener));
    }
  }

  /**
   * Get observable values. If is not set get them from {@link #initObservableValues()
   *
   * @return Observable values
   */
  private List<ObservableValue<String>> getObservableValues() {
    if (observableValues == null) {
      observableValues = List.copyOf(initObservableValues());
    }
    return observableValues;
  }

}
