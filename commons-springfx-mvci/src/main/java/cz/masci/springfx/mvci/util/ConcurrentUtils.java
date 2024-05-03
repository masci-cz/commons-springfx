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

package cz.masci.springfx.mvci.util;

import javafx.application.Platform;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ConcurrentUtils {

  /**
   * Runs the given GUI-related code in the JavaFX Application Thread.
   *
   * @param guiStuff the runnable representing the GUI-related code to be executed
   */
  public static void runInFXThread(Runnable guiStuff) {
    Platform.runLater(guiStuff);
  }
}
