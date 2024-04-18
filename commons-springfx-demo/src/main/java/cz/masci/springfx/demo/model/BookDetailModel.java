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

import cz.masci.springfx.mvci.util.constraint.ConstraintUtils;
import cz.masci.springfx.mvci.model.detail.DetailModel;
import cz.masci.springfx.mvci.model.dirty.DirtyStringProperty;
import io.github.palexdev.materialfx.validation.MFXValidator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Data;
import lombok.Getter;
import org.nield.dirtyfx.tracking.CompositeDirtyProperty;

@Data
public class BookDetailModel implements DetailModel<Long> {
  private ObjectProperty<Long> id = new SimpleObjectProperty<>();
  private DirtyStringProperty title = new DirtyStringProperty("");
  private DirtyStringProperty author = new DirtyStringProperty("");
  @Getter
  private final CompositeDirtyProperty composite = new CompositeDirtyProperty();
  @Getter
  private final MFXValidator validator = new MFXValidator();

  public BookDetailModel() {
    composite.addAll(title, author);
    validator.constraint(ConstraintUtils.isNotEmpty(title, "Title"));
  }

  @Override
  public boolean isTransient() {
    return false;
  }

  // region setters and getters

  public Long getId() {
    return id.get();
  }

  public void setId(Long id) {
    this.id.set(id);
  }

  public String getTitle() {
    return title.get();
  }

  public DirtyStringProperty titleProperty() {
    return title;
  }

  public void setTitle(String title) {
    this.title.set(title);
  }

  public String getAuthor() {
    return author.get();
  }

  public DirtyStringProperty authorProperty() {
    return author;
  }

  public void setAuthor(String author) {
    this.author.set(author);
  }
  // endregion
}
