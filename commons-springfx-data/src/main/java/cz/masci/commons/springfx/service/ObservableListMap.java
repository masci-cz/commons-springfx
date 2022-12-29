package cz.masci.commons.springfx.service;

import cz.masci.commons.springfx.data.Modifiable;
import java.util.List;
import javafx.collections.ListChangeListener;

/**
 * Modifiable observable lists identified by a key.
 * <p>
 * Every edit action (like {@link #add(String, Modifiable)}, {@link #remove(String, Modifiable)})
 * on specific list rises change list event which could be listened by external listener.
 * </p>
 * Every list is identified by key and contains {@link Modifiable} items.
 * 
 * @author Daniel Masek
 */
public interface ObservableListMap {

  /**
   * Add or update item in the list identified by item class.
   *
   * @param <T> Item type
   * @param item Item to add
   */
  <T extends Modifiable> void add(T item);

  /**
   * Add or update item in the list identified by key param.
   *
   * @param <T> Item type
   * @param key Identifier of list where to add item
   * @param item Item to add
   */
  <T extends Modifiable> void add(String key, T item);

  /**
   * Remove item from list identified by item.
   *
   * @param <T> Item type
   * @param item Item to remove
   */
  <T extends Modifiable> void remove(T item);

  /**
   * Remove item from list identified by key param.
   *
   * @param <T> Item type
   * @param key List identifier
   * @param item Item to remove
   */
  <T extends Modifiable> void remove(String key, T item);

  /**
   * Test the list identified by item class if contains the item.
   *
   * @param <T> Item type
   * @param item Item to test
   * @return <code>TRUE</code> if the list contains the item
   */
  <T extends Modifiable> boolean contains(T item);

  /**
   * Test the list identified by key param if contains the item.
   *
   * @param <T> Item type
   * @param key List identifier
   * @param item Item to test
   * @return <code>TRUE</code> if the list contains the item
   */
  <T extends Modifiable> boolean contains(String key, T item);

  /**
   * Get all items from list identified by clazz param.
   *
   * @param <T> Item type
   * @param key List identifier
   * @return All items from list
   */
  <T extends Modifiable> List<T> getAll(Class<T> key);

  /**
   * Get all items from list identified by key param.
   *
   * @param <T> Item type
   * @param key List identifier
   * @return Item list
   */
  <T extends Modifiable> List<T> getAll(String key);

  /**
   * Add list change listener on list identified by clazz param.
   *
   * @param <T> Item type
   * @param key List identifier
   * @param changeListener {@link ListChangeListener}
   */
  <T extends Modifiable> void addListener(Class<T> key, ListChangeListener<T> changeListener);

  /**
   * Add list change listener on list identified by key param.
   *
   * @param key List identifier
   * @param changeListener {@link ListChangeListener}
   */
  void addListener(String key, ListChangeListener<? extends Modifiable> changeListener);

  /**
   * Remove list change listener from list identified by clazz param.
   *
   * @param <T> Item type
   * @param clazz List identifier
   * @param changeListener {@link ListChangeListener}
   */
  <T extends Modifiable> void removeListener(Class<T> clazz, ListChangeListener<T> changeListener);

  /**
   * Remove list change listener from list identified by key param.
   *
   * @param key List identifier
   * @param changeListener {@link ListChangeListener}
   */
  void removeListener(String key, ListChangeListener<? extends Modifiable> changeListener);
}
