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

import cz.masci.springfx.demo.model.LOTRDetailModel;
import cz.masci.springfx.demo.model.LOTRListModel;
import cz.masci.springfx.mvci.util.builder.MFXTableViewBuilder;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LOTRListViewBuilder implements Builder<Region> {

  private final LOTRListModel viewModel;

  @Override
  public Region build() {
    var tableView = MFXTableViewBuilder.builder(viewModel)
        .column("Character", LOTRDetailModel::getCharacter, 200.0)
        .column("Location", LOTRDetailModel::getLocation, 200.0)
        .maxHeight(Double.MAX_VALUE)
        .maxWidth(Double.MAX_VALUE)
        .allowsMultipleSelection(false)
        .build();

    viewModel.setOnClearSelection(() -> {
      tableView.getSelectionModel().clearSelection();
      viewModel.select(null);
    });

    return tableView;
  }
}
