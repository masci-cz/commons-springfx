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

import static cz.masci.springfx.mvci.util.MFXBuilderUtils.createTextField;

import cz.masci.springfx.demo.model.BookDetailModel;
import cz.masci.springfx.demo.model.BookListModel;
import cz.masci.springfx.mvci.util.BuilderUtils;
import cz.masci.springfx.mvci.util.constraint.ConstraintUtils;
import cz.masci.springfx.mvci.util.property.PropertyUtils;
import cz.masci.springfx.mvci.view.builder.DetailViewBuilder;
import io.github.palexdev.materialfx.builders.layout.VBoxBuilder;
import javafx.beans.property.Property;
import javafx.geometry.Insets;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class BookDetailViewBuilder extends DetailViewBuilder<BookDetailModel> implements Builder<Region> {

  public BookDetailViewBuilder(BookListModel viewModel) {
    super(viewModel);
  }

  @Override
  public Region build() {
    Property<BookDetailModel> selectedProperty = viewModel.selectedElementProperty();

    // create nodes to show
    var titleTextField = createTextField("Title", Double.MAX_VALUE);
    var titleIsNotEmptyConstraint = ConstraintUtils.isNotEmptyWhenPropertyIsNotEmpty(titleTextField.textProperty(), selectedProperty, "Field Title is required");
    var titleTextFieldWithValidation = BuilderUtils.enhanceValidatedNodeWithSupportingText(titleTextField,
        PropertyUtils.not(titleTextField.delegateFocusedProperty())::addListener, titleIsNotEmptyConstraint);

    var authorTextField = createTextField("Author", Double.MAX_VALUE);

    // bind text fields bidirectional with detail model properties
    bindBidirectional(titleTextField.textProperty(), BookDetailModel::titleProperty);
    bindBidirectional(authorTextField.textProperty(), BookDetailModel::authorProperty);

    // which node should be selected when list view model focus is called
    viewModel.setOnFocusView(titleTextField::requestFocus);

    // create region to show
    return VBoxBuilder.vBox()
                      .setSpacing(10.0)
                      .addChildren(titleTextFieldWithValidation, authorTextField)
                      .setPadding(new Insets(5.0, 5.0, 5.0, 10.0))
                      .getNode();
  }

}
