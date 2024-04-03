/*
 * Copyright (c) 2024
 *
 * This file is part of DrD.
 *
 * DrD is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option)
 *   any later version.
 *
 * DrD is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *   FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 *    License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *  along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */

package cz.masci.springfx.mvci.model.list;

import java.util.function.Consumer;
import javafx.collections.ObservableList;
import org.reactfx.value.Var;

/**
 * <p>
 *   Ability to select element in elements observable list.
 *   First set {@code setOnSelectElement} which will be used when calling {@code selectElement}.
 *   {@code selectElement} explicitly selects an element in the list view from list-detail view pattern.
 *   {@code selectedItemProperty} should be bound to selected element in list view from list-detail view pattern.
 * </p>
 *
 * <pre>{@code
 *   class SelectableImpl implements Selectable<Model> {
 *     protected final ListProperty<Model> elements = FXCollections.arrayList();
 *     protected final Var<Model> selectedElement = Var.newSimpleVar(null);
 *     protected Consumer<Model> onSelectElement;
 *
 *     public ObservableList<Model> getElements() {
 *       return elements.get();
 *     }
 *
 *     public Var<Model> selectedElementProperty() {
 *       return selectedElement;
 *     }
 *
 *     public void setOnSelectElement(Consumer<Model> command) {
 *       onSelectElement = command;
 *     }
 *
 *     public void selectElement(Model element) {
 *       if (onSelectElement != null) {
 *         onSelectElement.accept(element);
 *       }
 *     }
 * }</pre>
 */
public interface Selectable<E> {
  /** Returns elements */
  ObservableList<E> getElements();
  /** Returns selected element property */
  Var<E> selectedElementProperty();
  /** Runs the predefined command set by {@link Selectable#setOnSelectElement}*/
  void selectElement(E element);
  /** Sets the command which should be run in {@link Selectable#selectElement}*/
  void setOnSelectElement(Consumer<E> command);
}
