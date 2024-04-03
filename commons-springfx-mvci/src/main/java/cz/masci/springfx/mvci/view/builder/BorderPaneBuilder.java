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
 * A border pane builder with provided regions is created. Regions could be null.
 * If the padding is null the implicit paddings are used (5.0)
 */
public class BorderPaneBuilder implements Builder<BorderPane> {

  private Region left;
  private Region right;
  private Region center;
  private Region top;
  private Region bottom;
  private Insets padding;

  public static BorderPaneBuilder builder() {
    return new BorderPaneBuilder();
  }

  @Override
  public BorderPane build() {
    BorderPane result = new BorderPane();
    result.setPadding(padding == null ? new Insets(5.0, 5.0, 5.0, 5.0) : padding);

    Optional.ofNullable(left).ifPresent(result::setLeft);
    Optional.ofNullable(right).ifPresent(result::setRight);
    Optional.ofNullable(center).ifPresent(result::setCenter);
    Optional.ofNullable(top).ifPresent(result::setTop);
    Optional.ofNullable(bottom).ifPresent(result::setBottom);

    return result;
  }

  public BorderPaneBuilder withLeft(Region view) {
    left = view;
    return this;
  }

  public BorderPaneBuilder withRight(Region view) {
    right = view;
    return this;
  }

  public BorderPaneBuilder withCenter(Region view) {
    center = view;
    return this;
  }

  public BorderPaneBuilder withTop(Region view) {
    top = view;
    return this;
  }

  public BorderPaneBuilder withBottom(Region view) {
    bottom = view;
    return this;
  }

  public BorderPaneBuilder withPadding(Insets padding) {
    this.padding = padding;
    return this;
  }
}
