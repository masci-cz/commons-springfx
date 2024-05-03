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

package cz.masci.springfx.mvci.view.builder;

import static cz.masci.springfx.mvci.TestConstants.INITIAL_TEXT;
import static cz.masci.springfx.mvci.TestConstants.UPDATED_TEXT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cz.masci.springfx.mvci.TestDetailModel;
import cz.masci.springfx.mvci.model.list.ListModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactfx.value.Var;

@ExtendWith(MockitoExtension.class)
public class DetailViewBuilderTest {

  @Mock
  private ListModel<TestDetailModel> viewModel;

  @InjectMocks
  private DetailViewBuilder<TestDetailModel> detailViewBuilder;

  @Test
  void bindBidirectional_isBind() {
    // Arrange
    StringProperty textFieldProperty = mock(StringProperty.class);
    Var<TestDetailModel> selectedElementProperty = Var.newSimpleVar(null);

    when(viewModel.selectedElementProperty()).thenReturn(selectedElementProperty);

    // Act
    detailViewBuilder.bindBidirectional(textFieldProperty, TestDetailModel::textProperty);

    // Assert
    verify(textFieldProperty).bindBidirectional(any());
    verify(textFieldProperty, times(0)).setValue(any());
  }

  @Test
  void bindBidirectional_setValue_selectedProperty() {
    // Arrange
    StringProperty textFieldProperty = new SimpleStringProperty();
    TestDetailModel model = new TestDetailModel();
    Var<TestDetailModel> selectedElementProperty = Var.newSimpleVar(null);

    when(viewModel.selectedElementProperty()).thenReturn(selectedElementProperty);

    // Act
    detailViewBuilder.bindBidirectional(textFieldProperty, TestDetailModel::textProperty);
    selectedElementProperty.setValue(model);

    // Assert
    assertEquals(INITIAL_TEXT, textFieldProperty.getValue());

    model.setText(UPDATED_TEXT);
    assertEquals(UPDATED_TEXT, textFieldProperty.getValue());

    verify(viewModel, times(2)).update();
  }

  @Test
  void bindBidirectional_setValue_textFieldProperty() {
    // Arrange
    StringProperty textFieldProperty = new SimpleStringProperty();
    TestDetailModel model = new TestDetailModel();
    Var<TestDetailModel> selectedElementProperty = Var.newSimpleVar(model);

    when(viewModel.selectedElementProperty()).thenReturn(selectedElementProperty);

    // Act
    detailViewBuilder.bindBidirectional(textFieldProperty, TestDetailModel::textProperty);
    textFieldProperty.setValue(UPDATED_TEXT);

    // Assert
    assertEquals(UPDATED_TEXT, model.getText());
    verify(viewModel, times(1)).update();
  }
}