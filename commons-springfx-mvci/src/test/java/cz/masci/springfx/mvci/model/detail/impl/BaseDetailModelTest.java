/*
 * Copyright (c) 2024
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

package cz.masci.springfx.mvci.model.detail.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cz.masci.springfx.mvci.model.dirty.DirtyStringProperty;
import io.github.palexdev.materialfx.validation.Constraint;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.Test;

public class BaseDetailModelTest {

  private final static String INITIAL_TEXT = "initial text";
  private final static String UPDATED_TEXT = "updated text";
  private final static Integer ID = 1;

  private final TestDetailModel baseDetailModel = new TestDetailModel();

  @Test
  public void getId() {
    Integer id = baseDetailModel.getId();
    assertNull(id);
  }

  @Test
  public void setId() {
    baseDetailModel.setId(ID);
    Integer resultId = baseDetailModel.getId();
    assertEquals(ID, resultId);
  }

  @Test
  public void isDirty() {
    assertFalse(baseDetailModel.isDirty());
  }

  @Test
  public void isDirty_afterUpdate() {
    baseDetailModel.setText(UPDATED_TEXT);

    assertTrue(baseDetailModel.isDirty());
    assertEquals(UPDATED_TEXT, baseDetailModel.getText());
  }

  @Test
  public void rebaseline() {
    baseDetailModel.setText(UPDATED_TEXT);

    assertTrue(baseDetailModel.isDirty());
    baseDetailModel.rebaseline();
    assertFalse(baseDetailModel.isDirty());
    assertEquals(UPDATED_TEXT, baseDetailModel.getText());
  }

  @Test
  public void reset() {
    baseDetailModel.setText(UPDATED_TEXT);

    assertTrue(baseDetailModel.isDirty());
    baseDetailModel.reset();
    assertFalse(baseDetailModel.isDirty());
    assertEquals(INITIAL_TEXT, baseDetailModel.getText());
  }

  @Test
  public void valid() {
    baseDetailModel.setText("");

    assertTrue(baseDetailModel.isDirty());
    assertFalse(baseDetailModel.isValid());

    baseDetailModel.setText(UPDATED_TEXT);
    assertTrue(baseDetailModel.isValid());
  }

  // region TestDetailModel
  @Data
  @EqualsAndHashCode(callSuper = true)
  private static class TestDetailModel extends BaseDetailModel<Integer> {
    private Integer id;
    private DirtyStringProperty text = new DirtyStringProperty(INITIAL_TEXT);

    public TestDetailModel() {
      addComposites(text);
      addConstraints(Constraint.of("Text should be not empty", text.isNotEmpty()));
    }

    @Override
    public boolean isTransient() {
      return false;
    }

    public String getText() {
      return text.get();
    }

    public void setText(String text) {
      this.text.set(text);
    }
  }
  // endregion
}