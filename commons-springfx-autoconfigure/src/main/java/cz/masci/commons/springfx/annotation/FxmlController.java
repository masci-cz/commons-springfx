package cz.masci.commons.springfx.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotated class is defined as Fxml Controller and is used for loading fxml as the controller.
 * 
 * @author Daniel Mašek
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FxmlController {
  
}
