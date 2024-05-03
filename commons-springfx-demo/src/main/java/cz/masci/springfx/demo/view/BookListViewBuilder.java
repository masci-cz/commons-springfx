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

import cz.masci.springfx.demo.model.BookDetailModel;
import cz.masci.springfx.demo.model.BookListModel;
import cz.masci.springfx.mvci.util.builder.MFXTableViewBuilder;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BookListViewBuilder implements Builder<Region> {

  private final BookListModel viewModel;

  @Override
  public Region build() {
    MFXTableViewBuilder<Long, BookDetailModel> builder = MFXTableViewBuilder.builder(viewModel);
    builder.maxHeight(Double.MAX_VALUE);
    builder.maxWidth(Double.MAX_VALUE);
    builder.column("Title", BookDetailModel::getTitle, 300.0);
    builder.column("Author", BookDetailModel::getAuthor);
    builder.allowsMultipleSelection(false);

    return builder.build();
  }
}
