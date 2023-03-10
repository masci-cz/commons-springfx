package cz.masci.commons.springfx;

import cz.masci.commons.springfx.data.Modifiable;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemOne implements Modifiable {
  private String message;
}
