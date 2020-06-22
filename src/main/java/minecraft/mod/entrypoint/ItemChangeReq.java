package minecraft.mod.entrypoint;

import lombok.Builder;
import lombok.Value;
import minecraft.mod.ItemType;

@Value
@Builder
public class ItemChangeReq {
  ItemType itemType;
  int quantity;
}
