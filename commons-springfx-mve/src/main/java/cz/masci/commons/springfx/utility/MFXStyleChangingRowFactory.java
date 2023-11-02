package cz.masci.commons.springfx.utility;

import cz.masci.commons.springfx.data.Modifiable;
import io.github.palexdev.materialfx.controls.MFXTableRow;
import io.github.palexdev.materialfx.controls.MFXTableView;
import java.util.Collections;
import java.util.function.Function;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import lombok.RequiredArgsConstructor;

/**
 * Row factory for items in observable list. Add style class to the row if
 * the item is found in the list.
 *
 * @author Daniel Masek
 * @param <T> Type of table item
 */
@RequiredArgsConstructor
public class MFXStyleChangingRowFactory<T extends Modifiable> implements Function<T, MFXTableRow<T>> {

  private final MFXTableView<T> tableView;
  private final ObservableList<T> selectionList;
  private final String styleClass;

  /**
   * Add listener on table item to an observable list to check the item existence
   * and therefor style changing.
   *
   * @param data Data to add
   * @return Table row
   */
  @Override
  public MFXTableRow<T> apply(T data) {
    final MFXTableRow<T> row = new MFXTableRow<>(tableView, data);

    // listen to row selection in the table view
    row.dataProperty().addListener((obs, oldValue, newValue) -> updateStyleClass(row));

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
  private ListChangeListener<T> createListChangeListener(MFXTableRow<T> row) {
    return (change -> updateStyleClass(row));
  }

  /**
   * Add or remove style class from row style classes based on existence of
   * selected item in the selection list.
   *
   * @param row Selected table row
   */
  private void updateStyleClass(MFXTableRow<T> row) {
    final ObservableList<String> rowStyleClasses = row.getStyleClass();
    if (selectionList.contains(row.getData())) {
      if (!rowStyleClasses.contains(styleClass)) {
        rowStyleClasses.add(styleClass);
      }
    } else {
      // remove all occurrences of styleClass:
      rowStyleClasses.removeAll(Collections.singleton(styleClass));
    }
  }

}
