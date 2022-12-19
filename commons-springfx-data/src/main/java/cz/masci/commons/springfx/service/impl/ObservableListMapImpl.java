package cz.masci.commons.springfx.service.impl;

import cz.masci.commons.springfx.data.Modifiable;
import cz.masci.commons.springfx.service.ObservableListMap;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.springframework.stereotype.Service;

/**
 * Implicit implementation of {@link ObservableListMap} using FXCollections.
 *
 * @author Daniel Mašek
 */
@Service
public class ObservableListMapImpl implements ObservableListMap {

  private final ObservableMap<String, ObservableList<Modifiable>> modifiedMap = FXCollections.observableHashMap();

  @Override
  public <T extends Modifiable> void add(T item) {
    if (item == null) {
      return;
    }

    add(item.getClass().getSimpleName(), item);
  }

  @Override
  public <T extends Modifiable> void add(String key, T item) {
    var modifiedList = getModifiedList(key);
    // add new item
    modifiedList.add(item);
  }

  @Override
  public <T extends Modifiable> void remove(T item) {
    if (item == null) {
      return;
    }

    remove(item.getClass().getSimpleName(), item);
  }

  @Override
  public <T extends Modifiable> void remove(String key, T item) {
    var modifiedList = getModifiedList(key);
    // remove item
    modifiedList.remove(item);
  }

  @Override
  public <T extends Modifiable> boolean contains(T item) {
    if (item == null) {
      return false;
    }

    return contains(item.getClass().getSimpleName(), item);
  }

  @Override
  public <T extends Modifiable> boolean contains(String key, T item) {
    return getModifiedList(key).contains(item);
  }

  @Override
  public <T extends Modifiable> List<T> getAll(Class<T> key) {
    return getAll(key.getSimpleName());
  }

  @Override
  public <T extends Modifiable> List<T> getAll(String key) {
    var modifiedList = getModifiedList(key);

    return modifiedList.stream().map(item -> (T) item).collect(Collectors.toList());
  }

  @Override
  public <T extends Modifiable> void addListener(Class<T> key, ListChangeListener<T> changeListener) {
    addListener(key.getSimpleName(), changeListener);
  }

  @Override
  public void addListener(String key, ListChangeListener<? extends Modifiable> changeListener) {
    ObservableList modifiedList = getModifiedList(key);

    modifiedList.addListener(changeListener);

  }

  @Override
  public <T extends Modifiable> void removeListener(Class<T> key, ListChangeListener<T> changeListener) {
    removeListener(key.getSimpleName(), changeListener);
  }

  @Override
  public void removeListener(String key, ListChangeListener<? extends Modifiable> changeListener) {
    ObservableList modifiedList = getModifiedList(key);

    modifiedList.removeListener(changeListener);

  }

  private ObservableList<Modifiable> getModifiedList(String key) {
    return modifiedMap.computeIfAbsent(key, k -> FXCollections.observableArrayList());
  }
}
