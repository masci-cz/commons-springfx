package cz.masci.commons.springfx.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import cz.masci.commons.springfx.data.Modifiable;
import cz.masci.commons.springfx.service.impl.ObservableListMapImplTest.Item;
import cz.masci.commons.springfx.service.impl.ObservableListMapNewImpl.ItemOne;
import cz.masci.commons.springfx.service.impl.ObservableListMapNewImpl.ItemTwo;
import java.util.List;
import java.util.function.Function;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class ObservableListMapNewImplTest {

  @Test
  void test() {
    var observableListMap = new ObservableListMapNewImpl();
    var item = new ItemOne("message");
    var key = ObservableListMapNewImpl.keyFactory(item);

    assertTrue(observableListMap.add(key, item));
    assertTrue(observableListMap.contains(key, item));
    assertTrue(observableListMap.remove(key, item));
  }

  @Disabled
  @Test
  void test2() {
    var observableListMap = new ObservableListMapNewImpl();
    var item1 = new ItemOne("message");
    var item2 = new ItemTwo("message 2", 25);
    var key1 = ObservableListMapNewImpl.keyFactory(item1);
    var key2 = ObservableListMapNewImpl.keyFactory(item2);

    assertTrue(observableListMap.add(key1, item1));
    assertTrue(observableListMap.add(key2, item2));
    assertTrue(observableListMap.contains(key1, item1));
    assertTrue(observableListMap.contains(key2, item2));
    assertTrue(observableListMap.remove(key1, item1));
    assertTrue(observableListMap.remove(key2, item2));

    assertTrue(observableListMap.add(key1, item2));
    assertTrue(observableListMap.add(key2, item1));

    ItemOne itemOne = observableListMap.get(key1, 0);
    ItemTwo itemTwo = observableListMap.get(key2, 0);
  }

  @Test
  void test3() {
    ObservableList<ItemOne> observableListItemOne = FXCollections.observableArrayList();
    var itemOne = new ItemOne("item one");
    ListChangeListener<ItemOne> addListener = getAddListChangeListener(itemOne);
    ListChangeListener<ItemOne> removeListener = getRemoveListChangeListener(itemOne);

    observableListItemOne.addListener(addListener);
    assertTrue(observableListItemOne.add(itemOne));
    var result = observableListItemOne.get(0);
    assertThat(result)
        .isNotNull()
        .isSameAs(itemOne);
    observableListItemOne.removeListener(addListener);

    observableListItemOne.addListener(removeListener);
    assertTrue(observableListItemOne.remove(itemOne));
    observableListItemOne.removeListener(removeListener);
  }

  private static <T> ListChangeListener<T> getAddListChangeListener(T item) {
    return getListChangeListener(item, Change::getAddedSubList);
  }

  private static <T> ListChangeListener<T> getRemoveListChangeListener(T item) {
    return getListChangeListener(item, Change::getRemoved);
  }

  private static <T> ListChangeListener<T> getListChangeListener(T item, Function<Change<? extends T>, List<? extends T>> listFnc) {
    return change -> {
      assertNotNull(change);
      assertTrue(change.next());
      assertThat(listFnc.apply(change))
          .asList()
          .hasSize(1)
          .contains(item);
    };
  }

  @Data
  @AllArgsConstructor
  private static class ItemOne {
    private String message;
  }

  @Data
  @AllArgsConstructor
  private static class ItemTwo {
    private String message;
    private int number;
  }

}