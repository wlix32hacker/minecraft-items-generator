package minecraft.mod.entrypoint;

import lombok.Value;
import minecraft.mod.Item;

@Value
public class HotBarComboItem {

  int index;
  Item item;

  @Override
  public String toString() {
    return String.format("%d: %s", index, item.getItemType());
  }
}
