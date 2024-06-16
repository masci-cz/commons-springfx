/*
 * Copyright (c) 2024
 *
 * This file is part of commons-springfx library.
 *
 * commons-springfx library is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option)
 *   any later version.
 *
 * commons-springfx library is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *   FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 *    License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *  along with Foobar. If not, see <https://www.gnu.org/licenses/>.
 */

package cz.masci.springfx.mvci.util.constraint;

import static org.junit.jupiter.api.Assertions.*;

import cz.masci.springfx.mvci.model.detail.ValidModel;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.MFXValidator;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.Data;
import org.junit.jupiter.api.Test;

class ConstraintUtilsTest {

  private static final String MESSAGE = "message";

  @Test
  void isValid() {
    ObjectProperty<ParentObject> parent = new SimpleObjectProperty<>();
    ParentObject parentObject = new ParentObject();
    TestObject testObject = new TestObject();

    var result = ConstraintUtils.isValid(MESSAGE, parent, ParentObject::childProperty);

    System.out.println("Initialization");
    assertNotNull(result);
    assertFalse(result.isValid());

    System.out.println("Set test object");
    parent.set(parentObject);
    assertFalse(result.isValid());

    System.out.println("Set name");
    testObject.setName("Name");
    assertFalse(result.isValid());

    System.out.println("Set age low");
    testObject.setAge(4);
    assertFalse(result.isValid());

    System.out.println("Set age high");
    testObject.setAge(16);
    assertFalse(result.isValid());

    System.out.println("Set child");
    parentObject.setChild(testObject);
    assertTrue(result.isValid());

    System.out.println("Set age low again");
    testObject.setAge(4);
    assertFalse(result.isValid());
  }

  @Data
  private static class ParentObject {
    private String name;
    private final ObjectProperty<TestObject> child = new SimpleObjectProperty<>();

    public TestObject getChild() {
      return child.get();
    }

    public ObjectProperty<TestObject> childProperty() {
      return child;
    }

    public void setChild(TestObject child) {
      this.child.set(child);
    }
  }

  private static class TestObject implements ValidModel {
    private final StringProperty name = new SimpleStringProperty();
    private final IntegerProperty age = new SimpleIntegerProperty();
    private final MFXValidator validator = new MFXValidator();

    public TestObject() {
      validator.constraint(Constraint.of(MESSAGE, name.isNotEmpty()));
      validator.constraint(Constraint.of(MESSAGE, age.greaterThan(15)));
      validator.validProperty().addListener(observable -> System.out.println("valid property was invalidated"));
    }

    public String getName() {
      return name.get();
    }

    public StringProperty nameProperty() {
      return name;
    }

    public void setName(String name) {
      this.name.set(name);
    }

    public int getAge() {
      return age.get();
    }

    public IntegerProperty ageProperty() {
      return age;
    }

    public void setAge(int age) {
      this.age.set(age);
    }

    @Override
    public MFXValidator getValidator() {
      return validator;
    }
  }
}