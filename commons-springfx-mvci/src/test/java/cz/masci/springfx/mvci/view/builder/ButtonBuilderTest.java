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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.function.Consumer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Button;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(ApplicationExtension.class)
class ButtonBuilderTest {

  private static final String BUTTON_TEXT = "TEXT";
  private static final String BUTTON_STYLE_CLASS = "STYLE_CLASS";

  @Test
  void build_simple() {
    var result = ButtonBuilder.builder().text(BUTTON_TEXT).styleClass(BUTTON_STYLE_CLASS).build(Button::new);

    assertAll("Simple button",
        () -> assertNotNull(result),
        () -> assertEquals(BUTTON_TEXT, result.getText()),
        () -> assertTrue(result.getStyleClass().contains(BUTTON_STYLE_CLASS))
    );
  }

  @Test
  void build_disableExpression() {
    BooleanProperty disableProperty = new SimpleBooleanProperty(false);

    var result = ButtonBuilder.builder().text(BUTTON_TEXT).disableExpression(disableProperty).build(Button::new);

    assertFalse(result.isDisabled());

    // change disable property
    disableProperty.set(true);

    assertTrue(result.isDisabled());
  }

  @Test
  void build_commands_consumable() {
    Button button = new Button();

    Consumer<Runnable> command = runnable -> {
      assertTrue(button.isDisabled());
      runnable.run();
      assertFalse(button.isDisabled());
    };

    var result = ButtonBuilder.builder().text(BUTTON_TEXT).command(command).build(() -> button);

    result.fire();
  }

  @Test
  void build_commands_runnable() {
    Button button = new Button();

    Runnable command = () -> assertTrue(button.isDisabled());

    var result = ButtonBuilder.builder().text(BUTTON_TEXT).command(command).build(() -> button);

    result.fire();

    assertFalse(button.isDisabled());
  }

  @Test
  void build_commands_priority_ok() {
    Consumer<Runnable> consumableCommand = runnable -> {
      runnable.run();
      assertTrue(true);
    };

    Runnable runnableCommand = () -> fail("Runnable command ran even it shouldn't");

    var result = ButtonBuilder.builder().text(BUTTON_TEXT)
        .command(runnableCommand) // first command set
        .command(consumableCommand) // second command set win
        .build(Button::new);

    result.fire();
  }

}