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
The main interface with base implementation is `ObservableListMap`. It stores lists of changed items
based on the mapping key. This list could be read in other services, checking which items where updated.  
Another interface is `CrudService` defining CRUD methods (list, save, delete).

## commons-springfx-mve

This package contains two abstract controllers:
* `AbstractDetailController` - uses `ObservableListMap` to add selected item to the list of changed items and display selected item attributes
* `AbstractMasterController` - uses `ObservableListMap` to highlight changed items and set selected item in detail controller