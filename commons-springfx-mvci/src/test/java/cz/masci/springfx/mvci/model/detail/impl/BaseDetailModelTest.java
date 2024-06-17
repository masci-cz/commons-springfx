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

import static cz.masci.springfx.mvci.TestConstants.DETAIL_MODEL_ID;
import static cz.masci.springfx.mvci.TestConstants.INITIAL_TEXT;
import static cz.masci.springfx.mvci.TestConstants.UPDATED_TEXT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import cz.masci.springfx.mvci.TestUtils.TestDetailModel;
import org.junit.jupiter.api.Test;

public class BaseDetailModelTest {

  private final TestDetailModel baseDetailModel = new TestDetailModel();

  @Test
  public void getId() {
    Integer id = baseDetailModel.getId();
    assertNull(id);
  }

  @Test
  public void setId() {
    baseDetailModel.setId(DETAIL_MODEL_ID);
    Integer resultId = baseDetailModel.getId();
    assertEquals(DETAIL_MODEL_ID, resultId);
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

  // endregion
}