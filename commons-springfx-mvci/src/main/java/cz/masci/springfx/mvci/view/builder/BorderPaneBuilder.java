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

  /** The left region of the border pane. */
  private Region left;
  /** The right region of the border pane. */
  private Region right;
  /** The center region of the border pane. */
  private Region center;
  /** The top region of the border pane. */
  private Region top;
  /** The bottom region of the border pane. */
  private Region bottom;
  /** The padding to apply to the border pane. */
  private Insets padding;

  /**
   * Creates a new instance of BorderPaneBuilder.
   *
   * @return a new instance of BorderPaneBuilder
   */
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

  /**
   * Sets the left region of the border pane.
   *
   * @param view the region to place on the left
   * @return this builder
   */
  public BorderPaneBuilder withLeft(Region view) {
    left = view;
    return this;
  }

  /**
   * Sets the right region of the border pane.
   *
   * @param view the region to place on the right
   * @return this builder
   */
  public BorderPaneBuilder withRight(Region view) {
    right = view;
    return this;
  }

  /**
   * Sets the center region of the border pane.
   *
   * @param view the region to place in the center
   * @return this builder
   */
  public BorderPaneBuilder withCenter(Region view) {
    center = view;
    return this;
  }

  /**
   * Sets the top region of the border pane.
   *
   * @param view the region to place at the top
   * @return this builder
   */
  public BorderPaneBuilder withTop(Region view) {
    top = view;
    return this;
  }

  /**
   * Sets the bottom region of the border pane.
   *
   * @param view the region to place at the bottom
   * @return this builder
   */
  public BorderPaneBuilder withBottom(Region view) {
    bottom = view;
    return this;
  }

  /**
   * Sets the padding of the border pane.
   *
   * @param padding the insets to use as padding
   * @return this builder
   */
  public BorderPaneBuilder withPadding(Insets padding) {
    this.padding = padding;
    return this;
  }
}
