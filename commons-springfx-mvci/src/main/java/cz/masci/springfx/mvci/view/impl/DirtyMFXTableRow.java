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

package cz.masci.springfx.mvci.view.impl;

import cz.masci.springfx.mvci.view.DirtyStyleable;
import io.github.palexdev.materialfx.controls.MFXTableRow;
import io.github.palexdev.materialfx.controls.MFXTableView;
import org.nield.dirtyfx.tracking.DirtyProperty;
import org.reactfx.value.Val;

/**
 * MFX table row with dirty style implementation.
 *
 * @param <T> Type of row data
 */
public class DirtyMFXTableRow<T extends DirtyProperty> extends MFXTableRow<T> implements DirtyStyleable<T> {

  public DirtyMFXTableRow(MFXTableView<T> tableView, T data, String dirtyRowStyleClass) {
    super(tableView, data);
    var itemProperty = Val.wrap(dataProperty());
    initDirtyPropertyChangeListener(itemProperty, dirtyRowStyleClass);
  }

}