package cz.masci.springfx.demo.controller;

import cz.masci.springfx.demo.model.LOTRListModel;
import cz.masci.springfx.demo.view.LOTRListViewBuilder;
import cz.masci.springfx.mvci.controller.impl.SimpleController;
import javafx.scene.layout.Region;

/**
 * Controller for the LOTR character list view, extending {@link SimpleController}.
 */
public class LOTRListController extends SimpleController<Region, LOTRListViewBuilder> {

  /**
   * Creates a new {@code LOTRListController} for the given list model.
   *
   * @param listModel the LOTR list model
   */
  public LOTRListController(LOTRListModel listModel) {
    super(new LOTRListViewBuilder(listModel));
  }
}
