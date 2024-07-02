package cz.masci.springfx.demo.controller;

import cz.masci.springfx.demo.interactor.LOTRInteractor;
import cz.masci.springfx.demo.model.LOTRListModel;
import cz.masci.springfx.mvci.view.builder.BorderPaneBuilder;
import javafx.scene.layout.Region;
import org.springframework.stereotype.Component;

@Component
public class LOTRListDetailController {

  private final BorderPaneBuilder viewBuilder;

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

  public Region getView() {
    return viewBuilder.build();
  }
}
