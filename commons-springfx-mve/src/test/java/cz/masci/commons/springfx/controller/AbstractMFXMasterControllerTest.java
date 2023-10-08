package cz.masci.commons.springfx.controller;

import static cz.masci.commons.springfx.TestFxUtils.clickOnDialogButton;
import static cz.masci.commons.springfx.TestFxUtils.clickOnDialogButtonWithText;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;

import cz.masci.commons.springfx.ItemOne;
import cz.masci.commons.springfx.exception.CrudException;
import cz.masci.commons.springfx.service.CrudService;
import cz.masci.commons.springfx.service.EditDialogControllerService;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.SimpleFxControllerAndView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.framework.junit5.Stop;

@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
class AbstractMFXMasterControllerTest {

  @Mock
  private FxWeaver fxWeaver;
  @Mock
  private CrudService<ItemOne> itemService;

  private TestMFXMasterController masterController;

  /**
   * Will be called with {@code @Before} semantics, i.e. before each test method.
   *
   * @param stage - Will be injected by the test runner.
   */
  @Start
  private void start(Stage stage) throws IOException {
    masterController = new TestMFXMasterController();
    var fxmlLoader = new FXMLLoader();
    fxmlLoader.setController(masterController);
    BorderPane pane = fxmlLoader.load(Objects.requireNonNull(getClass().getResourceAsStream("fxml/mfx-master-view.fxml")));
    stage.setScene(new Scene(pane, 640, 400));
    stage.show();
  }

  @Stop
  private void stop() {
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  void onNewItem(FxRobot robot) throws CrudException {
    var newItem = new ItemOne("test");
    var controller = new TestEditDialogController(newItem);
    var view = new EditDialogView();

    when(fxWeaver.load(TestEditDialogController.class)).thenReturn(SimpleFxControllerAndView.of(controller, view));
    when(itemService.save(any())).thenReturn(newItem);

    // when
    robot.clickOn("#newItem");
    clickOnDialogButton(robot, ".dialog-pane .button-bar .button");

    // then
    AtomicReference<ItemOne> selectedItem = new AtomicReference<>();
    verifyThat("#tableView",
        (MFXTableView<ItemOne> tableView) -> {
          ItemOne item = tableView.getSelectionModel().getSelectedValue();
          selectedItem.set(item);
          return Objects.equals(newItem, item);
        },
        sb -> sb.append("\nSelected item: ").append(selectedItem.get())
    );
  }

  @Test
  void onNewItem_null(FxRobot robot) throws CrudException {
    var controller = new TestEditDialogController(null);
    var view = new EditDialogView();

    when(fxWeaver.load(TestEditDialogController.class)).thenReturn(SimpleFxControllerAndView.of(controller, view));

    // when
    robot.clickOn("#newItem");
    clickOnDialogButton(robot, ".dialog-pane .button-bar .button");

    // then
    verifyThat("#tableView", (MFXTableView<ItemOne> tableView) ->
      tableView.getItems().isEmpty()
    );

    verify(itemService, never()).save(any());
  }

  @Test
  void onSaveAll(FxRobot robot) throws CrudException {
    var savedItem = new ItemOne("test");
    masterController.getChangedItemList().add(savedItem);

    when(itemService.save(savedItem)).thenReturn(savedItem);

    // when
    robot.clickOn("#saveAll");
    clickOnDialogButtonWithText(robot, "OK");

    // then
    assertThat(masterController.getChangedItemList())
        .doesNotContain(savedItem);
  }

  @Test
  void onSaveAll_cancel(FxRobot robot) throws CrudException {
    var savedItem = new ItemOne("test");
    masterController.getChangedItemList().add(savedItem);

    // when
    robot.clickOn("#saveAll");
    clickOnDialogButtonWithText(robot, "Cancel");

    // then
    assertThat(masterController.getChangedItemList())
        .contains(savedItem);
    verify(itemService, never()).save(any());
  }

  @Test
  void onSaveAll_nullObservableList(FxRobot robot) throws CrudException {
    // when
    robot.clickOn("#saveAll");

    // then
    verify(itemService, never()).save(any());
  }

  @Test
  void onDelete(FxRobot robot) throws CrudException {
    var itemToDelete = new ItemOne("test");
    masterController.getChangedItemList().add(itemToDelete);

    doNothing().when(itemService).delete(itemToDelete);

    // when
    var tableView = queryTableView(robot);
    tableView.getSelectionModel().selectItem(itemToDelete);
    robot.clickOn("#delete");
    clickOnDialogButtonWithText(robot, "OK");

    // then
    assertThat(masterController.getChangedItemList())
        .doesNotContain(itemToDelete);
  }

  @Test
  void onDelete_cancel(FxRobot robot) throws CrudException {
    var itemToDelete = new ItemOne("test");
    masterController.getChangedItemList().add(itemToDelete);

    // when
    var tableView = queryTableView(robot);
    tableView.getSelectionModel().selectItem(itemToDelete);
    robot.clickOn("#delete");
    clickOnDialogButtonWithText(robot, "Cancel");

    // then
    assertThat(masterController.getChangedItemList())
        .contains(itemToDelete);
    verify(itemService, never()).delete(any());
  }

  @Test
  void initialize(FxRobot robot) throws CrudException {
    when(itemService.list()).thenReturn(List.of(new ItemOne("test")));

    Platform.runLater(() -> {
      masterController.initialize();

      var tableView = queryTableView(robot);
      assertEquals(1, tableView.getItems().size());
    });
  }

  @Test
  void initialize_error(FxRobot robot) throws CrudException {
    when(itemService.list()).thenThrow(new CrudException("Error"));

    masterController.initialize();

    MFXTableView<ItemOne> tableView = queryTableView(robot);
    assertEquals(0, tableView.getItems().size());
  }

  @Test
  void addColumns(FxRobot robot) {
    Platform.runLater(() -> {
      masterController.addColumns(new MFXTableColumn<>("First column"));

      MFXTableView<ItemOne> tableView = queryTableView(robot);
      assertEquals(1, tableView.getTableColumns().size());
    });
  }

  @Test
  void setDetailController(FxRobot robot) {
    var itemToSelect = new ItemOne("test");
    //noinspection unchecked
    AbstractDetailController<ItemOne> controller = mock(AbstractDetailController.class);
    Node view = new Pane();

    when(fxWeaver.load(AbstractDetailController.class)).thenReturn(SimpleFxControllerAndView.of(controller, view));

    Platform.runLater(() -> {
      //noinspection unchecked
      masterController.setDetailController(AbstractDetailController.class);

      // check the listener is set
      MFXTableView<ItemOne> tableView = queryTableView(robot);
      tableView.getSelectionModel().selectItem(itemToSelect);

      verify(controller).setItem(itemToSelect);
    });
  }

  @Test
  void setRowFactory(FxRobot robot) {
    Platform.runLater(() -> {
      masterController.setRowFactory("TEST");

      MFXTableView<ItemOne> tableView = queryTableView(robot);
      assertNotNull(tableView.getTableRowFactory());
    });
  }

  @SuppressWarnings("unchecked")
  private static <T> MFXTableView<T> queryTableView(FxRobot robot) {
    var tableView = robot.lookup("#tableView").tryQueryAs(MFXTableView.class);
    if (tableView.isEmpty()) {
      fail("#tableView not found");
    }
    return (MFXTableView<T>) tableView.get();
  }

  private class TestMFXMasterController extends AbstractMFXMasterController<ItemOne> {

    public TestMFXMasterController() {
      super(fxWeaver, itemService, TestEditDialogController.class);
    }

    @Override
    protected void init() {

    }
  }

  private static class TestEditDialogController implements EditDialogControllerService<ItemOne> {

    ItemOne value;

    public TestEditDialogController(ItemOne value) {
      this.value = value;
    }
    @Override
    public Callback<ButtonType, ItemOne> getResultConverter() {
      return buttonType -> {
        System.out.println("getResultConverter returning: " + value);
        return value;
      };
    }
  }

  private static class EditDialogView extends DialogPane {

    public EditDialogView() {
      super();
      getButtonTypes().add(ButtonType.OK);
    }
  }
}