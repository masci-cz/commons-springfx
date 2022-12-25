package cz.masci.commons.springfx.utility;

import cz.masci.commons.springfx.data.Modifiable;
import cz.masci.commons.springfx.service.ObservableListMap;
import java.util.Collections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 * Row factory for items in observable list map. Add style class to the row if
 * the item row is found in appropriate list.
 *
 * @author Daniel Masek
 * @param <T> Type of table item
 */
public class StyleChangingRowFactory<T extends Modifiable> implements Callback<TableView<T>, TableRow<T>> {

  private final ObservableListMap observableListMap;
  private final String styleClass;
  private final String modifiableKey;
  private final Callback<TableView<T>, TableRow<T>> baseFactory;

  /**
   * Constructor with all parameters
   *
   * @param styleClass Name of the style class
   * @param modifiableKey Modifiable key
   * @param modifiableService Observable list map
   * @param baseFactory Row base factory
   */
  public StyleChangingRowFactory(String styleClass, String modifiableKey, ObservableListMap modifiableService, Callback<TableView<T>, TableRow<T>> baseFactory) {
    this.styleClass = styleClass;
    this.modifiableKey = modifiableKey;
    this.observableListMap = modifiableService;
    this.baseFactory = baseFactory;
  }

  /**
   * Constructor with all parameters
   *
   * @param styleClass Name of the style class
   * @param modifiableKey Modifiable key
   * @param modifiableService Observable list map
   */
  public StyleChangingRowFactory(String styleClass, String modifiableKey, ObservableListMap modifiableService) {
    this(styleClass, modifiableKey, modifiableService, null);
  }

  /**
   * Constructor with all parameters
   *
   * @param styleClass Name of the style class
   * @param modifiableKey Modifiable class key
   * @param modifiableService Observable list map
   * @param baseFactory Row base factory
   */
  public StyleChangingRowFactory(String styleClass, Class<T> modifiableKey, ObservableListMap modifiableService, Callback<TableView<T>, TableRow<T>> baseFactory) {
    this(styleClass, modifiableKey.getSimpleName(), modifiableService, baseFactory);
  }

  /**
   * Constructor with all parameters
   *
   * @param styleClass Name of the style class
   * @param modifiableKey Modifiable class key
   * @param modifiableService Observable list map
   */
  public StyleChangingRowFactory(String styleClass, Class<T> modifiableKey, ObservableListMap modifiableService) {
    this(styleClass, modifiableKey, modifiableService, null);
  }

  /**
   * Add listener on table item and observable list map to check item existence
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

    row.itemProperty().addListener((obs, oldValue, newValue) -> updateStyleClass(row));

    observableListMap.addListener(modifiableKey, (change) -> updateStyleClass(row));

    return row;
  }

  /**
   * Add or remove style class from row style classes based on existence of
   * selected item in the list map.
   *
   * @param row Selected table row
   */
  private void updateStyleClass(TableRow<T> row) {
    final ObservableList<String> rowStyleClasses = row.getStyleClass();
    if (observableListMap.contains(row.getItem())) {
      if (!rowStyleClasses.contains(styleClass)) {
        rowStyleClasses.add(styleClass);
      }
    } else {
      // remove all occurrences of styleClass:
      rowStyleClasses.removeAll(Collections.singleton(styleClass));
    }
  }

}
