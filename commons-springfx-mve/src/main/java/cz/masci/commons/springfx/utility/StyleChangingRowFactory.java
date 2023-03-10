package cz.masci.commons.springfx.utility;

import cz.masci.commons.springfx.data.Modifiable;
import java.util.Collections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 * Row factory for items in observable list. Add style class to the row if
 * the item is found in the list.
 *
 * @author Daniel Masek
 * @param <T> Type of table item
 */
public class StyleChangingRowFactory<T extends Modifiable> implements Callback<TableView<T>, TableRow<T>> {

  private final ObservableList<T> selectionList;
  private final String styleClass;
  private final Callback<TableView<T>, TableRow<T>> baseFactory;

  /**
   * Constructor with all parameters
   *
   * @param styleClass Name of the style class
   * @param selectionList Observable list of selected items
   * @param baseFactory Row base factory
   */
  public StyleChangingRowFactory(String styleClass, ObservableList<T> selectionList, Callback<TableView<T>, TableRow<T>> baseFactory) {
    this.styleClass = styleClass;
    this.selectionList = selectionList;
    this.baseFactory = baseFactory;
  }

  /**
   * Constructor with all parameters
   *
   * @param styleClass Name of the style class
   * @param selectionList Observable list of selected items
   */
  public StyleChangingRowFactory(String styleClass, ObservableList<T> selectionList) {
    this(styleClass, selectionList, null);
  }

  /**
   * Add listener on table item to an observable list to check the item existence
   * and therefor style changing.
   *
   * @param tableView Table view to check
   * @return Table row
   */
  @Override
  public TableRow<T> call(TableView<T> tableView) {

    final TableRow<T> row;
    if (baseFactory == null) {
      row = new TableRow<>();
    } else {
      row = baseFactory.call(tableView);
    }

    // listen to row selection in the table view
    row.itemProperty().addListener((obs, oldValue, newValue) -> updateStyleClass(row));

    // listen to adding item to the selection list
    selectionList.addListener(createListChangeListener(row));

    return row;
  }

  /**
   * Listen to change in selection list.
   *
   * @param row Selected row
   * @return list change listener
   */
  private ListChangeListener<T> createListChangeListener(TableRow<T> row) {
    return (change -> updateStyleClass(row));
  }

  /**
   * Add or remove style class from row style classes based on existence of
   * selected item in the selection list.
   *
   * @param row Selected table row
   */
  private void updateStyleClass(TableRow<T> row) {
    final ObservableList<String> rowStyleClasses = row.getStyleClass();
    if (selectionList.contains(row.getItem())) {
      if (!rowStyleClasses.contains(styleClass)) {
        rowStyleClasses.add(styleClass);
      }
    } else {
      // remove all occurrences of styleClass:
      rowStyleClasses.removeAll(Collections.singleton(styleClass));
    }
  }

}
