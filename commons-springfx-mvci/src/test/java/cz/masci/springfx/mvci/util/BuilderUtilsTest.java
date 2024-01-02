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

package cz.masci.springfx.mvci.util;

import static cz.masci.springfx.mvci.util.BuilderUtils.INVALID_PSEUDO_CLASS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.MFXValidator;
import io.github.palexdev.materialfx.validation.Validated;
import java.util.Objects;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Label;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(ApplicationExtension.class)
class BuilderUtilsTest {

  @Test
  void enhanceValidatedNodeWithSupportingText() {
    TestNode node = new TestNode("Initial");
    BooleanProperty validate = new SimpleBooleanProperty(false);
    Constraint constraint = Constraint.of("Text is missing", node.textProperty().isNotEmpty());

    var result = BuilderUtils.enhanceValidatedNodeWithSupportingText(node, validate::addListener, constraint);

    var supportingTextOpt = result.getChildrenUnmodifiable().stream().filter(child -> !Objects.equals(node, child)).findFirst();

    assertTrue(supportingTextOpt.isPresent());
    var supportingText = (Label) supportingTextOpt.get();

    // validate valid node
    validate.set(true);
    assertFalse(supportingText.isVisible());
    assertFalse(supportingText.isManaged());
    assertFalse(node.getPseudoClassStates().contains(INVALID_PSEUDO_CLASS));

    // change validity
    validate.set(false);
    node.setText("");
    validate.set(true);
    assertTrue(supportingText.isVisible());
    assertTrue(supportingText.isManaged());
    assertTrue(node.getPseudoClassStates().contains(INVALID_PSEUDO_CLASS));
    assertEquals("Text is missing", supportingText.getText());

    // change validity back
    node.setText("Is valid again");
    assertFalse(supportingText.isVisible());
    assertFalse(supportingText.isManaged());
    assertFalse(node.getPseudoClassStates().contains(INVALID_PSEUDO_CLASS));
  }

  @Getter
  private static class TestNode extends Label implements Validated {

    public TestNode(String text) {
      super(text);
    }

    private final MFXValidator validator = new MFXValidator();
  }

}