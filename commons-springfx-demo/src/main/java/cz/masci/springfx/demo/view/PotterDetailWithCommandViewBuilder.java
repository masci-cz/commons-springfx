package cz.masci.springfx.demo.view;

import io.github.palexdev.materialfx.builders.layout.VBoxBuilder;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PotterDetailWithCommandViewBuilder implements Builder<Region> {

  private final Region detailView;
  private final Region commandView;

  @Override
  public Region build() {
    return VBoxBuilder.vBox()
        .setVGrow(detailView, Priority.ALWAYS)
        .addChildren(detailView, commandView)
        .getNode();
  }
}
