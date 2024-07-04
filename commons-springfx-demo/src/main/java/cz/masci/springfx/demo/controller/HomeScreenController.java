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
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class HomeScreenController implements ViewProvider<Region> {

  private final HomeScreenViewBuilder viewBuilder;
  private final LOTRListDetailController lotrListDetailController;
  private final PotterListDetailController potterListDetailController;
  private final BookListDetailController bookListDetailController;

  public HomeScreenController(LOTRListDetailController lotrListDetailController, PotterListDetailController potterListDetailController, BookListDetailController bookListDetailController) {
    viewBuilder = new HomeScreenViewBuilder(this::openLOTRScene, this::openPotterScene, this::openBookScene);
    this.lotrListDetailController = lotrListDetailController;
    this.potterListDetailController = potterListDetailController;
    this.bookListDetailController = bookListDetailController;
  }

  @Override
  public Region getView() {
    return viewBuilder.build();
  }

  private void openLOTRScene() {
    Scene scene = new Scene(lotrListDetailController.getView(), 1000, 800);
    Stage stage = new Stage();
    stage.setTitle("Lord of the Rings");
    stage.setScene(scene);
    stage.show();
  }

  private void openPotterScene() {
    Scene scene = new Scene(potterListDetailController.getView(), 1000, 400);
    Stage stage = new Stage();
    stage.setTitle("Harry Potter");
    stage.setScene(scene);
    stage.show();
  }

  private void openBookScene() {
    Scene scene = new Scene(bookListDetailController.getView(), 1000, 400);
    Stage stage = new Stage();
    stage.setTitle("Books");
    stage.setScene(scene);
    stage.show();
  }
}
