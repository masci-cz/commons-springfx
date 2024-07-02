package cz.masci.springfx.demo.controller;

import cz.masci.springfx.demo.model.LOTRListModel;
import cz.masci.springfx.demo.view.LOTRListViewBuilder;
import cz.masci.springfx.mvci.controller.impl.SimpleController;
import javafx.scene.layout.Region;

public class LOTRListController extends SimpleController<Region, LOTRListViewBuilder> {

  public LOTRListController(LOTRListModel listModel) {
    super(new LOTRListViewBuilder(listModel));
  }
}
