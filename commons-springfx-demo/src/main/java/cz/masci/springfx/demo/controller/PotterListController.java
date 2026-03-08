package cz.masci.springfx.demo.controller;

import cz.masci.springfx.demo.interactor.PotterInteractor;
import cz.masci.springfx.demo.model.PotterListModel;
import cz.masci.springfx.demo.view.PotterListViewBuilder;
import cz.masci.springfx.mvci.controller.ViewProvider;
import cz.masci.springfx.mvci.util.builder.BackgroundTaskBuilder;
import javafx.scene.layout.Region;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for potter character list view.
 *
 * @author Daniel Mašek
 */
@Slf4j
public class PotterListController implements ViewProvider<Region> {

  /** The interactor for handling potter character operations. */
  private final PotterInteractor interactor;
  /** The view model for the potter character list. */
  private final PotterListModel viewModel;

  /** The view builder for constructing the potter character list view. */
  private final PotterListViewBuilder viewBuilder;

  /**
   * Creates a new instance of PotterListController with the given list model and interactor.
    *
   * @param listModel the view model for the potter character list
   * @param interactor the interactor for handling potter character operations
   */
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
