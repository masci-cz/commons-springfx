package cz.masci.springfx.demo.view;

import cz.masci.springfx.demo.model.LOTRDetailModel;
import javafx.scene.layout.Region;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LOTRCreateViewBuilder extends LOTRDetailViewBuilder {

  private final LOTRDetailModel viewModel;

  @Override
  public Region build() {
    viewModel.characterProperty().bind(characterTextField.textProperty());
    viewModel.locationProperty().bind(locationTextField.textProperty());

    return super.build();
  }
}
