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
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Builder;


/**
 * A builder class for creating a view containing a list of buttons.
 */
public class CommandsViewBuilder implements Builder<Region> {

  private final List<? extends Button> buttons;
  private final Pos alignment;

  public CommandsViewBuilder(@Nonnull List<? extends Button> buttons) {
    this(buttons, Pos.CENTER_LEFT);
  }

  public CommandsViewBuilder(@Nonnull List<? extends Button> buttons, @Nonnull Pos alignment) {
    this.buttons = buttons;
    this.alignment = alignment;
  }

  @Override
  public Region build() {
    HBox hBox = new HBox(buttons.toArray(new Button[]{}));
    hBox.setSpacing(15.0);
    hBox.setPadding(new Insets(10.0));
    hBox.setAlignment(alignment);

    return hBox;
  }

}
