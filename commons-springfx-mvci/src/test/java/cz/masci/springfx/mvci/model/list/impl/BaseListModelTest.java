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

package cz.masci.springfx.mvci.model.list.impl;

import static cz.masci.springfx.mvci.TestConstants.UPDATED_TEXT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import cz.masci.springfx.mvci.TestUtils.TestDetailModel;
import java.util.Random;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

public class BaseListModelTest {

  private final BaseListModel<Integer, TestDetailModel> baseListModel = new BaseListModel<>();

  @Test
  void constructor() {
    BaseListModel<Integer, TestDetailModel> baseListModel = new BaseListModel<>(model -> new Observable[]{model.validProperty()});

    TestDetailModel testDetailModel = new TestDetailModel();

    baseListModel.getElements().addListener((ListChangeListener<? super TestDetailModel>) c -> {
      while (c.next()) {
        if (c.wasUpdated()) {
          TestDetailModel updatedModel = c.getList().get(c.getFrom());
          assertEquals(UPDATED_TEXT, updatedModel.getText());
        }
      }
    });

    baseListModel.getElements().add(testDetailModel);

    testDetailModel.setText(UPDATED_TEXT);
  }

  // region getElements
  @Test
  void getElements() {
    // Initialize test data
    ObservableList<TestDetailModel> testData = FXCollections.observableArrayList();
    Random random = new Random();
    for (int i = 0; i < 10; i++) {
      TestDetailModel testDetailModel = new TestDetailModel();
      testDetailModel.setId(random.nextInt());
      testData.add(testDetailModel);
    }

    // Initialize testable BaseListModel instance
    baseListModel.getElements()
                 .addAll(testData);

    // Test getElements method
    assertEquals(testData, baseListModel.getElements());
  }
  // endregion

  // region selectedElementProperty
  @Test
  void selectedElementProperty() {
    TestDetailModel testDetailModel = new TestDetailModel();

    // Test selectedElementProperty method
    baseListModel.selectedElementProperty()
                 .setValue(testDetailModel);
    assertEquals(testDetailModel, baseListModel.selectedElementProperty()
                                               .getValue());
  }

  @Test
  void selectedElementProperty_null() {
    // Test selectedElementProperty method when no element is selected
    assertNull(baseListModel.selectedElementProperty()
                            .getValue());
  }
  // endregion

  // region removeElement
  @Test
  void removeElement() {
    // Initialize test data
    TestDetailModel testDetailModel = new TestDetailModel();
    baseListModel.getElements()
                 .add(testDetailModel);
    baseListModel.setOnRemoveElement(element -> assertEquals(testDetailModel, element));

    // Call remove method
    baseListModel.remove(testDetailModel);

    // Assert that the element has been removed
    assertFalse(baseListModel.getElements()
                             .contains(testDetailModel));
  }

  @Test
  void removeElement_notInList() {
    // Initialize test data
    TestDetailModel testDetailModel = new TestDetailModel();
    baseListModel.setOnRemoveElement(element -> fail("onRemoveElement was called even it shouldn't be"));

    // Call remove method for an element not in the list
    baseListModel.remove(testDetailModel);

    // Assert that the list is still empty
    assertTrue(baseListModel.getElements()
                            .isEmpty());
  }

  @Test
  void removeElement_nullOnRemoveElement() {
    // Initialize test data
    TestDetailModel testDetailModel = new TestDetailModel();
    baseListModel.setOnRemoveElement(null);

    // Call remove method for an element not in the list
    baseListModel.remove(testDetailModel);

    // Assert that the list is still empty
    assertTrue(baseListModel.getElements()
                            .isEmpty());
  }
  // endregion

  // region update
  @Test
  void update() {
    TestDetailModel testDetailModel = new TestDetailModel();
    baseListModel.getElements()
                 .add(testDetailModel);
    baseListModel.setOnUpdateElementsProperty(() -> assertTrue(true));

    baseListModel.update();
  }
  // endregion

  // region select
  @Test
  void select() {
    TestDetailModel testDetailModel = new TestDetailModel();
    baseListModel.getElements()
                 .add(testDetailModel);
    baseListModel.setOnSelectElement(element -> assertEquals(testDetailModel, element));

    baseListModel.select(testDetailModel);
  }
  // endregion

  // region focus
  @Test
  void focus() {
    baseListModel.onFocusView = () -> assertTrue(true);
    baseListModel.focus();
  }
  // endregion

  // endregion
}
