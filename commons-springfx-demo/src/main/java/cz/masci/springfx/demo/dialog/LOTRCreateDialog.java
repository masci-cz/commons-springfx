package cz.masci.springfx.demo.dialog;

import cz.masci.springfx.demo.model.LOTRDetailModel;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

public class LOTRCreateDialog extends Dialog<LOTRDetailModel> {

  public LOTRCreateDialog() {
    super();
    var character = new LOTRDetailModel();
    var builder = new LOTRCreateViewBuilder(character);
    setTitle("Create LOTR character");
    getDialogPane().getButtonTypes().add(ButtonType.OK);
    getDialogPane().setContent(builder.build());
    setResultConverter(buttonType -> {
      if (ButtonType.OK.equals(buttonType)) {
        character.characterProperty().unbind();
        character.locationProperty().unbind();
        character.rebaseline();
        return character;
      }
      return null;
    });
  }

}
