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

package cz.masci.springfx.mvci.controller.impl;

import static cz.masci.springfx.mvci.TestConstants.DETAIL_MODEL_ID;
import static cz.masci.springfx.mvci.TestConstants.EMPTY_TEXT;
import static cz.masci.springfx.mvci.TestConstants.UPDATED_TEXT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cz.masci.springfx.mvci.TestDetailModel;
import cz.masci.springfx.mvci.model.list.Removable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactfx.value.Var;

@ExtendWith(MockitoExtension.class)
class OperableDetailControllerTest {

  private final Var<TestDetailModel> selectedElement = Var.newSimpleVar(null);
  @Mock
  private Removable<TestDetailModel> removable;

  private OperableDetailController<Integer, TestDetailModel> operableDetailController;

  @BeforeEach
  void setUp() {
    operableDetailController = new OperableDetailController<>(selectedElement, removable);
  }

  @Test
  void saveDisabledProperty() {
    var property = operableDetailController.saveDisabledProperty();
    // after init
    assertNotNull(property);
    assertTrue(property.get());
    // select element
    var model = new TestDetailModel();
    selectedElement.setValue(model);
    assertTrue(property.get());
    // update element value -> not valid dirty
    model.setText(EMPTY_TEXT);
    assertTrue(property.get());
    // update element value -> valid dirty
    model.setText(UPDATED_TEXT);
    assertFalse(property.get());
    // reset/rebase element
    model.reset();
    assertTrue(property.get());
    // deselect element
    selectedElement.setValue(null);
    assertTrue(property.get());
  }

  @Test
  void discardDisabledProperty() {
    var property = operableDetailController.discardDisabledProperty();
    // after init
    assertNotNull(property);
    assertTrue(property.get());
    // select element
    var model = new TestDetailModel();
    selectedElement.setValue(model);
    assertTrue(property.get());
    // update element value -> not valid dirty
    model.setText(EMPTY_TEXT);
    assertFalse(property.get());
    // update element value -> valid dirty
    model.setText(UPDATED_TEXT);
    assertFalse(property.get());
    // reset/rebase element
    model.reset();
    assertTrue(property.get());
    // deselect element
    selectedElement.setValue(null);
    assertTrue(property.get());
  }

  @Test
  void deleteDisabledProperty() {
    var property = operableDetailController.deleteDisabledProperty();
    // after init
    assertNotNull(property);
    assertTrue(property.get());
    // select element
    var model = new TestDetailModel();
    selectedElement.setValue(model);
    assertFalse(property.get());
    // update element value -> not valid dirty
    model.setText(EMPTY_TEXT);
    assertFalse(property.get());
    // update element value -> valid dirty
    model.setText(UPDATED_TEXT);
    assertFalse(property.get());
    // reset/rebase element
    model.reset();
    assertFalse(property.get());
    // deselect element
    selectedElement.setValue(null);
    assertTrue(property.get());
  }

  // region discard
  @Test
  void discard_notEnabled() {
    operableDetailController.discard();

    assertNull(selectedElement.getValue());
    verify(removable, never()).remove(any());
  }

  @Test
  void discard_isTransient() {
    TestDetailModel element = mock(TestDetailModel.class);
    BooleanProperty isDirty = new SimpleBooleanProperty(true);

    when(element.isDirtyProperty()).thenReturn(isDirty);
    when(element.isTransient()).thenReturn(true);

    selectedElement.setValue(element);
    operableDetailController.discard();

    verify(removable).remove(eq(element));
    verify(element, never()).reset();
  }

  @Test
  void discard_isNotTransient() {
    TestDetailModel element = mock(TestDetailModel.class);
    BooleanProperty isDirty = new SimpleBooleanProperty(true);

    when(element.isDirtyProperty()).thenReturn(isDirty);
    when(element.isTransient()).thenReturn(false);

    selectedElement.setValue(element);
    operableDetailController.discard();

    verify(removable, never()).remove(eq(element));
    verify(element).reset();
  }
  // endregion

  // region update
  @Test
  void update_notEnabled() {
    operableDetailController.update((item, updatedElementAction) -> fail("Update action is called when shouldn't"));

    assertNull(selectedElement.getValue());
  }

  @Test
  void update_isTransient() {
    TestDetailModel element = mock(TestDetailModel.class);
    BooleanProperty isDirty = new SimpleBooleanProperty(true);
    BooleanProperty isValid = new SimpleBooleanProperty(true);

    when(element.isDirtyProperty()).thenReturn(isDirty);
    when(element.validProperty()).thenReturn(isValid);
    when(element.isTransient()).thenReturn(true);

    selectedElement.setValue(element);
    operableDetailController.update((item, updateElementAction) -> {
      assertEquals(element, item);
      var updatedItem = new TestDetailModel();
      updatedItem.setId(DETAIL_MODEL_ID);
      updateElementAction.accept(updatedItem);
    });

    verify(element).setId(DETAIL_MODEL_ID);
    verify(element).rebaseline();
  }

  @Test
  void update_isNotTransient() {
    TestDetailModel element = mock(TestDetailModel.class);
    BooleanProperty isDirty = new SimpleBooleanProperty(true);
    BooleanProperty isValid = new SimpleBooleanProperty(true);

    when(element.isDirtyProperty()).thenReturn(isDirty);
    when(element.validProperty()).thenReturn(isValid);
    when(element.isTransient()).thenReturn(false);

    selectedElement.setValue(element);
    operableDetailController.update((item, updateElementAction) -> {
      assertEquals(element, item);
      var updatedItem = new TestDetailModel();
      updatedItem.setId(DETAIL_MODEL_ID);
      updateElementAction.accept(updatedItem);
    });

    verify(element, never()).setId(DETAIL_MODEL_ID);
    verify(element).rebaseline();
  }
  // endregion

  // region remove
  @Test
  void remove_notEnabled() {
    operableDetailController.remove((item, removeAction) -> fail("Update action is called when shouldn't"));

    assertNull(selectedElement.getValue());
    verify(removable, never()).remove(any());
  }

  @Test
  void remove_enabled() {
    TestDetailModel element = new TestDetailModel();

    selectedElement.setValue(element);
    operableDetailController.remove((item, removeAction) -> removeAction.run());

    verify(removable).remove(element);
  }
  // endregion

}