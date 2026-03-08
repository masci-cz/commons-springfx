package cz.masci.springfx.demo.controller;

import cz.masci.springfx.demo.interactor.PotterInteractor;
import cz.masci.springfx.demo.model.PotterListModel;
import cz.masci.springfx.mvci.view.builder.BorderPaneBuilder;
import javafx.scene.layout.Region;
import org.springframework.stereotype.Component;

/**
 * Controller for potter character list and detail view.
 *
 * @author Daniel Mašek
 */
@Component
public class PotterListDetailController {

  /** The view builder for constructing the combined list and detail view. */
  private final BorderPaneBuilder viewBuilder;

  /**
   * Creates a new instance of PotterListDetailController with the given interactor.
   *
   * @param interactor the interactor for handling potter character operations
   */
  public PotterListDetailController(PotterInteractor interactor) {
    var listViewModel = new PotterListModel();
    var listController = new PotterListController(listViewModel, interactor);
    var detailController = new PotterDetailController(listViewModel, interactor);
    var managerController = new PotterManagerController(listViewModel);

    viewBuilder = BorderPaneBuilder.builder()
        .withLeft(listController.getView())
        .withCenter(detailController.getView())
        .withTop(managerController.getView());
  }

  /**
   * Returns the combined view of the potter character list and detail.
   *
   * @return the combined view of the potter character list and detail
   */
  public Region getView() {
    return viewBuilder.build();
  }
}
