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

import cz.masci.springfx.demo.model.LOTRDetailModel;
import cz.masci.springfx.demo.model.LOTRListModel;
import cz.masci.springfx.demo.view.LOTREditViewBuilder;
import cz.masci.springfx.mvci.controller.ViewProvider;
import cz.masci.springfx.mvci.controller.impl.OperableDetailController;
import cz.masci.springfx.mvci.controller.impl.SimpleController;
import cz.masci.springfx.mvci.util.ConcurrentUtils;
import cz.masci.springfx.mvci.util.builder.BackgroundTaskBuilder;
import cz.masci.springfx.mvci.view.builder.ButtonBuilder;
import cz.masci.springfx.mvci.view.builder.CommandsViewBuilder;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LOTRDetailController implements ViewProvider<Region> {

  private final OperableDetailController<Long, LOTRDetailModel> operableDetailController;

  private final Builder<Region> builder;

  public LOTRDetailController(LOTRListModel viewModel) {
    operableDetailController = new OperableDetailController<>(viewModel.selectedElementProperty(), viewModel);

    var detailViewBuilder = new LOTREditViewBuilder(viewModel);
    var detailController = new SimpleController<>(detailViewBuilder);
    var commandViewBuilder = new CommandsViewBuilder(
        List.of(
            ButtonBuilder.builder().text("Save").command(this::saveItem).styleClass("filledTonal").disableExpression(operableDetailController.saveDisabledProperty()).build(MFXButton::new),
            ButtonBuilder.builder().text("Cancel").command(this::discardDirtyItem).styleClass("outlined").disableExpression(operableDetailController.discardDisabledProperty()).build(MFXButton::new),
            ButtonBuilder.builder().text("Delete").command(this::deleteItem).disableExpression(operableDetailController.deleteDisabledProperty()).styleClass("outlined").build(MFXButton::new)
        ),
        Pos.CENTER_RIGHT
    );

    builder = createDetailWithCommandViewBuilder(detailController.getView(), commandViewBuilder.build());
  }

  @Override
  public Region getView() {
    return builder.build();
  }

  private void saveItem(Runnable postGuiStuff) {
    operableDetailController.update((item, afterSave) ->
        BackgroundTaskBuilder
            .task(() -> {
              ConcurrentUtils.runInFXThread(() -> afterSave.accept(item));
              return item;
            })
            .onFailed(task -> {
              var e = task.getException();
              log.error("Error saving LOTR character", e);
            })
            .onSucceeded(savedItem -> log.info("LOTR character was saved"))
            .postGuiCall(postGuiStuff)
            .start());
  }

  private void discardDirtyItem() {
    operableDetailController.discard();
  }

  private void deleteItem(Runnable postGuiStuff) {
    operableDetailController.remove((item, afterDelete) ->
        BackgroundTaskBuilder
            .task(() -> {
              log.info("Deleting item");
              ConcurrentUtils.runInFXThread(afterDelete);
              return item;
            })
            .onFailed(task -> log.error("Something happen when saving LOTR character", task.getException()))
            .onSucceeded(deletedItem -> log.info("LOTR character was deleted"))
            .postGuiCall(postGuiStuff)
            .start());
  }
}
