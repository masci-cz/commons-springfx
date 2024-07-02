package cz.masci.springfx.demo.controller;

import cz.masci.springfx.demo.interactor.PotterInteractor;
import cz.masci.springfx.demo.model.PotterListModel;
import cz.masci.springfx.mvci.view.builder.BorderPaneBuilder;
import javafx.scene.layout.Region;
import org.springframework.stereotype.Component;

@Component
public class PotterListDetailController {

  private final BorderPaneBuilder viewBuilder;

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

  public Region getView() {
    return viewBuilder.build();
  }
}
