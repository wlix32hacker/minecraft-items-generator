package minecraft.mod.clientinfo;

import com.mageddo.ramspiderjava.ClassId;

import lombok.Value;
import minecraft.mod.classmapping.ClassMappingsListener;
import minecraft.mod.classmapping.FieldMapping;

@Value
public class VersionDefs {

  GameVersion version;

  ClassMappingsListener mappingsListener;

  ItemDef itemDef;

  ItemTypeDef itemTypeDef;

  PlayerDef playerDef;

  public static VersionDefs of(GameVersion version, ClassMappingsListener mappingsListener) {

    final String itemClassName = "net.minecraft.world.item.ItemStack";
    final FieldMapping itemType = mappingsListener.getField(itemClassName, "item");

    return new VersionDefs(
        version,
        mappingsListener,
        ItemDef.of(mappingsListener),
        ItemTypeDef
            .builder()
            .classId(ClassId.of(mappingsListener.toObfuscatedClassName(itemType.getTypeName())))
            .build(),
        PlayerDef.of(mappingsListener)
    );
  }
}
