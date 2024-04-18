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

package cz.masci.springfx.demo.view;

import static cz.masci.springfx.mvci.util.reactfx.ReactFxUtils.selectVarOrElseConst;
import static cz.masci.springfx.mvci.util.MFXBuilderUtils.createTextField;

import cz.masci.springfx.mvci.util.constraint.ConstraintUtils;
import cz.masci.springfx.mvci.util.property.PropertyUtils;
import cz.masci.springfx.demo.model.BookDetailModel;
import cz.masci.springfx.demo.model.BookListModel;
import cz.masci.springfx.mvci.util.BuilderUtils;
import io.github.palexdev.materialfx.builders.layout.VBoxBuilder;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;
import org.reactfx.value.Var;

@RequiredArgsConstructor
public class BookDetailViewBuilder implements Builder<Region> {

  private final BookListModel viewModel;

  @Override
  public Region build() {
    Var<BookDetailModel> selectedProperty = viewModel.selectedElementProperty();

    // create nodes to show
    var titleTextField = createTextField("Title", Double.MAX_VALUE);
    var titleIsNotEmptyConstraint = ConstraintUtils.isNotEmptyWhenPropertyIsNotEmpty(titleTextField.textProperty(), selectedProperty, "Title");
    var titleTextFieldWithValidation = BuilderUtils.enhanceValidatedNodeWithSupportingText(titleTextField,
        PropertyUtils.not(titleTextField.delegateFocusedProperty())::addListener, titleIsNotEmptyConstraint);

    var authorTextField = createTextField("Author", Double.MAX_VALUE);

    // prepare nullable properties from the selected property
    Var<String> titleProperty = selectVarOrElseConst(selectedProperty, BookDetailModel::titleProperty, "");
    Var<String> authorProperty = selectVarOrElseConst(selectedProperty, BookDetailModel::authorProperty, "");
    // bind nullable properties to text fields
    titleTextField.textProperty()
                  .bindBidirectional(titleProperty);
    authorTextField.textProperty()
                   .bindBidirectional(authorProperty);

    // listen to changes and update source properties in the list view model
    ChangeListener<String> changeListener = (obs, oldValue, newValue) -> viewModel.updateElementsProperty();
    titleProperty.observeChanges(changeListener);
    authorProperty.observeChanges(changeListener);

    // which node should be selected when list view model focusView is called
    viewModel.setOnFocusView(titleTextField::requestFocus);

    // create region to show
    return VBoxBuilder.vBox()
                      .setSpacing(10.0)
                      .addChildren(titleTextFieldWithValidation, authorTextField)
                      .setPadding(new Insets(5.0, 5.0, 5.0, 10.0))
                      .getNode();
  }

}
