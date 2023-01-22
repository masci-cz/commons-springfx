package cz.masci.commons.springfx.controller;

import static cz.masci.commons.springfx.controller.TestFxUtils.clickOnDialogButton;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;

import cz.masci.commons.springfx.data.Modifiable;
import cz.masci.commons.springfx.exception.CrudException;
import cz.masci.commons.springfx.service.CrudService;
import cz.masci.commons.springfx.service.EditDialogControllerService;
import cz.masci.commons.springfx.service.ObservableListMap;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.SimpleFxControllerAndView;
import org.junit.jupiter.api.Disabled;
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
  private ObservableListMap observableListMap;
  private ObservableList<ItemOne> observableList;

  private MasterController masterController;

  /**
   * Will be called with {@code @Before} semantics, i.e. before each test method.
   *
   * @param stage - Will be injected by the test runner.
   */
  @Start
  private void start(Stage stage) throws IOException {
    masterController = new MasterController(fxWeaver, itemService);
    var fxmlLoader = new FXMLLoader();
    fxmlLoader.setController(masterController);
    BorderPane pane = fxmlLoader.load(Objects.requireNonNull(getClass().getResourceAsStream("fxml/master-view.fxml")));
    stage.setScene(new Scene(pane, 100, 100));
    stage.show();
  }

  @Stop
  private void stop() {
  }

  @Test
  void onNewItem(FxRobot robot) throws CrudException {
    var newItem = new ItemOne("test");
    var controller = new EditDialogController(newItem);
    var view = new EditDialogView();

    when(fxWeaver.load(EditDialogController.class)).thenReturn(SimpleFxControllerAndView.of(controller, view));
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
    var controller = new EditDialogController(null);
    var view = new EditDialogView();

    when(fxWeaver.load(EditDialogController.class)).thenReturn(SimpleFxControllerAndView.of(controller, view));

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
    observableList = FXCollections.observableArrayList();
    observableList.add(savedItem);

    when(itemService.save(savedItem)).thenReturn(savedItem);

    masterController.setObservableList(observableList);

    // when
    robot.clickOn("#saveAll");
    clickOnDialogButton(robot, ".alert .button-bar .button");

    // then
    assertTrue(observableList.isEmpty());
  }

  @Test
  void onSaveAll_nullObservableList(FxRobot robot) throws CrudException {
    // when
    robot.clickOn("#saveAll");

    // then
    verify(itemService, never()).save(any());
  }

  @Disabled
  @Test
  void onDelete() {
  }

  @Disabled
  @Test
  void setObservableListMap() {
  }

  @Disabled
  @Test
  void setObservableList() {
  }

  @Disabled
  @Test
  void initialize() {
  }

  @Disabled
  @Test
  void addColumns() {
  }

  @Disabled
  @Test
  void setDetailController() {
  }

  @Disabled
  @Test
  void setRowFactory() {
  }

  @Disabled
  @Test
  void init() {
  }

  private static class MasterController extends AbstractMasterController<ItemOne> {

    public MasterController(FxWeaver fxWeaver, CrudService<ItemOne> itemService) {
      super(fxWeaver, itemService, "test", EditDialogController.class);
    }

    @Override
    protected void init() {

    }
  }

  private static class EditDialogController implements EditDialogControllerService<ItemOne> {

    ItemOne value;

    public EditDialogController(ItemOne value) {
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

  @Data
  @AllArgsConstructor
  private static class ItemOne implements Modifiable {
    private String message;
  }
}