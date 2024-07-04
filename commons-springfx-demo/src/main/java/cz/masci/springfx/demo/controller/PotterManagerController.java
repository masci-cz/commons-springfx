package cz.masci.springfx.demo.controller;

import cz.masci.springfx.demo.model.PotterDetailModel;
import cz.masci.springfx.demo.model.PotterListModel;
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

@Slf4j
public class PotterManagerController {

  private final PotterListModel viewModel;
  private final CommandsViewBuilder viewBuilder;

  private final OperableManagerController<Long, PotterDetailModel> operableManagerController;

  public PotterManagerController(PotterListModel viewModel) {
    this.viewModel = viewModel;
    this.operableManagerController = new OperableManagerController<>(viewModel, viewModel.getElements());
    this.viewBuilder = new CommandsViewBuilder(
        List.of(
            ButtonBuilder.builder().text("Save all").command(this::saveCharacters).styleClass("filledTonal").build(MFXButton::new),
            ButtonBuilder.builder().text("Clear selection").command(this::clearSelection).styleClass("outlined").build(MFXButton::new),
            ButtonBuilder.builder().text("Discard").command(this::discardDirtyItems).styleClass("outlined").build(MFXButton::new)
        )
    );
  }

  public Region getView() {
    return viewBuilder.build();
  }

  // Run in FX thread
  private void clearSelection() {
    viewModel.clearSelection();
  }

  private void discardDirtyItems() {
    operableManagerController.discard();
  }

  private void saveCharacters(Runnable postGuiStuff) {
    AtomicInteger savedCount = new AtomicInteger(0);
    BackgroundTaskBuilder
        .task(() -> {
          operableManagerController.update((element, updateElement) -> {
            try {
              element.rebaseline();
              ConcurrentUtils.runInFXThread(() -> updateElement.accept(element));
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

}
