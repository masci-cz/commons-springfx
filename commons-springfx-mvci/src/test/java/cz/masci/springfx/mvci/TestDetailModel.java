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

package cz.masci.springfx.mvci;

import static cz.masci.springfx.mvci.TestConstants.INITIAL_TEXT;

import cz.masci.springfx.mvci.model.detail.impl.BaseDetailModel;
import cz.masci.springfx.mvci.model.dirty.DirtyStringProperty;
import io.github.palexdev.materialfx.validation.Constraint;

public class TestDetailModel extends BaseDetailModel<Integer> {
  private final DirtyStringProperty text = new DirtyStringProperty(INITIAL_TEXT);

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

  public DirtyStringProperty textProperty() {
    return text;
  }

  public void setText(String text) {
    this.text.set(text);
  }
}
