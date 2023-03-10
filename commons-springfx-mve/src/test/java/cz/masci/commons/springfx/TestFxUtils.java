package cz.masci.commons.springfx;

import static org.testfx.util.NodeQueryUtils.hasText;

import javafx.scene.control.Button;
import org.testfx.api.FxRobot;
import org.testfx.util.WaitForAsyncUtils;

public class TestFxUtils {

  public static void clickOnDialogButton(FxRobot robot, String query) {
    Button button = robot.lookup(query).queryButton();
    WaitForAsyncUtils.asyncFx(button::fire);
    WaitForAsyncUtils.waitForFxEvents();
  }

  public static void clickOnDialogButtonWithText(FxRobot robot, String withText) {
    Button button = robot.lookup(hasText(withText)).queryButton();
    WaitForAsyncUtils.asyncFx(button::fire);
    WaitForAsyncUtils.waitForFxEvents();
  }
}
