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
import cz.masci.springfx.mvci.controller.impl.OperableManagerController;
import cz.masci.springfx.mvci.util.ConcurrentUtils;
import cz.masci.springfx.mvci.util.builder.BackgroundTaskBuilder;
import cz.masci.springfx.mvci.view.builder.ButtonBuilder;
import cz.masci.springfx.mvci.view.builder.CommandsViewBuilder;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.scene.layout.Region;
import lombok.extern.slf4j.Slf4j;

/**
 * Spring component controller for the book manager toolbar view.
 * Provides buttons to create, load, save all, and discard all books.
 */
@Slf4j
public class BookManagerController implements ViewProvider<Region> {

  /** Interactor for book persistence operations. */
  private final BookInteractor interactor;

  /** Controller managing bulk operations on the book list. */
  private final OperableManagerController<Long, BookDetailModel> operableManagerController;
  /** Builder for the commands toolbar view. */
  private final CommandsViewBuilder builder;

  /**
   * Creates a new {@code BookManagerController}.
   *
   * @param viewModel  the list model providing elements and selection callbacks
   * @param interactor the interactor for book CRUD operations
   */
  public BookManagerController(BookListModel viewModel, BookInteractor interactor) {
    this.interactor = interactor;
    operableManagerController = new OperableManagerController<>(viewModel, viewModel.getElements());
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

  /**
   * Creates a new blank book and adds it to the list.
   */
  private void newBook() {
      var element = new BookDetailModel();
      element.setTitle("New Book");
      operableManagerController.add(element);
  }

  /**
   * Loads books from the interactor in a background task.
   *
   * @param postGuiStuff runnable executed on the JavaFX thread after loading
   */
  private void load(Runnable postGuiStuff) {
    BackgroundTaskBuilder
        .task(interactor::list)
        .postGuiCall(postGuiStuff)
        .onSucceeded(operableManagerController::addAll)
        .start();
  }

  /**
   * Saves all dirty valid books in a background task.
   *
   * @param postGuiStuff runnable executed on the JavaFX thread after saving
   */
  private void save(Runnable postGuiStuff) {
    AtomicInteger savedCount = new AtomicInteger(0);
    BackgroundTaskBuilder
        .task(() -> {
          operableManagerController.update((element, updateElement) -> {
            try {
              var savedElement = interactor.save(element);
              ConcurrentUtils.runInFXThread(() -> updateElement.accept(savedElement));
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

  /**
   * Discards all unsaved changes in the book list.
   */
  private void discard() {
    operableManagerController.discard();
    log.info("Changes were discarded");
  }
}
