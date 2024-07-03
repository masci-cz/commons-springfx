package cz.masci.springfx.demo.controller;

import cz.masci.springfx.demo.dialog.LOTRCreateDialog;
import cz.masci.springfx.demo.interactor.LOTRInteractor;
import cz.masci.springfx.demo.model.LOTRDetailModel;
import cz.masci.springfx.demo.model.LOTRListModel;
import cz.masci.springfx.mvci.controller.impl.OperableManagerController;
import cz.masci.springfx.mvci.util.ConcurrentUtils;
import cz.masci.springfx.mvci.util.builder.BackgroundTaskBuilder;
import cz.masci.springfx.mvci.view.builder.ButtonBuilder;
import cz.masci.springfx.mvci.view.builder.CommandsViewBuilder;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.Region;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LOTRManagerController {

  private final LOTRListModel viewModel;
  private final LOTRInteractor interactor;

  private final OperableManagerController<Long, LOTRDetailModel> operableManagerController;
  private final CommandsViewBuilder viewBuilder;

  public LOTRManagerController(LOTRListModel viewModel, LOTRInteractor interactor) {
    this.viewModel = viewModel;
    this.interactor = interactor;

    this.operableManagerController = new OperableManagerController<>(viewModel, viewModel.getElements());
    this.viewBuilder = new CommandsViewBuilder(
        List.of(
            ButtonBuilder.builder().text("Add").command(this::addCharacter).build(Button::new),
            ButtonBuilder.builder().text("Save all").command(this::saveCharacters).build(Button::new),
            ButtonBuilder.builder().text("Discard").command(this::discardDirtyItems).build(Button::new),
            ButtonBuilder.builder().text("Delete").command(this::deleteCharacter).build(Button::new),
            ButtonBuilder.builder().text("Reload").command(this::loadCharacters).build(Button::new),
            ButtonBuilder.builder().text("Clear selection").command(this::clearSelection).build(Button::new)
        )
    );
  }

  public Region getView() {
    loadCharacters(() -> {});
    return viewBuilder.build();
  }

  // Run in app thread
  private void addCharacter(Runnable postGuiStuff) {
    var dialog = new LOTRCreateDialog();
    dialog.showAndWait()
        .ifPresent(model ->
            BackgroundTaskBuilder
                .task(() -> interactor.saveCharacter(model))
                .onSucceeded(operableManagerController::add)
                .postGuiCall(postGuiStuff)
                .start()
        );
  }

  // Run in FX thread
  private void clearSelection() {
    viewModel.clearSelection();
  }

  private void discardDirtyItems() {
    operableManagerController.discard();
  }

  private void loadCharacters(Runnable postGuiStuff) {
    BackgroundTaskBuilder
        .task(interactor::loadCharacters)
        .postGuiCall(postGuiStuff)
        .onSucceeded(operableManagerController::addAll)
        .start();
  }

  private void deleteCharacter(Runnable postGuiStuff) {
    var confirmDialog = new Dialog<ButtonType>();
    confirmDialog.setTitle("Delete LOTR character confirmation");
    confirmDialog.setContentText("Do you want to delete selected LOTR character?");
    confirmDialog.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
    confirmDialog.showAndWait()
        .filter(ButtonType.YES::equals)
        .ifPresent(unused -> viewModel.getElements().remove(viewModel.getSelectedElement()));
    postGuiStuff.run();
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
