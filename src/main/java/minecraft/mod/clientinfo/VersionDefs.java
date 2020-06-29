package minecraft.mod.clientinfo;

import com.mageddo.ramspiderjava.ClassId;

import com.mageddo.ramspiderjava.FieldId;

import lombok.Value;
import minecraft.mod.classmapping.ClassMappingsListener;
import minecraft.mod.classmapping.FieldMapping;

@Value
public class VersionDefs {

  GameVersion version;

  ClassMappingsListener mappingsListener;

  ItemDef itemDef;

  ItemTypeDef itemTypeDef;

  public static VersionDefs of(GameVersion version, ClassMappingsListener mappingsListener) {

    final String itemClassName = "net.minecraft.world.item.ItemStack";
    final FieldMapping itemQuantity = mappingsListener.getField(itemClassName, "count");
    final FieldMapping itemType = mappingsListener.getField(itemClassName, "item");

    return new VersionDefs(
        version,
        mappingsListener,
        ItemDef
            .builder()
            .classId(ClassId.of(mappingsListener.toObfuscatedClassName(itemClassName)))
            .quantityField(FieldId.of(itemQuantity.getObfuscatedName()))
            .itemTypeField(FieldId.of(itemType.getObfuscatedName()))
            .build(),
        ItemTypeDef
            .builder()
            .classId(ClassId.of(mappingsListener.toObfuscatedClassName(itemType.getTypeName())))
            .build()
    );
  }
}
