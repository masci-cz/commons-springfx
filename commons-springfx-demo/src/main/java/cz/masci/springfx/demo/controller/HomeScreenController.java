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

package cz.masci.springfx.demo.controller;

import cz.masci.springfx.demo.view.HomeScreenViewBuilder;
import cz.masci.springfx.mvci.controller.ViewProvider;
import javafx.scene.layout.Region;
import org.springframework.stereotype.Component;

@Component
public class HomeScreenController implements ViewProvider<Region> {

  private final HomeScreenViewBuilder viewBuilder;

  public HomeScreenController() {
    viewBuilder = new HomeScreenViewBuilder(this::openLOTRScene, this::openPotterScene);
  }

  @Override
  public Region getView() {
    return viewBuilder.build();
  }

  private void openLOTRScene() {
    System.out.println("LOTR");
  }

  private void openPotterScene() {
    System.out.println("POTTER");
  }
}
