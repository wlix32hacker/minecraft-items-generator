package minecraft.mod.clientinfo;

import com.mageddo.ramspiderjava.ClassId;
import com.mageddo.ramspiderjava.FieldId;

import lombok.Builder;
import lombok.Value;
import minecraft.mod.classmapping.ClassMappingsListener;
import minecraft.mod.classmapping.FieldMapping;

@Value
@Builder
public class PlayerDef {

  ClassId classId;
  FieldId xp;

  public static PlayerDef of(ClassMappingsListener mappingsListener) {

    final String serverPlayerClassName = "net.minecraft.world.entity.player.Player";
    final FieldMapping experienceLevel = mappingsListener.getField(serverPlayerClassName, "experienceLevel");

    return PlayerDef
        .builder()
        .classId(ClassId.of(mappingsListener.toObfuscatedClassName(serverPlayerClassName)))
        .xp(FieldId.of(experienceLevel.getObfuscatedName()))
        .build();
  }
}
