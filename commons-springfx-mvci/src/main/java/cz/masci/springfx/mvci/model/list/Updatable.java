/*
 * Copyright (c) 2024
 *
 * This file is part of DrD.
 *
 * DrD is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option)
 *   any later version.
 *
 * DrD is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *   FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 *    License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *  along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */

package cz.masci.springfx.mvci.model.list;

/**
 * <pre>
 *   Ability to update view. Mostly used to update element in list view in list-detail view pattern.
 *   First set {@code setOnUpdateElementsProperty} which will be used when calling {@code updateElementsProperty}.
 * </pre>
 *
 * {@code
 *   class UpdatableImpl implements Updatable<Model> {
 *     private Runnable onUpdateElementsProperty;
 *
 *     public void updateElementsProperty() {
 *       if (onUpdateElementsProperty != null) {
 *         onUpdateElementsProperty.run();
 *       }
 *     }
 *
 *     public void setOnUpdateElementsProperty(Runnable command) {
 *       onFocusView = command;
 *     }
 *   }
 * }
 */
public interface Updatable {
  void updateElementsProperty();
  void setOnUpdateElementsProperty(Runnable command);
}
