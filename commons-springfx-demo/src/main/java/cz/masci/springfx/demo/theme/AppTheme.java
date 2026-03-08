/*
 * Copyright (c) 2023
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

package cz.masci.springfx.demo.theme;

import io.github.palexdev.materialfx.theming.base.Theme;
import java.net.URL;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration of CSS themes available in the demo application.
 * Each constant references a stylesheet resource path.
 */
@RequiredArgsConstructor
public enum AppTheme implements Theme {

  /** Theme for table view styling. */
  TABLE_VIEW("css/table-view.css"),
  /** General application theme. */
  APP("css/app.css"),
  /** Design tokens theme. */
  TOKENS("css/tokens.css");

  /** The classpath-relative path to the CSS stylesheet. */
  private final String path;

  @Override
  public String path() {
    return path;
  }

  @Override
  public URL get() {
    return Optional.ofNullable(AppTheme.class.getResource(path)).orElseThrow();
  }

  @Override
  public String deployName() {
    return "app";
  }
}
