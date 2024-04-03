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

import static org.junit.jupiter.api.Assertions.*;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

@ExtendWith(ApplicationExtension.class)
class BorderPaneBuilderTest {

  @Test
  void build_center() {
    Pane center = new Pane();

    var borderPane = BorderPaneBuilder.builder().withCenter(center).build();

    assertAll("Border Pane",
        () -> assertNull(borderPane.getLeft()),
        () -> assertNull(borderPane.getRight()),
        () -> assertEquals(center, borderPane.getCenter()),
        () -> assertNull(borderPane.getTop()),
        () -> assertNull(borderPane.getBottom())
    );
  }

  @Test
  void build_all() {
    var borderPane = BorderPaneBuilder.builder()
                                  .withLeft(new Pane())
                                  .withRight(new Pane())
                                  .withCenter(new Pane())
                                  .withTop(new Pane())
                                  .withBottom(new Pane())
                                  .build();

    assertAll("Border Pane",
        () -> assertNotNull(borderPane.getLeft()),
        () -> assertNotNull(borderPane.getRight()),
        () -> assertNotNull(borderPane.getCenter()),
        () -> assertNotNull(borderPane.getTop()),
        () -> assertNotNull(borderPane.getBottom())
    );

  }
}