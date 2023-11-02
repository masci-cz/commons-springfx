package cz.masci.commons.springfx.controller;

import cz.masci.commons.springfx.data.Modifiable;
import cz.masci.commons.springfx.exception.CrudException;
import cz.masci.commons.springfx.service.CrudService;
import cz.masci.commons.springfx.service.EditDialogControllerService;
import cz.masci.commons.springfx.utility.MFXStyleChangingRowFactory;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;

/**
 * Abstract controller for master-detail view.<br>
 * Displays table view with items loaded by the item service.<br>
 * Displays three buttons for actions:
 * <ul>
 *   <li>new item</li>
 *   <li>save all</li>
 *   <li>delete</li>
 * </ul>
 *
 * <p>
 *   Annotate subclass with {@link cz.masci.commons.springfx.fxml.annotation.FxmlController} to let loader set the right controller class.
 * </p>
 *
 * @author Daniel Masek
 *
 * @param <T> Item type
 */
@Slf4j
@FxmlView("fxml/mfx-master-view.fxml")
public abstract class AbstractMFXMasterController<T extends Modifiable> {

  /**
   * FxWeaver instance
   */
  private final FxWeaver fxWeaver;
  /**
   * Item service responsible for CRUD operations
   */
  private final CrudService<T> itemService;
  /**
   * Edit controller class
   */
  private final Class<? extends EditDialogControllerService<T>> editControllerClass;
  /**
   * Changed item list
   */
  @Getter
  private final ObservableList<T> changedItemList;

  @FXML
  protected BorderPane borderPane;

  @FXML
  protected MFXTableView<T> tableView;

  @FXML
  protected VBox items;

  public AbstractMFXMasterController(FxWeaver fxWeaver, CrudService<T> itemService, Class<? extends EditDialogControllerService<T>> editControllerClass) {
    this.fxWeaver = fxWeaver;
    this.itemService = itemService;
    this.editControllerClass = editControllerClass;
    changedItemList = FXCollections.observableArrayList();
  }

  /**
   * Open edit dialog and save new item defined in edit controller.
   *
   * @param event Action event - is not used
   */
  @FXML
  public void onNewItem(ActionEvent event) {
    log.debug("New item action occurred");

    FxControllerAndView<? extends EditDialogControllerService<T>, DialogPane> editor = fxWeaver.load(editControllerClass);
    Dialog<T> dialog = new Dialog<>();
    dialog.setTitle("New Item");
    editor.getView().ifPresent(dialog::setDialogPane);
    dialog.setResultConverter(editor.getController().getResultConverter());
    var result = dialog.showAndWait();
    result.ifPresent(item -> {
              try {
                log.debug("Saving new item: " + item);
                var savedItem = itemService.save(item);
                tableView.getItems().add(savedItem);
                tableView.getSelectionModel().selectItem(savedItem);
                tableView.scrollTo(tableView.getItems().indexOf(savedItem));
              } catch (CrudException ex) {
                log.error(ex.getMessage());
              }
            });
    if (result.isEmpty()) {
      log.debug("Dialog result is empty");
    }
  }

  /**
   * Save all items from the changed item list. At the end removes them from the list.
   * <p>
   * Open alert dialog.
   * </p>
   *
   * @param event Action event
   */
  @FXML
  public void onSaveAll(ActionEvent event) {
    log.debug("Save all action occurred");

    Alert alert = new Alert(AlertType.CONFIRMATION, "Saving all items");
    alert.showAndWait()
        .filter(ButtonType.OK::equals)
        .ifPresent(button -> {
          List<T> removedList = new ArrayList<>();
          changedItemList.forEach(item -> {
            try {
              itemService.save(item);
              removedList.add(item);
            } catch (CrudException ex) {
              log.error(ex.getMessage());
            }
          });
          changedItemList.removeAll(removedList);
    });
  }

  /**
   * Open alert dialog and delete selected item.
   *
   * @param event Action event
   */
  @FXML
  public void onDelete(ActionEvent event) {
    log.debug("Delete action occurred");

    Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure to delete selected item?");
    alert.showAndWait()
        .filter(ButtonType.OK::equals)
        .ifPresent(unused -> {
          try {
            T item = tableView.getSelectionModel().getSelectedValue();
            tableView.getItems().remove(item);
            itemService.delete(item);
            changedItemList.remove(item);
          } catch (CrudException ex) {
            log.error(ex.getMessage());
          }
    });
  }

  /**
   * Initialize FX controller.
   * <p>
   * Load items from item service and set in table view.
   * </p>
   */
  public final void initialize() {
    log.debug("Initialize");

    init();
    var newList = FXCollections.<T>observableArrayList();
    try {
      newList.addAll(itemService.list());
    } catch (CrudException ex) {
      // TODO: Inform user about an error somehow
      log.error(ex.getMessage());
    }
    tableView.setItems(newList);
  }

  /**
   * Add table view columns.
   *
   * @param columns Columns to add
   */
  @SafeVarargs
  public final void addColumns(MFXTableColumn<T>... columns) {
    log.trace("Add table view columns: {}", (Object[]) columns);

    tableView.getTableColumns().addAll(columns);
  }

  /**
   * Set the detail controller.
   *
   * @param <E> Detail controller type
   * @param detailController Controller to set
   */
  public <E extends AbstractDetailController<T>> void setDetailController(Class<E> detailController) {
    FxControllerAndView<E, Node> detailView = fxWeaver.load(detailController);

    detailView.getController().setChangedItemList(this.changedItemList);

    borderPane.setCenter(detailView.getView().orElseThrow(
        () -> new RuntimeException("There is no view defined for controller " + detailController))
    );

    tableView.getSelectionModel()
        .selectionProperty()
        .addListener(
            (MapChangeListener<? super Integer, ? super T>) (change) -> detailView.getController().setItem(change.wasAdded() ? change.getValueAdded() : null)
        );
  }

  /**
   * Sets the row factory
   *
   * @param styleClass Name of the style class to used in row factory
   */
  protected void setRowFactory(String styleClass) {
    tableView.setTableRowFactory(new MFXStyleChangingRowFactory<>(tableView, changedItemList, styleClass));
  }

  /**
   * Delegate titles and columns initialization to subclass.
   */
  protected abstract void init();

}
