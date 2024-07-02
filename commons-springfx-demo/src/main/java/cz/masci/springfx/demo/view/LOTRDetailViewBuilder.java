package cz.masci.springfx.demo.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

public class LOTRDetailViewBuilder implements Builder<Region> {

  protected final TextField characterTextField;
  protected final TextField locationTextField;

  public LOTRDetailViewBuilder() {
    characterTextField = new TextField();
    locationTextField = new TextField();
  }

  public Region build() {
    VBox rightColumn = new VBox(characterTextField, locationTextField);
    rightColumn.setSpacing(4.0);
    rightColumn.setPadding(new Insets(5.0));
    rightColumn.setAlignment(Pos.TOP_LEFT);
    VBox leftColumn = new VBox(new Label("Character:"), new Label("Location"));
    leftColumn.setSpacing(4.0);
    leftColumn.setPadding(new Insets(5.0));
    leftColumn.setAlignment(Pos.TOP_RIGHT);

    HBox result = new HBox(leftColumn, rightColumn);
    result.setAlignment(Pos.TOP_CENTER);
    result.setSpacing(5.0);
    result.setPadding(new Insets(5.0));

    return result;
  }
}
