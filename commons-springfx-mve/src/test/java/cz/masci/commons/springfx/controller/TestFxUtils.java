package cz.masci.commons.springfx.controller;

import javafx.scene.control.Button;
import org.testfx.api.FxRobot;
import org.testfx.util.WaitForAsyncUtils;

public class TestFxUtils {

  public static void clickOnDialogButton(FxRobot robot, String query) {
    Button button = robot.lookup(query).query();
    WaitForAsyncUtils.asyncFx(button::fire);
    WaitForAsyncUtils.waitForFxEvents();

  }
}
