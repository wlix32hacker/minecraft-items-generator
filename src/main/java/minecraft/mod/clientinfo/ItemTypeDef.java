package minecraft.mod.clientinfo;

import com.mageddo.ramspiderjava.ClassId;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ItemTypeDef {

  public static final ClassId ITEM_TYPE = ClassId.of("net.minecraft.world.item.Item");

  ClassId classId;
}
