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
import cz.masci.springfx.mvci.util.builder.MFXTableViewBuilder;
import cz.masci.springfx.mvci.view.builder.ButtonBuilder;
import io.github.palexdev.materialfx.builders.layout.StackPaneBuilder;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.util.function.Consumer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PotterListViewBuilder implements Builder<Region> {

  private final PotterListModel viewModel;
  private final Consumer<Runnable> onAddItem;

  @Override
  public Region build() {
    var addButton = ButtonBuilder.builder().text("+").styleClass("filled").command(onAddItem).build(MFXButton::new);
    return StackPaneBuilder.stackPane()
        .setAlignment(addButton, Pos.BOTTOM_RIGHT)
        .setMargin(addButton, new Insets(0.0, 30.0, 80.0, 0.0))
        .addChildren(getMFXTableView(), addButton)
        .getNode();
  }

  private Region getMFXTableView() {
    var tableView = MFXTableViewBuilder.builder(viewModel)
        .column("Book", PotterDetailModel::getBook, 200.0)
        .column("Character", PotterDetailModel::getCharacter, 100.0)
        .column("Location", PotterDetailModel::getLocation, 200.0)
        .maxHeight(Double.MAX_VALUE)
        .allowsMultipleSelection(false)
        .build();

    viewModel.setOnClearSelection(tableView.getSelectionModel()::clearSelection);

    return tableView;
  }
}
