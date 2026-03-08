package cz.masci.springfx.demo.controller;

import cz.masci.springfx.demo.interactor.LOTRInteractor;
import cz.masci.springfx.demo.model.LOTRListModel;
import cz.masci.springfx.mvci.view.builder.BorderPaneBuilder;
import javafx.scene.layout.Region;
import org.springframework.stereotype.Component;

/**
 * Spring component controller that combines the LOTR list, detail, and manager views
 * into a single border-pane layout.
 */
@Component
public class LOTRListDetailController {

  /** Builder for the combined border-pane view. */
  private final BorderPaneBuilder viewBuilder;

  /**
   * Creates a new {@code LOTRListDetailController}, wiring up sub-controllers.
   *
   * @param interactor the interactor providing LOTR character persistence operations
   */
  public LOTRListDetailController(LOTRInteractor interactor) {
    var listViewModel = new LOTRListModel();
    var listController = new LOTRListController(listViewModel);
    var managerController = new LOTRManagerController(listViewModel, interactor);
    var detailController = new LOTRDetailController(listViewModel);

    viewBuilder = BorderPaneBuilder.builder()
        .withLeft(listController.getView())
        .withCenter(detailController.getView())
        .withTop(managerController.getView());
  }

  /**
   * Builds and returns the combined LOTR list-detail view.
   *
   * @return the view region
   */
  public Region getView() {
    return viewBuilder.build();
  }
}
