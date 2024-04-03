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

package cz.masci.springfx.mvci.controller.impl;

import cz.masci.springfx.mvci.controller.ViewProvider;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;

/**
 * Simple controller implementation providing view through the view builder provided in the constructor.
 *
 * <pre>{@code
 *   public MyBuilder implements Builder<Region> {
 *     Region build() {
 *       return new VBox();
 *     }
 *   }
 *
 *   public MyController extends SimpleController<Region, MyBuilder> {
 *     public MyController() {
 *       super(new MyBuilder());
 *     }
 *   }
 * }</pre>
 *
 * @param <T> Type of the view returned by this controller
 * @param <U> Type of the view builder
 */
@RequiredArgsConstructor
public class SimpleController<T extends Region, U extends Builder<T>> implements ViewProvider<T> {

  protected final U viewBuilder;

  @Override
  public T getView() {
    return viewBuilder.build();
  }
}
