# Commons SpringFX
Libraries for commons spring related to JavaFX

## commons-springfx-bom

BOM package contains all needed package versions for commons-springfx-* packages.

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>cz.masci.commons</groupId>
            <artifactId>commons-springfx-bom</artifactId>
            <version>0.0.12</version>
            <scope>import</scope>
            <type>pom</type>
        </dependency>
    </dependencies>
</dependencyManagement>
```

## commons-springfx-fxml

This package extends FxViewer with its own spring bean factory.  
Adding two new annotations.

* `@FxmlController`
* `@FxmlRoot` (combination of `@FxmlView` and `@FxmlController`)

Based on `@FxmlView` class annotation loads the fxml file.  
Based on `@FxmlController` class annotation sets explicitly the fxml controller class.  
Based on `@FxmlRoot` class annotation sets explicitly the fxml root class. The root class is 
used when `&lt;fx:root&gt;` is used in the fxml as custom component. 

## commons-springfx-data

This package contains support interfaces for fxml controllers.  
All items has to implement `Modifiable` interface.
The main interface is `CrudService` defining CRUD methods (list, save, delete).
Second one is `EditDialogControllerService` with method which converts dialog result `ButtonType` do item class.

## commons-springfx-mve

This package contains three abstract controllers:
* `AbstractMasterController<T>` - lists data items and highlights changed items and set selected item in the detail controller
  * Default view contains button for `New`, `Save all`, `Delete` actions
* `AbstractMFXMasterController<T>` - lists data items and highlights changed items and set selected item in the detail controller
    * Default view contains button for `New`, `Save all`, `Delete` actions
    * This controller is designed with Material Design library
    * The buttons are preset with these style classes
      * New - `filled`
      * Save all - `filledTonal`
      * Delete - `filledTonal`
    * Whole view is styled with class `masterView`
* `AbstractDetailController<T>` - displays selected item in the master controller and updates list of changed items whenever one of attribute is updated

### AbstractMasterController usage

```java
import javax.swing.table.TableColumn;

@Component
@Slf4j
@FxmlController
public class AdventureController extends AbstractMasterController<AdventureDTO> {

  public AdventureController(FxWeaver fxWeaver, CrudService<AdventureDTO> itemService) {
    super(fxWeaver, itemService, AdventureDetailDialogController.class);
  }

  @Override
  protected void init() {
    TableColumn<AdventureDTO, String> name = new TableColumn<>("Název");
    name.setPrefWidth(250);
    name.setCellValueFactory(new PropertyValueFactory<>("name"));
    // Adds columns to the list view
    addColumns(name);
    // Sets editor part with item attributes to view or update
    setDetailController(AdventureDetailEditorController.class);
    // Sets the row factory class to distinguish updated items in the master view
    setRowFactory("edited-row");
  }
}
```

```java
@Component
@Slf4j
@FxmlController
public class AdventureController extends AbstractMFXMasterController<AdventureDTO> {

  public AdventureController(FxWeaver fxWeaver, CrudService<AdventureDTO> itemService) {
    super(fxWeaver, itemService, AdventureDetailDialogController.class);
  }
    
  @Override
  protected void init() {
    MFXTableColumn<AdventureDTO> name = new MFXTableColumn<>("Název");
    name.setPrefWidth(250);
    name.setRowCellFactory(new MFXTableRowCellFactory<>(AdventureDTO::getName));

    // Adds columns to the list view
    addColumns(name);
    // Sets editor part with item attributes to view or update
    setDetailController(AdventureDetailEditorController.class);
    // Sets the row factory class to distinguish updated items in the master view
    setRowFactory("edited-row");
  }

}
```
### AbstractDetailController usage

```java
@Component
@Slf4j
@FxmlView("fxml/adventure-detail-editor.fxml")
@FxmlController
public class AdventureDetailEditorController extends AbstractDetailController<AdventureDTO> {

  @FXML
  private AdventureDetailControl editor;
  
  @Override
  protected List<ObservableValue<String>> initObservableValues() {
    // return list of properties where the change enables save ability and mark the item in the master view
    return List.of(
        editor.nameProperty()
    );
  }

  @Override
  protected void fillInputs(AdventureDTO item) {
    // fills components with values from selected item 
    if (item == null) {
      editor.setName("");
    } else {
      editor.setName(item.getName());
    }
  }

  @Override
  protected void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
    // sets the appropriate item value from the input component
    if (editor.nameProperty().equals(observable)) {
      getItem().setName(newValue);
    }
  }

}

```

## commons-springfx-mvci

This module contains classes related to MVCI design pattern I found more flexible and productive than FXML.  
More info about this pattern could be found on [pragmaticcoding](https://www.pragmaticcoding.ca/javafx/mvci/).  
This should be replacement of commons-springfx-mve. More info on [readme](https://github.com/masci-cz/commons-springfx/tree/master/commons-springfx-mvci/src/README.md).