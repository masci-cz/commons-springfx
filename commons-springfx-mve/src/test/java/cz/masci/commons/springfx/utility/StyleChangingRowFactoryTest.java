package cz.masci.commons.springfx.utility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cz.masci.commons.springfx.ItemOne;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(ApplicationExtension.class)
class StyleChangingRowFactoryTest {

  private static final String STYLE_CLASS = "style-class";
  public static final ItemOne ITEM = new ItemOne("test");

  @Test
  void call_rowPropertyChangeListener() {
    ObservableList<ItemOne> selectionList = FXCollections.observableArrayList();
    StyleChangingRowFactory<ItemOne> styleChangingRowFactory = new StyleChangingRowFactory<>(STYLE_CLASS, selectionList);

    // when
    var tableRow = styleChangingRowFactory.call(null);
    selectionList.add(ITEM);

    // then
    // check not set item
    assertThat(tableRow.getStyleClass())
        .doesNotContain(STYLE_CLASS);

    // check set item
    tableRow.setItem(ITEM);
    assertThat(tableRow.getStyleClass())
        .contains(STYLE_CLASS);

    // check unset item
    tableRow.setItem(null);
    assertThat(tableRow.getStyleClass())
        .doesNotContain(STYLE_CLASS);
  }

  @Test
  void call_listChangeListener() {
    ObservableList<ItemOne> selectionList = FXCollections.observableArrayList();
    StyleChangingRowFactory<ItemOne> styleChangingRowFactory = new StyleChangingRowFactory<>(STYLE_CLASS, selectionList);

    // when
    var tableRow = styleChangingRowFactory.call(null);
    tableRow.setItem(ITEM);

    // then
    // check not set item
    assertThat(tableRow.getStyleClass())
        .doesNotContain(STYLE_CLASS);

    // check add item to list
    selectionList.add(ITEM);
    assertThat(tableRow.getStyleClass())
        .contains(STYLE_CLASS);

    // check remove item from list
    selectionList.remove(ITEM);
    assertThat(tableRow.getStyleClass())
        .doesNotContain(STYLE_CLASS);
  }

  @Test
  void call_baseFactory() {
    TableView<ItemOne> tableView = mock(TableView.class);
    Callback<TableView<ItemOne>, TableRow<ItemOne>> baseFactory = mock(Callback.class);
    ObservableList<ItemOne> selectionList = FXCollections.observableArrayList();
    StyleChangingRowFactory<ItemOne> styleChangingRowFactory = new StyleChangingRowFactory<>(STYLE_CLASS, selectionList, baseFactory);

    when(baseFactory.call(tableView)).thenReturn(new TableRow<>());

    // when
    var tableRow = styleChangingRowFactory.call(tableView);

    // then
    assertNotNull(tableRow);
  }
}