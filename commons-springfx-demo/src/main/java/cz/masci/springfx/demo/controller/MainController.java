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

import cz.masci.springfx.demo.interactor.BookInteractor;
import cz.masci.springfx.demo.model.BookListModel;
import cz.masci.springfx.mvci.controller.ViewProvider;
import cz.masci.springfx.mvci.util.builder.BackgroundTaskBuilder;
import cz.masci.springfx.mvci.view.builder.BorderPaneBuilder;
import javafx.scene.layout.Region;
import org.springframework.stereotype.Component;

@Component
public class MainController implements ViewProvider<Region> {

  private final BorderPaneBuilder builder;
  private final BookListModel viewModel;
  private final BookInteractor interactor;

  public MainController(BookInteractor interactor) {
    this.interactor = interactor;
    this.viewModel = new BookListModel();

    var listController = new BookListController(viewModel);
    var detailController = new BookDetailController(viewModel, interactor);
    var managerController = new BookManagerController(viewModel, interactor);

    builder = BorderPaneBuilder.builder()
        .withTop(managerController.getView())
        .withLeft(listController.getView())
        .withCenter(detailController.getView());
  }

  @Override
  public Region getView() {
    load();
    return builder.build();
  }

  private void load() {
    viewModel.getElements()
             .clear();
    BackgroundTaskBuilder.task(interactor::list)
                         .onSucceeded(viewModel.getElements()::setAll)
                         .start();
  }
}
