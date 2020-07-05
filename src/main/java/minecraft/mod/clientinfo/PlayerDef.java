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

  /**
   * Referencia dos itens que o player tem
   */
  public static final FieldId INVENTORY = FieldId.of("inventory");

  public static final FieldId XP = FieldId.of("experienceLevel");

  /**
   * Retorna os items da mao esquerda e direita do player
   */
  public static final MethodId GET_HANDS_SLOTS = MethodId.of(PLAYER, "getHandSlots");

  /**
   * Versão desta instância do player, a maior é a atual, as menores são loads anteriores
   */
  public static final FieldId LAST_LEVEL_UP_TIME = FieldId.of("lastLevelUpTime");

  /**
   * Nome do player
   */
  public static final MethodId NAME = MethodId.of(PLAYER, "getName");


  ClassId classId;
  FieldId xp;
  MethodId getHandSlots;
  FieldId inventory;

  InventoryDef inventoryDef;

  public static PlayerDef of(ClassMappingsListener mappingsListener) {
    return PlayerDef
        .builder()
        .classId(mappingsListener.findObfuscatedClassName(PLAYER))
        .xp(mappingsListener.findObfuscatedField(PLAYER, XP))
        .getHandSlots(mappingsListener.findObfuscatedMethod(GET_HANDS_SLOTS))
        .inventory(mappingsListener.findObfuscatedField(PLAYER, INVENTORY))
        .inventoryDef(InventoryDef.of(mappingsListener))
        .build();
  }
}
