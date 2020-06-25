package minecraft.mod;

import com.mageddo.ramspiderjava.ClassId;

import com.mageddo.ramspiderjava.FieldId;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MinecraftVersion {

  V1_15(
      new ItemDef(ClassId.of("ben"), FieldId.of("d"), FieldId.of("f")),
      new ItemTypeDef(ClassId.of("Bei"))
  ),

  V1_16_1(
      new ItemDef(ClassId.of("bki"), FieldId.of("f"), FieldId.of("h")),
      new ItemTypeDef(ClassId.of("bke"))
  ),

  ;
  private final ItemDef itemDef;
  private final ItemTypeDef itemTypeDef;
}
