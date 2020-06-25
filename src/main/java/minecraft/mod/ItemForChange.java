package minecraft.mod;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ItemForChange {
  Item item;

  ItemType type;

  int quantity;
}
