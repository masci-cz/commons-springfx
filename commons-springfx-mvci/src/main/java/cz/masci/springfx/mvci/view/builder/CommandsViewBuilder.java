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

import jakarta.annotation.Nonnull;
import java.util.List;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

/**
 * A builder class for creating a view containing a list of buttons.
 */
public class CommandsViewBuilder implements Builder<Region> {

  private final List<? extends Button> buttons;
  private final Pos alignment;
  private final boolean isVertical;

  /**
   * Create a horizontal view containing a list of buttons aligned to center left of the view.
   *
   * @param buttons a list of buttons to be displayed in the view
   */
  public CommandsViewBuilder(@Nonnull List<? extends Button> buttons) {
    this(buttons, false, Pos.CENTER_LEFT);
  }

  /**
   * Create a horizontal view containing a list of buttons aligned with provided alignment.
   *
   * @param buttons   a list of buttons to be displayed in the view
   * @param alignment the alignment of the buttons in the view
   */
  public CommandsViewBuilder(@Nonnull List<? extends Button> buttons, @Nonnull Pos alignment) {
    this(buttons, false, alignment);
  }

  /**
   * Create a horizontal or vertical view containing a list of buttons aligned with provided alignment.
   *
   * @param buttons    a list of buttons to be displayed in the view
   * @param isVertical if <code>true</code> creates vertical view
   * @param alignment  the alignment of the buttons in the view
   */
  public CommandsViewBuilder(@Nonnull List<? extends Button> buttons, boolean isVertical, @Nonnull Pos alignment) {
    this.buttons = buttons;
    this.isVertical = isVertical;
    this.alignment = alignment;
  }

  @Override
  public Region build() {
    Node[] children = buttons.toArray(new Button[]{});
    double spacing = 15.0;

    Region result = isVertical ? createVBox(spacing, alignment, children) : createHBox(spacing, alignment, children);
    result.setPadding(new Insets(10.0));

    return result;
  }

  private HBox createHBox(double spacing, Pos alignment, Node... children) {
    HBox hBox = new HBox(children);
    hBox.setSpacing(spacing);
    hBox.setAlignment(alignment);

    return hBox;
  }

  private VBox createVBox(double spacing, Pos alignment, Node... children) {
    VBox vBox = new VBox(children);
    vBox.setSpacing(spacing);
    vBox.setAlignment(alignment);

    return vBox;
  }
}
