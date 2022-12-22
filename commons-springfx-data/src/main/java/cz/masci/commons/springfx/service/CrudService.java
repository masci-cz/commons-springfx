package cz.masci.commons.springfx.service;

import cz.masci.commons.springfx.data.Modifiable;
import cz.masci.commons.springfx.exception.CrudException;
import java.util.List;

/**
 * Service for CRUD operations on {@link Modifiable} children items.
 * <ul>
 *   <li>Get list of modifiable items</li>
 *   <li>Saves modified item</li>
 *   <li>Delete modified item</li>
 * </ul>
 * 
 * @author Daniel Mašek
 * 
 * @param <T> Item type
 */
public interface CrudService<T extends Modifiable> {

  /**
   * Get list of items to display in Master view table.
   *
   * @return List of items
   * @throws CrudException Exception reading data
   */
  List<T> list() throws CrudException;

  /**
   * Saves created/updated item from edit dialog or detail view.
   *
   * @param item Item to save
   * @return Saved item
   * @throws CrudException Exception saving data
   */
  T save(T item) throws CrudException;

  /**
   * Deletes item.
   *
   * @param item Item to delete
   * @throws CrudException Exception deleting data
   */
  void delete(T item) throws CrudException;
}
