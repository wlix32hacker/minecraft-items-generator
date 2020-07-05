package minecraft.mod.clientinfo;

import com.mageddo.ramspiderjava.ClassId;
import com.mageddo.ramspiderjava.FieldId;
import com.mageddo.ramspiderjava.MethodId;

import lombok.Builder;
import lombok.Value;
import minecraft.mod.classmapping.ClassMappingsListener;

@Value
@Builder
public class PlayerDef {

  public static final ClassId PLAYER = ClassId.of("net.minecraft.world.entity.player.Player");
  public static final FieldId INVENTORY = FieldId.of("inventory");
  public static final FieldId XP = FieldId.of("experienceLevel");
  public static final MethodId GET_HANDS_SLOTS = MethodId.of(PLAYER, "getHandSlots");

  ClassId classId;
  FieldId xp;
  MethodId getHandSlots;
  FieldId inventory;
  InventoryDef inventoryDef;

  public static PlayerDef of(ClassMappingsListener mappingsListener) {
    return PlayerDef
        .builder()
        .classId(mappingsListener.findObfuscatedClassName(PLAYER))
        .xp(mappingsListener.getObfuscatedField(PLAYER, XP))
        .getHandSlots(mappingsListener.getObfuscatedMethod(GET_HANDS_SLOTS))
        .inventory(mappingsListener.getObfuscatedField(PLAYER, INVENTORY))
        .inventoryDef(InventoryDef.of(mappingsListener))
        .build();
  }
}
