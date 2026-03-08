package cz.masci.springfx.demo.dialog;

import cz.masci.springfx.demo.model.LOTRDetailModel;
import cz.masci.springfx.demo.view.LOTRCreateViewBuilder;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

/**
 * Dialog for creating a new LOTR character, showing a form and returning
 * the populated {@link LOTRDetailModel} on confirmation.
 */
public class LOTRCreateDialog extends Dialog<LOTRDetailModel> {

  /**
   * Creates a new {@code LOTRCreateDialog}, setting up the form and result converter.
   */
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
