package minecraft.mod.clientinfo;

import com.mageddo.ramspiderjava.ClassId;
import com.mageddo.ramspiderjava.MethodId;

import lombok.Builder;
import lombok.Value;
import minecraft.mod.classmapping.ClassMappingsListener;

@Value
@Builder
public class InventoryDef {

  public static final ClassId INVENTORY = ClassId.of("net.minecraft.world.entity.player.Inventory");
  public static final MethodId GET_ITEM = MethodId.of(INVENTORY, "getItem", ClassId.of(int.class));

  ClassId classId;
  MethodId getItem;

  public static InventoryDef of(ClassMappingsListener mappingsListener) {
    return InventoryDef
        .builder()
        .classId(mappingsListener.findObfuscatedClassName(INVENTORY))
        .getItem(mappingsListener.getObfuscatedMethod(GET_ITEM))
        .build();
  }
}
