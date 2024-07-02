package cz.masci.springfx.demo.controller;

import cz.masci.springfx.demo.interactor.PotterInteractor;
import cz.masci.springfx.demo.model.PotterListModel;
import cz.masci.springfx.demo.view.PotterListViewBuilder;
import cz.masci.springfx.mvci.controller.ViewProvider;
import cz.masci.springfx.mvci.util.builder.BackgroundTaskBuilder;
import javafx.scene.layout.Region;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PotterListController implements ViewProvider<Region> {

  private final PotterInteractor interactor;
  private final PotterListModel viewModel;

  private final PotterListViewBuilder viewBuilder;

  public PotterListController(PotterListModel listModel, PotterInteractor interactor) {
    this.viewModel = listModel;
    this.interactor = interactor;
    this.viewBuilder = new PotterListViewBuilder(listModel, this::addCharacter);
  }

  @Override
  public Region getView() {
    return viewBuilder.build();
  }

  private void addCharacter(Runnable postGuiStuff) {
    BackgroundTaskBuilder
        .task(interactor::addCharacter)
        .onFailed(task -> {
          var e = task.getException();
          log.error("Error saving LOTR character", e);
        })
        .onSucceeded(savedItem -> {
          viewModel.getElements().add(savedItem);
          log.info("Potter character was saved");
        })
        .postGuiCall(postGuiStuff)
        .start();
  }

}
