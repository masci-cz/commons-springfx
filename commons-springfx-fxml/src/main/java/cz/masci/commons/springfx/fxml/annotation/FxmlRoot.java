package cz.masci.commons.springfx.fxml.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.core.annotation.AliasFor;

/**
 * Annotated class is defined as root fxml tag also with controller.
 * It is used as root and controller when loading from fxml.
 * 
 * @author Daniel Mašek
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@FxmlController
@FxmlView
public @interface FxmlRoot {

  @AliasFor(annotation = FxmlView.class, attribute = "value")
  String value() default "";
}
