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

import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.scene.layout.Region;
import javafx.util.Builder;
import org.junit.jupiter.api.Test;

public class SimpleControllerTest {

  @Test
  void getView() {
    Region expectedRegion = new Region();
    TestBuilder testBuilder = new TestBuilder(expectedRegion);

    SimpleController<Region, TestBuilder> simpleController = new SimpleController<>(testBuilder);

    var result = simpleController.getView();

    assertEquals(expectedRegion, result);
  }

  private record TestBuilder(Region region) implements Builder<Region> {

    @Override
    public Region build() {
      return region;
    }
  }
}