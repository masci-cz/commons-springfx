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

import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Validated;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import javafx.beans.value.ChangeListener;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BuilderUtils {

  /**
   * Pseudo class added to invalid node.
   */
  public static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");
  /**
   * Implicit style class of the validation supported text
   */
  public static final String ERROR_STYLE_CLASS = "error";

  /**
   * Creates region with node and supporting text which appears when the node is invalid. When the node is invalid the supporting text appears with the text
   * from the {@link Constraint}. The supporting text disappears when the node is valid again. The constrain is validated based on the change coming from
   * revalidateFlagListener.
   *
   * @param validatedNode Node with validator to be validated
   * @param revalidateFlagListener Boolean property listener. Whenever value changes from <code>false</code> to <code>true</code> the node is revalidated.
   * @param inputConstraints Constraints to check
   * @param <T> Type of Node to validate extends {@link Node} and implements {@link Validated}
   * @return Region with the Node and supported text
   */
  public <T extends Node & Validated> Region enhanceValidatedNodeWithSupportingText(T validatedNode,
                                                                                    Consumer<ChangeListener<? super Boolean>> revalidateFlagListener,
                                                                                    Constraint... inputConstraints) {
    return enhanceValidatedNodeWithSupportingText(validatedNode, createValidationSupportingText(), revalidateFlagListener, inputConstraints);
  }

  /**
   * Creates a region with node and supporting text which appears when the node is invalid. When the node is invalid the supporting text appears with the text
   * from the {@link Constraint}. The supporting text disappears when the node is valid again. The constraint is validated based on the change coming from
   * revalidateFlagListener.
   *
   * @param validatedNode Node with validator to be validated
   * @param revalidateFlagListener Boolean property listener. Whenever value changes from <code>false</code> to <code>true</code> the node is revalidated.
   * @param inputConstraints Constraints to check
   * @param <T> Type of Node to validate extends {@link Node} and implements {@link Validated}
   * @return Region with the Node and supported text
   */
  public <T extends Node & Validated> Region enhanceValidatedNodeWithSupportingText(T validatedNode, Label supportingText, Consumer<ChangeListener<?
      super Boolean>> revalidateFlagListener, Constraint... inputConstraints) {
    Arrays.stream(inputConstraints)
          .forEach(validatedNode.getValidator()::constraint);
    validatedNode.getValidator()
                 .validProperty()
                 .addListener((observable, oldValue, newValue) -> {
                   if (newValue) {
                     supportingText.setVisible(false);
                     supportingText.setManaged(false); // disable
                     validatedNode.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, false);
                   }
                 });
    revalidateFlagListener.accept((observable, oldValue, newValue) -> {
      if (!oldValue && newValue) {
        List<Constraint> constraints = validatedNode.validate();
        if (!constraints.isEmpty()) {
          validatedNode.pseudoClassStateChanged(INVALID_PSEUDO_CLASS, true);
          supportingText.setText(constraints.get(0)
                                            .getMessage());
          supportingText.setVisible(true);
          supportingText.setManaged(true);
        }
      }
    });
    var result = new VBox(validatedNode, supportingText);
    result.setAlignment(Pos.TOP_LEFT);

    return result;
  }

  /**
   * Creates validation supporting text label with set style class <b>error</b>. Set the label as not managed to not layout the node. Visibility and managing is
   * updated when validating supporting input.
   *
   * @return Created label
   */
  public Label createValidationSupportingText() {
    var result = new Label();
    result.getStyleClass()
          .add(ERROR_STYLE_CLASS);
    result.setPadding(new Insets(4.0, 16.0, 0.0, 16.0));
    result.setVisible(false);
    result.setManaged(false);

    return result;
  }

  public static Builder<Region> createDetailWithCommandViewBuilder(Region detailView, Region commandView) {
    return () -> {
      VBox.setVgrow(detailView, Priority.ALWAYS);
      return new VBox(detailView, commandView);
    };
  }
}
