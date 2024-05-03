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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cz.masci.springfx.mvci.TestDetailModel;
import cz.masci.springfx.mvci.model.list.Focusable;
import cz.masci.springfx.mvci.model.list.Removable;
import cz.masci.springfx.mvci.model.list.Selectable;
import cz.masci.springfx.mvci.model.list.impl.BaseListModel;
import java.util.List;
import java.util.stream.Stream;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OperableManagerControllerTest {

  @Mock
  private Selectable<TestDetailModel> selectable;
  @Mock
  private Focusable focusable;
  @Mock
  private Removable<TestDetailModel> removable;
  @Mock
  private ObservableList<TestDetailModel> modelElements;

  @InjectMocks
  private OperableManagerController<Integer, TestDetailModel> controller;

  @Test
  void constructor() {
    BaseListModel<Integer, TestDetailModel> listModel = new BaseListModel<>();

    OperableManagerController<Integer, TestDetailModel> localController = new OperableManagerController<>(listModel, listModel.getElements());

    assertNotNull(localController);
  }

  // region add
  @Test
  void add() {
    var element = new TestDetailModel();

    controller.add(element);

    verify(selectable).select(element);
    verify(focusable).focus();
    verify(modelElements).add(element);
  }
  // endregion

  // region addAll
  @Test
  void addAll() {
    var newElements = List.of(new TestDetailModel(), new TestDetailModel());

    controller.addAll(newElements);

    verify(selectable).select(eq(null));
    verify(modelElements).clear();
    verify(modelElements).addAll(newElements);
  }
  // endregion

  // region update
  @Test
  void update() {
    var notDirtyElement = createTestDetailModel(false, null, null);
    var notValidElement = createTestDetailModel(true, false, null);
    var transientElement = createTestDetailModel(true, true, true);
    var notTransientElement = createTestDetailModel(true, true, false);

    when(modelElements.stream()).thenReturn(Stream.of(notDirtyElement, notValidElement, transientElement, notTransientElement));

    controller.update((element, updateAction) -> {
      var updatedElement = new TestDetailModel();
      updatedElement.setId(DETAIL_MODEL_ID);
      updateAction.accept(updatedElement);
    });

    verify(notDirtyElement, never()).isValid();
    verify(notDirtyElement, never()).isTransient();
    verify(notDirtyElement, never()).setId(any());
    verify(notDirtyElement, never()).rebaseline();

    verify(notValidElement, never()).isTransient();
    verify(notValidElement, never()).setId(any());
    verify(notValidElement, never()).rebaseline();

    verify(transientElement).setId(DETAIL_MODEL_ID);
    verify(transientElement).rebaseline();

    verify(notTransientElement, never()).setId(any());
    verify(notTransientElement).rebaseline();
  }
  // endregion

  // region discard
  @Test
  void discard() {
    var notDirtyElement = createTestDetailModel(false, null, null);
    var transientElement = createTestDetailModel(true, null, true);
    var notTransientElement = createTestDetailModel(true, null, false);

    when(modelElements.stream()).thenReturn(Stream.of(notDirtyElement, transientElement, notTransientElement));

    controller.discard();

    verify(notDirtyElement, never()).isTransient();
    verify(notDirtyElement, never()).reset();

    verify(transientElement, never()).reset();

    verify(notTransientElement).reset();

    verify(removable).remove(transientElement);
  }
  // endregion


  private TestDetailModel createTestDetailModel(Boolean dirty, Boolean valid, Boolean isTransient) {
    var testDetailModel = mock(TestDetailModel.class);
    if (dirty != null) {
      when(testDetailModel.isDirty()).thenReturn(dirty);
    }
    if (valid != null) {
      when(testDetailModel.isValid()).thenReturn(valid);
    }
    if (isTransient != null) {
      when(testDetailModel.isTransient()).thenReturn(isTransient);
    }

    return testDetailModel;
  }

}