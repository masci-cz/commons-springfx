package cz.masci.commons.springfx.controller;

import static cz.masci.commons.springfx.TestFxUtils.clickOnDialogButton;
import static cz.masci.commons.springfx.TestFxUtils.clickOnDialogButtonWithText;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
class AbstractMasterControllerTest {

  @Mock
  private FxWeaver fxWeaver;
  @Mock
  private CrudService<ItemOne> itemService;
  private final ObservableList<ItemOne> changedItemList = FXCollections.observableArrayList();

  private TestMasterController masterController;

  /**
   * Will be called with {@code @Before} semantics, i.e. before each test method.
   *
   * @param stage - Will be injected by the test runner.
   */
  @Start
  private void start(Stage stage) throws IOException {
    masterController = new TestMasterController();
    var fxmlLoader = new FXMLLoader();
    fxmlLoader.setController(masterController);
    BorderPane pane = fxmlLoader.load(Objects.requireNonNull(getClass().getResourceAsStream("fxml/master-view.fxml")));
    stage.setScene(new Scene(pane, 640, 400));
    stage.show();
  }

  @Stop
  private void stop() {
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
        (TableView<ItemOne> tableView) -> {
          ItemOne item = tableView.getSelectionModel().getSelectedItem();
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
    verifyThat("#tableView", (TableView<ItemOne> tableView) ->
      tableView.getItems().isEmpty()
    );

    verify(itemService, never()).save(any());
  }

  @Test
  void onSaveAll(FxRobot robot) throws CrudException {
    var savedItem = new ItemOne("test");
    changedItemList.add(savedItem);

    when(itemService.save(savedItem)).thenReturn(savedItem);

    // when
    robot.clickOn("#saveAll");
    clickOnDialogButton(robot, ".alert .button-bar .button");

    // then
    assertThat(changedItemList)
        .doesNotContain(savedItem);
  }

  @Test
  void onSaveAll_cancel(FxRobot robot) throws CrudException {
    var savedItem = new ItemOne("test");
    changedItemList.add(savedItem);

    // when
    robot.clickOn("#saveAll");
    clickOnDialogButtonWithText(robot, "Cancel");

    // then
    assertThat(changedItemList)
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
    changedItemList.add(itemToDelete);

    doNothing().when(itemService).delete(itemToDelete);

    // when
    TableView<ItemOne> tableView = robot.lookup("#tableView").queryTableView();
    tableView.getSelectionModel().select(itemToDelete);
    robot.clickOn("#delete");
    clickOnDialogButton(robot, ".alert .button-bar .button");

    // then
    assertThat(changedItemList)
        .doesNotContain(itemToDelete);
  }

  @Test
  void onDelete_cancel(FxRobot robot) throws CrudException {
    var itemToDelete = new ItemOne("test");
    changedItemList.add(itemToDelete);

    // when
    TableView<ItemOne> tableView = robot.lookup("#tableView").queryTableView();
    tableView.getSelectionModel().select(itemToDelete);
    robot.clickOn("#delete");
    clickOnDialogButtonWithText(robot, "Cancel");

    // then
    assertThat(changedItemList)
        .contains(itemToDelete);
    verify(itemService, never()).delete(any());
  }

  @Test
  void initialize(FxRobot robot) throws CrudException {
    when(itemService.list()).thenReturn(List.of(new ItemOne("test")));

    masterController.initialize();

    TableView<ItemOne> tableView = robot.lookup("#tableView").queryTableView();
    assertEquals(1, tableView.getItems().size());
  }

  @Test
  void initialize_error(FxRobot robot) throws CrudException {
    when(itemService.list()).thenThrow(new CrudException("Error"));

    masterController.initialize();

    TableView<ItemOne> tableView = robot.lookup("#tableView").queryTableView();
    assertEquals(0, tableView.getItems().size());
  }

  @Test
  void addColumns(FxRobot robot) {
    Platform.runLater(() -> {
      masterController.addColumns(new TableColumn<>("First column"));

      TableView<ItemOne> tableView = robot.lookup("#tableView").queryTableView();
      assertEquals(1, tableView.getColumns().size());
    });
  }

  @Test
  void setDetailController(FxRobot robot) {
    var itemToSelect = new ItemOne("test");
    AbstractDetailController<ItemOne> controller = mock(AbstractDetailController.class);
    Node view = new Pane();

    when(fxWeaver.load(AbstractDetailController.class)).thenReturn(SimpleFxControllerAndView.of(controller, view));

    Platform.runLater(() -> {
      masterController.setDetailController(AbstractDetailController.class);

      // check the listener is set
      TableView<ItemOne> tableView = robot.lookup("#tableView").queryTableView();
      tableView.getSelectionModel().select(itemToSelect);

      verify(controller).setItem(itemToSelect);
    });
  }

  @Test
  void setRowFactory(FxRobot robot) {
    masterController.setRowFactory("TEST");

    TableView<ItemOne> tableView = robot.lookup("#tableView").queryTableView();
    assertNotNull(tableView.getRowFactory());
  }

  private class TestMasterController extends AbstractMasterController<ItemOne> {

    public TestMasterController() {
      super(fxWeaver, itemService, "test", TestEditDialogController.class, changedItemList);
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