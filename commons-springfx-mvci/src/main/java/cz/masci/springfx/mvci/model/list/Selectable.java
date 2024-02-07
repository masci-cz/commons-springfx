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
 * <pre>
 *   Ability to select element in elements observable list.
 *   First set {@code setOnSelectElement} which will be used when calling {@code selectElement}.
 *   {@code selectElement} explicitly selects an element in the list view from list-detail view pattern.
 *   {@code selectedItemProperty} should be bound to selected element in list view from list-detail view pattern.
 * </pre>
 *
 * <pre>
 *   {@code class SelectableImpl implements Selectable<Model> }{
 *     {@code protected final ListProperty<Model> elements = FXCollections.arrayList(); }
 *     {@code protected final Var<Model> selectedElement = Var.newSimpleVar(null); }
 *     {@code protected Consumer<Model> onSelectElement; }
 *
 *     {@code public ObservableList<Model> getElements() } {
 *       return elements.get();
 *     }
 *
 *     {@code public Var<Model> selectedElementProperty() } {
 *       return selectedElement;
 *     }
 *
 *     {@code public void setOnSelectElement(Consumer<Model> command) } {
 *       onSelectElement = command;
 *     }
 *
 *     public void selectElement(Model element) {
 *       if (onSelectElement != null) {
 *         onSelectElement.accept(element);
 *       }
 *     }
 * </pre>
 */
public interface Selectable<E> {
  ObservableList<E> getElements();
  Var<E> selectedElementProperty();
  void selectElement(E element);
  void setOnSelectElement(Consumer<E> command);
}
