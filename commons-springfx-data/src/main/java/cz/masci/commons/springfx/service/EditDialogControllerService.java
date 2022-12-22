package cz.masci.commons.springfx.service;

import cz.masci.commons.springfx.data.Modifiable;
import javafx.scene.control.ButtonType;
import javafx.util.Callback;


/**
 * This service is used by Master-Detail View Controllers. It is used to convert
 * javaFx dialog button type to edited/created item.
 * 
 * @author Daniel Mašek
 * 
 * @param <T> Edited item type
 */
public interface EditDialogControllerService<T extends Modifiable> {
  
  /**
   * Returns converter from {@link javafx.scene.control.ButtonType} to edited item type.
   * 
   * @return ButtonType to edited item Converter 
   */
  Callback<ButtonType, T> getResultConverter();
}
