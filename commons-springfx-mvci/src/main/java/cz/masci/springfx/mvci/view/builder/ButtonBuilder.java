/*
 * Copyright (c) 2023
 *
 * This file is part of commons-springfx library.
 *
 * commons-springfx library is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option)
 *   any later version.
 *
 * commons-springfx library is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *   FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 *    License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *  along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */

package cz.masci.springfx.mvci.view.builder;

import java.util.function.Consumer;
import java.util.function.Supplier;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import org.apache.commons.lang3.StringUtils;

/**
 * <pre>
 *   Button builder.
 *   Based on this builder the new button is created and configured with the command to process.
 *   The {@link ButtonBuilder#builder()} is used to build button with set attributes.
 *   Provide button supplier with button class to be created.
 * </pre>
 *
 * <pre>
 *   Last set command will win. Either consumable or runnable command. If no command is set, no action is set.
 *   <dl>
 *     <dt>appThreadCommand</dt><dd>- the command is executed with accepting button related process running in JavaFX main thread. It is used for long processes running in app thread.</dd>
 *     <dt>fxThreadCommand</dt><dd>- the command is executed before button related process. It is used for quick processes not running in app thread but in JavaFX main thread.</dd>
 *   </dl>
 *   Remaining attributes are
 *   <dl>
 *     <dt>disableExpression</dt><dd>- expression connected to button disable property. It is combined with internal disable property</dd>
 *     <dt>text</dt><dd>- button text.</dd>
 *     <dt>styleClass</dt><dd>- button style class.</dd>
 *   </dl>
 * </pre>
 */
public class ButtonBuilder {
  private Consumer<Runnable> appThreadCommand;
  private Runnable fxThreadCommand;
  BooleanExpression disableExpression;
  private String text;
  private String styleClass;

  protected ButtonBuilder() {}

  public static ButtonBuilder builder() {
    return new ButtonBuilder();
  }

  public ButtonBuilder command(Consumer<Runnable> appThreadCommand) {
    this.appThreadCommand = appThreadCommand;
    fxThreadCommand = null;

    return this;
  }

  public ButtonBuilder command(Runnable fxThreadCommand) {
    this.fxThreadCommand = fxThreadCommand;
    appThreadCommand = null;

    return this;
  }

  public ButtonBuilder disableExpression(BooleanExpression disableExpression) {
    this.disableExpression = disableExpression;

    return this;
  }

  public ButtonBuilder text(String text) {
    this.text = text;

    return this;
  }

  public ButtonBuilder styleClass(String styleClass) {
    this.styleClass = styleClass;

    return this;
  }

  public <T extends Button> T build(Supplier<T> buttonSupplier) {
    var button = buttonSupplier.get();

    // set text
    button.setText(text);
    // set style class
    if (StringUtils.isNotBlank(styleClass)) {
      button.getStyleClass().add(styleClass);
    }
    // set disable property
    BooleanProperty internalDisableProperty = new SimpleBooleanProperty(false);
    button.disableProperty().bind(
        disableExpression != null
        ? Bindings.or(disableExpression, internalDisableProperty)
        : internalDisableProperty
    );
    // set on action
    if (appThreadCommand != null) {
      button.setOnAction(evt -> {
        internalDisableProperty.set(true);
        appThreadCommand.accept(() -> internalDisableProperty.set(false));
      });
    }
    if (fxThreadCommand != null) {
      button.setOnAction(evt -> {
        internalDisableProperty.set(true);
        fxThreadCommand.run();
        internalDisableProperty.set(false);
      });
    }

    return button;
  }
}
