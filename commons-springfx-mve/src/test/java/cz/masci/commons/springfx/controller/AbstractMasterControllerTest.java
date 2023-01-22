package cz.masci.commons.springfx.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testfx.api.FxAssert.verifyThat;
import static org.junit.jupiter.api.Assertions.*;

import cz.masci.commons.springfx.data.Modifiable;
import cz.masci.commons.springfx.exception.CrudException;
import cz.masci.commons.springfx.service.CrudService;
import cz.masci.commons.springfx.service.EditDialogControllerService;
import cz.masci.commons.springfx.service.ObservableListMap;
import java.io.IOException;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.SimpleFxControllerAndView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.service.query.NodeQuery;
import org.testfx.util.NodeQueryUtils;

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

  @Test
  void onNewItem(FxRobot robot) throws CrudException {
    var selectedItem = new ItemOne("message");
    var controller = new EditDialogController(selectedItem);
    var view = new EditDialogView();

    when(fxWeaver.load(EditDialogController.class)).thenReturn(SimpleFxControllerAndView.of(controller, view));
    when(itemService.save(any())).thenReturn(selectedItem);

    // when
    robot.clickOn("#newItem");
    robot.clickOn(".dialog-pane .button-bar .button");

    // then
    verifyThat("#tableView", (TableView<ItemOne> tableView) -> {
      System.out.println("Verifying #tableView " + tableView);
      Object item = tableView.getSelectionModel().getSelectedItem();
      return Objects.equals(item, selectedItem);
    });
  }

  @Test
  void onNewItem_null(FxRobot robot) throws CrudException {
    var controller = new EditDialogController(null);
    var view = new EditDialogView();

    when(fxWeaver.load(EditDialogController.class)).thenReturn(SimpleFxControllerAndView.of(controller, view));

    // when
    robot.clickOn("#newItem");
    robot.clickOn(".dialog-pane .button-bar .button");

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
    robot.clickOn(".alert .button-bar .button");

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

  @Test
  void onDelete() {
  }

  @Test
  void setObservableListMap() {
  }

  @Test
  void setObservableList() {
  }

  @Test
  void initialize() {
  }

  @Test
  void addColumns() {
  }

  @Test
  void setDetailController() {
  }

  @Test
  void setRowFactory() {
  }

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
      return buttonType -> value;
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