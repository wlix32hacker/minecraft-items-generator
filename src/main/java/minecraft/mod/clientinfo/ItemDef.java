package minecraft.mod.clientinfo;

import com.mageddo.ramspiderjava.ClassId;
import com.mageddo.ramspiderjava.FieldId;
import com.mageddo.ramspiderjava.MethodId;

import lombok.Builder;
import lombok.Value;
import minecraft.mod.classmapping.ClassMappingsListener;

@Value
@Builder
public class ItemDef {

  public static final ClassId ITEM = ClassId.of("net.minecraft.world.item.ItemStack");
  public static final FieldId QUANTITY = FieldId.of("count");
  public static final FieldId ITEM_TYPE = FieldId.of("item");
  public static final MethodId GET_BASE_REPAIR_COST = MethodId.of(
      ITEM,
      "getBaseRepairCost"
  );
  public static final MethodId SET_REPAIR_COST = MethodId.of(
      ITEM,
      "setRepairCost",
      ClassId.of(int.class)
  );

  ClassId classId;
  FieldId quantityField;
  FieldId itemTypeField;
  FieldId tag;
  MethodId getBaseRepairCost;
  MethodId setRepairCost;

  public static ItemDef of(ClassMappingsListener mappingsListener) {
    return ItemDef
        .builder()
        .classId(mappingsListener.findObfuscatedClassName(ITEM))
        .quantityField(mappingsListener.findObfuscatedField(ITEM, QUANTITY))
        .itemTypeField(mappingsListener.findObfuscatedField(ITEM, ITEM_TYPE))
        .getBaseRepairCost(mappingsListener.findObfuscatedMethod(GET_BASE_REPAIR_COST))
        .setRepairCost(mappingsListener.findObfuscatedMethod(SET_REPAIR_COST))
        .build()
        ;
  }

}
