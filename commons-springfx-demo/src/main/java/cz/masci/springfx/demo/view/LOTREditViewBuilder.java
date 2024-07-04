package cz.masci.springfx.demo.view;

import static cz.masci.springfx.mvci.util.reactfx.ReactFxUtils.selectVarOrElseConst;

import cz.masci.springfx.demo.model.LOTRDetailModel;
import cz.masci.springfx.demo.model.LOTRListModel;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.scene.layout.Region;
import lombok.RequiredArgsConstructor;
import org.reactfx.value.Var;

@RequiredArgsConstructor
public class LOTREditViewBuilder extends LOTRDetailViewBuilder {

  private final LOTRListModel viewModel;

  @Override
  public Region build() {
    // create nullable properties
    Property<LOTRDetailModel> selectedProperty = viewModel.selectedElementProperty();

    Var<String> characterProperty = selectVarOrElseConst(selectedProperty, LOTRDetailModel::characterProperty, "");
    Var<String> locationProperty = selectVarOrElseConst(selectedProperty, LOTRDetailModel::locationProperty, "");

    // bind nullable properties to text fields
    characterTextField.textProperty().bindBidirectional(characterProperty);
    locationTextField.textProperty().bindBidirectional(locationProperty);
    // listen to changes and update source
    ChangeListener<String> changeListener = (obs, oldValue, newValue) -> viewModel.update();
    characterProperty.observeChanges(changeListener);
    locationProperty.observeChanges(changeListener);

    viewModel.setOnFocusView(characterTextField::requestFocus);

    return super.build();
  }

}
