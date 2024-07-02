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

package cz.masci.springfx.demo.model;

import cz.masci.springfx.mvci.model.detail.impl.BaseDetailModel;
import cz.masci.springfx.mvci.util.constraint.ConstraintUtils;
import lombok.EqualsAndHashCode;
import org.nield.dirtyfx.beans.DirtyStringProperty;

@EqualsAndHashCode(callSuper=true)
public class LOTRDetailModel extends BaseDetailModel<Long> {

  private final DirtyStringProperty character = new DirtyStringProperty("");
  private final DirtyStringProperty location = new DirtyStringProperty("");

  public LOTRDetailModel() {
    addComposites(character, location);
    addConstraints(ConstraintUtils.isNotEmpty(character, "Character"));
  }

  // region setters and getters
  public String getCharacter() {
    return character.get();
  }

  public DirtyStringProperty characterProperty() {
    return character;
  }

  public void setCharacter(String character) {
    this.character.set(character);
  }

  public String getLocation() {
    return location.get();
  }

  public DirtyStringProperty locationProperty() {
    return location;
  }

  public void setLocation(String location) {
    this.location.set(location);
  }
  // endregion
}
