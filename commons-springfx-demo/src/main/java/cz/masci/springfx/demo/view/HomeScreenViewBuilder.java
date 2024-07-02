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

import cz.masci.springfx.mvci.view.builder.ButtonBuilder;
import io.github.palexdev.materialfx.builders.layout.TilePaneBuilder;
import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.geometry.Insets;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HomeScreenViewBuilder implements Builder<Region> {

  private final Runnable openLOTRScene;
  private final Runnable openPotterScene;

  @Override
  public Region build() {
    return TilePaneBuilder.tilePane()
                          .setHGap(10.0)
                          .setVGap(10.0)
                          .setPrefColumns(2)
                          .setPrefTileHeight(50.0)
                          .addChildren(
                              ButtonBuilder.builder()
                                           .text("LOTR")
                                           .command(openLOTRScene)
                                           .styleClass("tile")
                                           .build(MFXButton::new),
                              ButtonBuilder.builder()
                                           .text("POTTER")
                                           .command(openPotterScene)
                                           .styleClass("tile")
                                           .build(MFXButton::new)
                          )
                          .setPadding(new Insets(10.0))
                          .getNode();
  }
}
