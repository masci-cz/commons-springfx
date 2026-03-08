package cz.masci.springfx.demo.view;

import cz.masci.springfx.demo.model.LOTRDetailModel;
import javafx.scene.layout.Region;
import lombok.RequiredArgsConstructor;

/**
 * Builder for the LOTR character creation view, binding text fields directly to the provided model.
 */
@RequiredArgsConstructor
public class LOTRCreateViewBuilder extends LOTRDetailViewBuilder {

  /** The new LOTR detail model whose properties are bound to the text fields. */
  private final LOTRDetailModel viewModel;

  @Override
  public Region build() {
    viewModel.characterProperty().bind(characterTextField.textProperty());
    viewModel.locationProperty().bind(locationTextField.textProperty());

    return super.build();
  }
}
