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

package cz.masci.springfx.mvci.view.builder;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(ApplicationExtension.class)
class CommandsViewBuilderTest {

  @Test
  void build_hbox() {
    Button expectedButton = new Button();

    var result = new CommandsViewBuilder(List.of(expectedButton)).build();

    assertTrue(result.getChildrenUnmodifiable().contains(expectedButton));
  }

  @Test
  void build_vbox() {
    Button expectedButton = new Button();

    var result = new CommandsViewBuilder(List.of(expectedButton), true, Pos.CENTER).build();

    assertTrue(result.getChildrenUnmodifiable().contains(expectedButton));
  }
}