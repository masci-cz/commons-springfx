/*
 * Copyright (c) 2023
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

import java.util.Optional;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Builder;

/**
 * A border pane with provided region is created. Region could be null.
 */
public class ListDetailViewBuilder implements Builder<Region> {

  private Region left;
  private Region right;
  private Region center;
  private Region top;
  private Region bottom;

  public static ListDetailViewBuilder builder() {
    return new ListDetailViewBuilder();
  }

  @Override
  public Region build() {
    BorderPane result = new BorderPane();
    result.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));

    Optional.ofNullable(left).ifPresent(result::setLeft);
    Optional.ofNullable(right).ifPresent(result::setRight);
    Optional.ofNullable(center).ifPresent(result::setCenter);
    Optional.ofNullable(top).ifPresent(result::setTop);
    Optional.ofNullable(bottom).ifPresent(result::setBottom);

    return result;
  }

  public ListDetailViewBuilder withLeft(Region view) {
    left = view;
    return this;
  }

  public ListDetailViewBuilder withRight(Region view) {
    right = view;
    return this;
  }

  public ListDetailViewBuilder withCenter(Region view) {
    center = view;
    return this;
  }

  public ListDetailViewBuilder withTop(Region view) {
    top = view;
    return this;
  }

  public ListDetailViewBuilder withBottom(Region view) {
    bottom = view;
    return this;
  }
}
