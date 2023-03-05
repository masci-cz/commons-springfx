package cz.masci.commons.springfx.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import cz.masci.commons.springfx.ItemOne;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

class AbstractDetailControllerTest {

  private final ObservableList<ItemOne> changedItemList = FXCollections.observableArrayList();

  @Test
  void setItem() {
    var item = new ItemOne("TEST");
    var testDetailController = new TestDetailController();

    testDetailController.setItem(item);
    testDetailController.name.set("NEW TEXT");

    assertEquals("NEW TEXT", item.getMessage());
    assertThat(changedItemList)
        .isNotEmpty()
        .contains(item);

    testDetailController.setItem(null);

    assertNull(testDetailController.name.get());
    assertThat(changedItemList)
        .isNotEmpty()
        .contains(item);
  }

  private class TestDetailController extends AbstractDetailController<ItemOne> {

    final StringProperty name = new SimpleStringProperty();

    public TestDetailController() {
      super(changedItemList);
    }

    @Override
    protected List<ObservableValue<String>> initObservableValues() {
      return List.of(name);
    }

    @Override
    protected void fillInputs(ItemOne item) {
      if (item != null) {
        name.set(item.getMessage());
      } else {
        name.set(null);
      }
    }

    @Override
    protected void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
      getItem().setMessage(newValue);
    }
  }
}