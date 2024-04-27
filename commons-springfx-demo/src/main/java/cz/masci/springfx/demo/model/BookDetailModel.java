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
import cz.masci.springfx.mvci.model.dirty.DirtyStringProperty;
import cz.masci.springfx.mvci.util.constraint.ConstraintUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class BookDetailModel extends BaseDetailModel<Long> {
  private DirtyStringProperty title = new DirtyStringProperty("");
  private DirtyStringProperty author = new DirtyStringProperty("");

  public BookDetailModel() {
    composite.addAll(title, author);
    validator.constraint(ConstraintUtils.isNotEmpty(title, "Title"));
  }

  @Override
  public boolean isTransient() {
    return getId() == null;
  }

  // region setters and getters
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
