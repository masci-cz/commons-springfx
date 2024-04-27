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

import cz.masci.springfx.mvci.model.detail.DetailModel;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.MFXValidator;
import java.util.Arrays;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import org.nield.dirtyfx.tracking.CompositeDirtyProperty;
import org.nield.dirtyfx.tracking.DirtyProperty;

public abstract class BaseDetailModel<T> implements DetailModel<T> {
  private final ObjectProperty<T> id = new SimpleObjectProperty<>();
  @Getter
  private final CompositeDirtyProperty composite = new CompositeDirtyProperty();
  @Getter
  private final MFXValidator validator = new MFXValidator();

  public T getId() {
    return id.get();
  }

  public void setId(T id) {
    this.id.set(id);
  }

  protected void addComposites(DirtyProperty ...properties) {
    composite.addAll(properties);
  }

  protected void addConstraints(Constraint ...constraints) {
    Arrays.stream(constraints).forEach(validator::constraint);
  }
}
