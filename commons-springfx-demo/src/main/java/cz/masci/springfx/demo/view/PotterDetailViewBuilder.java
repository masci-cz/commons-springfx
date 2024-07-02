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

import cz.masci.springfx.demo.model.PotterDetailModel;
import cz.masci.springfx.demo.model.PotterListModel;
import cz.masci.springfx.mvci.util.BuilderUtils;
import cz.masci.springfx.mvci.util.MFXBuilderUtils;
import cz.masci.springfx.mvci.util.constraint.ConstraintUtils;
import cz.masci.springfx.mvci.util.property.PropertyUtils;
import cz.masci.springfx.mvci.view.builder.DetailViewBuilder;
import io.github.palexdev.materialfx.builders.layout.VBoxBuilder;
import javafx.beans.property.Property;
import javafx.geometry.Insets;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class PotterDetailViewBuilder extends DetailViewBuilder<PotterDetailModel> implements Builder<Region> {

  public PotterDetailViewBuilder(PotterListModel viewModel) {
    super(viewModel);
  }

  @Override
  public Region build() {
    Property<PotterDetailModel> selectedProperty = viewModel.selectedElementProperty();

    var bookTextField = MFXBuilderUtils.createTextField("Book", Double.MAX_VALUE);
    var bookConstraint = ConstraintUtils.isNotEmptyWhenPropertyIsNotEmpty(bookTextField.textProperty(), selectedProperty, "Book");
    var bookTextFieldWithValidation = BuilderUtils.enhanceValidatedNodeWithSupportingText(bookTextField, PropertyUtils.not(bookTextField.delegateFocusedProperty())::addListener, bookConstraint);

    var characterTextField = MFXBuilderUtils.createTextField("Character", Double.MAX_VALUE);
    var characterConstraint = ConstraintUtils.isNotEmptyWhenPropertyIsNotEmpty(characterTextField.textProperty(), selectedProperty, "Character");
    var characterTextFieldWithValidation = BuilderUtils.enhanceValidatedNodeWithSupportingText(characterTextField, characterTextField.delegateFocusedProperty().not()::addListener, characterConstraint);

    var locationTextField = MFXBuilderUtils.createTextField("Location", Double.MAX_VALUE);
    var quoteTextField = MFXBuilderUtils.createTextField("Quote", Double.MAX_VALUE);

    return VBoxBuilder.vBox()
        .setSpacing(10.0)
        .setChildren(bookTextFieldWithValidation, characterTextFieldWithValidation, locationTextField, quoteTextField)
        .setPadding(new Insets(5.0))
        .getNode();
  }
}
