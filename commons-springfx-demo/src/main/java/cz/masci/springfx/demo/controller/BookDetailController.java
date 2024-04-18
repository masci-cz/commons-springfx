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

import static cz.masci.springfx.mvci.util.BuilderUtils.createDetailWithCommandViewBuilder;

import cz.masci.springfx.demo.interactor.BookInteractor;
import cz.masci.springfx.demo.model.BookDetailModel;
import cz.masci.springfx.demo.model.BookListModel;
import cz.masci.springfx.demo.view.BookDetailViewBuilder;
import cz.masci.springfx.mvci.controller.ViewProvider;
import cz.masci.springfx.mvci.controller.impl.SimpleController;
import cz.masci.springfx.mvci.model.detail.DirtyModel;
import cz.masci.springfx.mvci.model.detail.ValidModel;
import cz.masci.springfx.mvci.util.BackgroundTaskBuilder;
import cz.masci.springfx.mvci.util.ConcurrentUtils;
import cz.masci.springfx.mvci.view.builder.ButtonBuilder;
import cz.masci.springfx.mvci.view.builder.CommandsViewBuilder;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import lombok.extern.slf4j.Slf4j;
import org.reactfx.value.Val;

@Slf4j
public class BookDetailController implements ViewProvider<Region> {

  private final Val<BookDetailModel> selectedItemProperty;
  private final BookListModel viewModel;
  private final BookInteractor interactor;

  private final BooleanProperty saveDisableProperty = new SimpleBooleanProperty(true);
  private final BooleanProperty discardDisableProperty = new SimpleBooleanProperty(true);
  private final BooleanProperty deleteDisableProperty = new SimpleBooleanProperty(true);

  private final Builder<Region> builder;

  public BookDetailController(BookListModel viewModel, BookInteractor interactor) {
    this.selectedItemProperty = Val.wrap(viewModel.selectedElementProperty());
    this.viewModel = viewModel;
    this.interactor = interactor;

    var detailViewBuilder = new BookDetailViewBuilder(viewModel);
    var detailController = new SimpleController<>(detailViewBuilder);
    var commandViewBuilder = new CommandsViewBuilder(
        List.of(
            ButtonBuilder.builder().text("Save").command(this::saveItem).styleClass("filledTonal").disableExpression(saveDisableProperty).build(MFXButton::new),
            ButtonBuilder.builder().text("Cancel").command(this::discardDirtyItem).styleClass("outlined").disableExpression(discardDisableProperty).build(MFXButton::new),
            ButtonBuilder.builder().text("Delete").command(this::deleteItem).disableExpression(deleteDisableProperty).styleClass("outlined").build(MFXButton::new)
        ),
        Pos.CENTER_RIGHT
    );

    initDisableProperties();

    builder = createDetailWithCommandViewBuilder(detailController.getView(), commandViewBuilder.build());
  }

  @Override
  public Region getView() {
    return builder.build();
  }

  private void initDisableProperties() {
    // delete disabled => not selected
    Val<Boolean> dirtyProperty = selectedItemProperty.flatMap(DirtyModel::isDirtyProperty);
    Val<Boolean> validProperty = selectedItemProperty.flatMap(ValidModel::validProperty);
    Val<Boolean> saveDisable = Val.combine(dirtyProperty, validProperty, (dirty, valid) -> !dirty || !valid);
    deleteDisableProperty.bind(Bindings.createBooleanBinding(selectedItemProperty::isEmpty, selectedItemProperty));
    saveDisableProperty.bind(Bindings.createBooleanBinding(() -> selectedItemProperty.isEmpty() || saveDisable.getOrElse(true), selectedItemProperty, saveDisable));
    discardDisableProperty.bind(Bindings.createBooleanBinding(() -> selectedItemProperty.isEmpty() || !dirtyProperty.getOrElse(true), selectedItemProperty, dirtyProperty));
  }

  private void discardDirtyItem() {
    if (!discardDisableProperty.get()) {
      selectedItemProperty.ifPresent(item -> {
        if (item.isTransient()) {
          viewModel.removeElement(item);
        } else {
          item.reset();
        }
      });
    }
  }

  private void saveItem(Runnable postGuiStuff) {
    if (!saveDisableProperty.get()) {
      selectedItemProperty.ifPresent(item ->
          BackgroundTaskBuilder
              .task(() -> {
                var savedItem = interactor.save(item);
                ConcurrentUtils.runInFXThread(() -> {
                  if (item.isTransient()) {
                    item.setId(savedItem.getId());
                  }
                  item.rebaseline();
                });
                return savedItem;
              })
              .onFailed(task -> {
                var e = task.getException();
                log.error("Error saving book", e);
              })
              .onSucceeded(savedItem -> log.info("Book was saved"))
              .postGuiCall(postGuiStuff)
              .start()
      );
    }
  }

  private void deleteItem(Runnable postGuiStuff) {
    if (!deleteDisableProperty.get()) {
      selectedItemProperty.ifPresent(item -> BackgroundTaskBuilder
          .task(() -> {
            log.info("Deleting item");
            interactor.delete(item);
            ConcurrentUtils.runInFXThread(() -> viewModel.removeElement(item));
            return item;
          })
          .onFailed(task -> log.error("Something happen when saving book", task.getException()))
          .onSucceeded(deletedItem -> log.info("Book was deleted"))
          .postGuiCall(postGuiStuff)
          .start());
    }
  }
}
