package minecraft.mod;

import com.mageddo.ramspiderjava.ClassId;

import com.mageddo.ramspiderjava.FieldId;

import lombok.Value;
import minecraft.mod.classmapping.ClassMappingsListener;
import minecraft.mod.classmapping.FieldMapping;

@Value
public class VersionDefs {

  GameVersion version;
  ItemDef itemDef;
  ItemTypeDef itemTypeDef;

  public static VersionDefs of(GameVersion version, ClassMappingsListener mappingsListener) {

    final String itemClassName = "net.minecraft.world.item.ItemStack";
    final FieldMapping quantity = mappingsListener.getField(itemClassName, "count");
    final FieldMapping type = mappingsListener.getField(itemClassName, "item");

    return new VersionDefs(
        version,
        ItemDef
            .builder()
            .classId(ClassId.of(quantity.getObfuscatedType()))
            .quantityField(FieldId.of(quantity.getObfuscatedName()))
            .itemTypeField(FieldId.of(type.getObfuscatedName()))
            .build(),
        ItemTypeDef
            .builder()
            .classId(ClassId.of(mappingsListener.getObfuscatedClassName(type.getTypeName())))
            .build()
    );
  }
}
