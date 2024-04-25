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
import cz.masci.springfx.demo.model.BookDetailModel;
import cz.masci.springfx.demo.model.BookListModel;
import cz.masci.springfx.mvci.controller.ViewProvider;
import cz.masci.springfx.mvci.model.detail.DirtyModel;
import cz.masci.springfx.mvci.util.BackgroundTaskBuilder;
import cz.masci.springfx.mvci.util.ConcurrentUtils;
import cz.masci.springfx.mvci.view.builder.ButtonBuilder;
import cz.masci.springfx.mvci.view.builder.CommandsViewBuilder;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import javafx.scene.layout.Region;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BookManagerController implements ViewProvider<Region> {

  private final BookListModel viewModel;
  private final BookInteractor interactor;

  private final CommandsViewBuilder builder;

  public BookManagerController(BookListModel viewModel, BookInteractor interactor) {
    this.viewModel = viewModel;
    this.interactor = interactor;

    builder = new CommandsViewBuilder(
        List.of(
            ButtonBuilder.builder().text("New").command(this::newBook).styleClass("outlined").build(MFXButton::new),
            ButtonBuilder.builder().text("Load").command(this::load).styleClass("outlined").build(MFXButton::new),
            ButtonBuilder.builder().text("Save All").command(this::save).styleClass("filledTonal").build(MFXButton::new),
            ButtonBuilder.builder().text("Discard All").command(this::discard).styleClass("outlined").build(MFXButton::new)
        )
    );
  }

  @Override
  public Region getView() {
    return builder.build();
  }

  private void newBook() {
      var element = new BookDetailModel();
      element.setTitle("New Book");
      viewModel.getElements().add(element);
      viewModel.select(element);
      viewModel.focus();
  }

  private void load(Runnable postGuiStuff) {
    viewModel.select(null);
    viewModel.getElements().clear();
    BackgroundTaskBuilder
        .task(interactor::list)
        .postGuiCall(postGuiStuff)
        .onSucceeded(viewModel.getElements()::setAll)
        .start();
  }

  private void save(Runnable postGuiStuff) {
    AtomicInteger savedCount = new AtomicInteger(0);
    BackgroundTaskBuilder
        .task(() -> {
          getDirtyElements().forEach(item -> {
            try {
              var savedItem = interactor.save(item);
              ConcurrentUtils.runInFXThread(() -> {
                if (item.isTransient()) {
                  item.setId(savedItem.getId());
                }
                item.rebaseline();
              });
              savedCount.incrementAndGet();
            } catch (Exception e) {
              log.error("Something went wrong when saving", e);
            }
          });
          return null;
        })
        .onSucceeded(unused -> log.info("{} records were saved", savedCount.get()))
        .postGuiCall(postGuiStuff)
        .start();
  }

  private void discard() {
    var elementsToRemove = new ArrayList<BookDetailModel>();
    getDirtyElements().forEach(element -> {
      if (element.isTransient()) {
        elementsToRemove.add(element);
      } else {
        element.reset();
      }
    });
    elementsToRemove.forEach(viewModel::remove);
    log.info("Changes were discarded");
  }

  private Stream<BookDetailModel> getDirtyElements() {
    return viewModel.getElements().stream().filter(DirtyModel::isDirty);
  }
}
