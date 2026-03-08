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
 * <p>
 *   Button builder.
 *   Based on this builder the new button is created and configured with the command to process.
 *   The {@link ButtonBuilder#builder()} is used to build button with set attributes.
 *   Provide button supplier with button class to be created.
 * </p>
 *
 *   Last set command will win. Either consumable or runnable command. If no command is set, no action is set.
 *   <dl>
 *     <dt>appThreadCommand
 *     <dd>- the command is executed with accepting button related process running in JavaFX main thread. It is used for long processes running in app thread.
 *     <dt>fxThreadCommand
 *     <dd>- the command is executed before button related process. It is used for quick processes not running in app thread but in JavaFX main thread.
 *   </dl>
 *   Remaining attributes are
 *   <dl>
 *     <dt>disableExpression
 *     <dd>- expression connected to button disable property. It is combined with internal disable property
 *     <dt>text
 *     <dd>- button text.
 *     <dt>styleClass
 *     <dd>- button style class.
 *   </dl>
 */
public class ButtonBuilder {
  /** The command accepting a post-action runnable to be executed on the app thread. */
  private Consumer<Runnable> appThreadCommand;
  /** The command to execute directly on the JavaFX thread. */
  private Runnable fxThreadCommand;
  /** Expression bound to the button's disable property. */
  BooleanExpression disableExpression;
  /** The text to display on the button. */
  private String text;
  /** The CSS style class to add to the button. */
  private String styleClass;

  /**
   * Creates a new {@code ButtonBuilder} instance.
   */
  protected ButtonBuilder() {}

  /**
   * Creates a new {@code ButtonBuilder} instance.
   *
   * @return a new {@code ButtonBuilder}
   */
  public static ButtonBuilder builder() {
    return new ButtonBuilder();
  }

  /**
   * Sets the command to execute on the application thread, receiving a post-GUI runnable.
   *
   * @param appThreadCommand the consumer to invoke with the post-GUI callback
   * @return this builder
   */
  public ButtonBuilder command(Consumer<Runnable> appThreadCommand) {
    this.appThreadCommand = appThreadCommand;
    fxThreadCommand = null;

    return this;
  }

  /**
   * Sets the command to execute directly on the JavaFX thread before the button action completes.
   *
   * @param fxThreadCommand the runnable to execute on the JavaFX thread
   * @return this builder
   */
  public ButtonBuilder command(Runnable fxThreadCommand) {
    this.fxThreadCommand = fxThreadCommand;
    appThreadCommand = null;

    return this;
  }

  /**
   * Sets the boolean expression bound to the button's disable property.
   *
   * @param disableExpression the expression to bind; combined with internal disable property
   * @return this builder
   */
  public ButtonBuilder disableExpression(BooleanExpression disableExpression) {
    this.disableExpression = disableExpression;

    return this;
  }

  /**
   * Sets the text label on the button.
   *
   * @param text the text to display
   * @return this builder
   */
  public ButtonBuilder text(String text) {
    this.text = text;

    return this;
  }

  /**
   * Adds a CSS style class to the button.
   *
   * @param styleClass the style class to add
   * @return this builder
   */
  public ButtonBuilder styleClass(String styleClass) {
    this.styleClass = styleClass;

    return this;
  }

  /**
   * Builds a button of the type provided by the supplier, applying all configured properties.
   *
   * @param buttonSupplier supplier that creates the button instance
   * @param <T>            the type of button to build
   * @return the fully configured button
   */
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
