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
 *   Ability to focus view. Mostly used to focus detail view in list-detail view pattern.
 *   First set {@code setOnFocusView} which will be used when calling {@code focusView}.
 * </pre>
 *
 * <pre>
 *   class FocusableImpl implements Focusable {
 *     private Runnable onFocusView;
 *
 *     public void focusView() {
 *       if (onFocusView != null) {
 *         onFocusView.run();
 *       }
 *     }
 *
 *     public void setOnFocusView(Runnable command) {
 *       onFocusView = command;
 *     }
 *   }
 * </pre>
 */
public interface Focusable {
  /** Runs predefined runnable. */
  void focusView();
  /** Sets runnable command which should be executed in {@code focusView}. */
  void setOnFocusView(Runnable command);
}
