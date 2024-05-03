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

package cz.masci.springfx.demo;

import cz.masci.springfx.demo.controller.MainController;
import cz.masci.springfx.demo.theme.AppTheme;
import cz.masci.springfx.mvci.controller.ViewProvider;
import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaFXApp extends Application {

  private ConfigurableApplicationContext applicationContext;

  @Override
  public void init() {
    String[] args = getParameters().getRaw()
                                   .toArray(new String[0]);

    this.applicationContext = new SpringApplicationBuilder().sources(SpringFXApp.class)
                                                            .run(args);

    UserAgentBuilder.builder()
                    .themes(JavaFXThemes.MODENA)
                    .themes(MaterialFXStylesheets.forAssemble(false))
                    .themes(AppTheme.TABLE_VIEW)
                    .setDeploy(true)
                    .setDebug(true)
                    .setResolveAssets(true)
                    .build()
                    .setGlobal();
  }

  @Override
  public void start(Stage primaryStage) {
    ViewProvider<Region> viewProvider = applicationContext.getBean(MainController.class);
    Scene scene = new Scene(viewProvider.getView(), 640, 480);
    primaryStage.setScene(scene);
    primaryStage.setTitle("SpringFX MVCI Demo");
    primaryStage.show();
  }

  @Override
  public void stop() {
    this.applicationContext.close();
    Platform.exit();
  }
}
