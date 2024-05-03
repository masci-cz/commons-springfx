# commons-springfx-mvci

This module contains classes related to MVCI design pattern I found more flexible and productive than FXML.  
More info about this pattern could be found on [pragmaticcoding](https://www.pragmaticcoding.ca/javafx/mvci/)

## Package CONTROLLER

`ViewProvider` is used as an interface for controllers to specify that controller returns view.  
`SimpleController` is a simple implementation of a controller returning a view. It implements the `ViewProvider` interface and
accepts view builder which builds the view.

## Package MODEL

### Dirty

Dirty properties are new implementation of [DirtyFX](https://github.com/thomasnield/DirtyFX) because of an issue [unbindBidirectional adds the listener instead of removing it](https://github.com/thomasnield/DirtyFX/issues/3)  
As it is described id the **DirtyFX** readme these properties tracks and manages its *dirty* state.

### List

There are two interface types.
* `Focusable` used for focus view when the element is selected.
* `Selectable` used for select element from list, get all elements, run action when element is selected and returns selected element property for binding possibility.
* `Removable` used for remove element from list and run action when element is removed.
* `Updateable` used for update element in list and run action when element is updated.
  
* `ListModel` combines `Selectable`, `Updatable` and `Removable` interfaces used as view model for list in the mvci pattern.
* `SimpleListModel` is simple implementation of dirty list model with all `Selectable`, `Updatable`, `Removable` and `Focusable` interfaces.

### Detail

* `IdentifiableModel` used for object with id or without when it is in transient (not saved yet) state.
* `ValidModel` extends [validated](https://github.com/palexdev/MaterialFX/blob/main/materialfx/src/main/java/io/github/palexdev/materialfx/validation/Validated.java) to simplify get the valid property.
* `DirtyModel` wraps the dirty property for composite object. Change in any property in the object will update the dirty property.
  
* `DetailModel` combines `IdentifiableModel`, `ValidModel` and `DirtyModel` interfaces used as view model for detail in the mvci pattern.

## Package VIEW

* `BorderPaneBuilder` builds the BorderPane with set regions and padding.
* `ButtonBuilder` builds the button with command to be run on act. Button class is resolved by the supplier in the build method. Two command types could be set with expression when the button should be disabled.
  * `command(Consumer<Runnable> appThreadCommand)` run in application thread waiting for the end of the process and then enable button.
  * `command(Runnable fxThreadCommand)` run in fx thread enable the button at the end.
  * `disableExpression` combines internal disable state (when clicked on) and this expression to calculate button disable state.
* `CommandsViewBuilder` builds horizontal representation of provided buttons aligned with provided alignment with implicit CENTER_LEFT alignment if not provided.


* `DirtyStyleable` interface extending `Styleable` enabling add and remove provided dirty style class to property based on the dirty flag from `DirtyProperty`
* `DirtyMFXTableRow` MFX table row with dirty style implementation.
* `DirtyJFXTableRow` JFX table row with dirty style implementation.

## Package UTIL

* `BindingUtils` utils for bidirectional bindings when one of properties could be null. (Considering to remove because I use now [ReactFX](https://github.com/TomasMikula/ReactFX) library for this)
* `BuilderUtils` utils for enhancing node with supporting text for not valid node based on `Validated` class.
* `FunctionUtils` utils transforming object only when is not null.
* `ListChangeListenerBuilder` simplify `ListChangeListener` class with actions on added, removed, permuted and updated items.
