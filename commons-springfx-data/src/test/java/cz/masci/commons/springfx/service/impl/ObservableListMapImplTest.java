package cz.masci.commons.springfx.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import cz.masci.commons.springfx.data.Modifiable;
import java.util.List;
import java.util.function.Function;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;

class ObservableListMapImplTest {

  private static final String ITEM_KEY = "ITEM-KEY";

  @Test
  void test_classKey() {
    var observableListMap = new ObservableListMapImpl();
    var item = new Item("test-class-key");
    ListChangeListener<Item> addListener = getAddListChangeListener(item);
    ListChangeListener<Item> removeListener = getRemoveListChangeListener(item);

    observableListMap.addListener(Item.class, addListener);
    observableListMap.add(item);
    var containsItem = observableListMap.contains(item);
    assertTrue(containsItem);
    var allList = observableListMap.getAll(Item.class);
    assertThat(allList)
        .isNotNull()
        .hasSize(1)
        .containsExactly(item);
    observableListMap.removeListener(Item.class, addListener);
    observableListMap.addListener(Item.class, removeListener);
    observableListMap.remove(item);
    observableListMap.removeListener(Item.class, removeListener);
    var allEmptyList = observableListMap.getAll(Item.class);
    assertThat(allEmptyList)
        .isNotNull()
        .hasSize(0);
    var nullContainsItem = observableListMap.contains(null);
    assertFalse(nullContainsItem);
  }
  
  @Test
  void test_StringKey() {
    var observableListMap = new ObservableListMapImpl();
    var item = new Item("test-string-key");
    ListChangeListener<Item> addListener = getAddListChangeListener(item);
    ListChangeListener<Item> removeListener = getRemoveListChangeListener(item);

    observableListMap.addListener(ITEM_KEY, addListener);
    observableListMap.add(ITEM_KEY, item);
    var containsItem = observableListMap.contains(ITEM_KEY, item);
    assertTrue(containsItem);
    var allList = observableListMap.getAll(ITEM_KEY);
    assertThat(allList)
        .isNotNull()
        .hasSize(1)
        .containsExactly(item);
    observableListMap.removeListener(ITEM_KEY, addListener);
    observableListMap.addListener(ITEM_KEY, removeListener);
    observableListMap.remove(ITEM_KEY, item);
    observableListMap.removeListener(ITEM_KEY, removeListener);
    var allEmptyList = observableListMap.getAll(ITEM_KEY);
    assertThat(allEmptyList)
        .isNotNull()
        .hasSize(0);
    var nullContainsItem = observableListMap.contains(ITEM_KEY, null);
    assertFalse(nullContainsItem);
  }

  private static ListChangeListener<Item> getAddListChangeListener(Item item) {
    return getListChangeListener(item, Change::getAddedSubList);
  }

  private static ListChangeListener<Item> getRemoveListChangeListener(Item item) {
    return getListChangeListener(item, Change::getRemoved);
  }

  private static <T extends Modifiable> ListChangeListener<T> getListChangeListener(T item, Function<ListChangeListener.Change<? extends T>, List<? extends T>> listFnc) {
    return change -> {
      assertNotNull(change);
      assertTrue(change.next());
      assertThat(listFnc.apply(change))
          .asList()
          .hasSize(1)
          .contains(item);
    };
  }

  @AllArgsConstructor
  @Getter
  static class Item implements Modifiable {
    String str;
  }
}