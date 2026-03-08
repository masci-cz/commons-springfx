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

/**
 * Controller for the Potter manager toolbar, providing buttons to save all, clear selection, and discard.
 */
@Slf4j
public class PotterManagerController {

  /** The shared Potter list view model. */
  private final PotterListModel viewModel;
  /** Builder for the commands toolbar view. */
  private final CommandsViewBuilder viewBuilder;

  /** Controller managing bulk operations on the Potter list. */
  private final OperableManagerController<Long, PotterDetailModel> operableManagerController;

  /**
   * Creates a new {@code PotterManagerController}.
   *
   * @param viewModel the Potter list model
   */
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

  /**
   * Builds and returns the toolbar view.
   *
   * @return the toolbar region
   */
  public Region getView() {
    return viewBuilder.build();
  }

  /**
   * Clears the current selection in the list view.
   */
  private void clearSelection() {
    viewModel.clearSelection();
  }

  /**
   * Discards all unsaved changes in the Potter list.
   */
  private void discardDirtyItems() {
    operableManagerController.discard();
  }

  /**
   * Saves all dirty valid Potter characters in a background task.
   *
   * @param postGuiStuff runnable executed on the JavaFX thread after saving
   */
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
