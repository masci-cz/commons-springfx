package cz.masci.commons.springfx.service.impl;

import cz.masci.commons.springfx.service.ObservableListMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

/**
 * Implicit implementation of {@link ObservableListMap} using FXCollections.
 *
 * @author Daniel Mašek
 */
@Service
public class ObservableListMapNewImpl {

  private final ObservableMap<String, ObservableList<Object>> modifiedMap = FXCollections.observableHashMap();

  public <T> boolean add(String key, T item) {
    var modifiedList = getModifiedList(key);
    return modifiedList.add(item);
  }

  public <T> boolean remove(String key, T item) {
    var modifiedList = getModifiedList(key);
    return modifiedList.remove(item);
  }

  public <T> boolean contains(String key, T item) {
    var modifiedList = getModifiedList(key);
    return modifiedList.contains(item);
  }

  public <T> T get(String key, int index) {
    var modifiedList = getModifiedList(key);
    return (T) modifiedList.get(index);
  }

  private ObservableList<Object> getModifiedList(String key) {
    return modifiedMap.computeIfAbsent(key, mapKey -> FXCollections.observableArrayList());
  }

  public static <T> String keyFactory(T item) {
    return item.getClass().getSimpleName();
  }

  @Data
  @AllArgsConstructor
  static class ItemOne {
    private String message;
  }

  @Data
  @AllArgsConstructor
  static class ItemTwo {
    private String message;
    private int number;
  }

  static class MappedObservableList {
    private Class<?> storedClass;
    private String key;
    private final ObservableList<Object> observableList = FXCollections.observableArrayList();

    public <T> boolean add(String key, T item) {
      if (this.key.equals(key) && storedClass.isInstance(item)) {
        return observableList.add(item);
      }
      return false;
    }

    public <T> T get(String key, int index) {
      if (this.key.equals(key)) {
        return (T) observableList.get(index);
      }
      return null;
    }

    public <T> boolean remove(String key, T item) {
      if (this.key.equals(key)) {
        return observableList.remove(item);
      }
      return false;
    }

    public <T> boolean contains(String key, T item) {
      if (this.key.equals(key)) {
        return observableList.contains(item);
      }
      return false;
    }


  }
}
