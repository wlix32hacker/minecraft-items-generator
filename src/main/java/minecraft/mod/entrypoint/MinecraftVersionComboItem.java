package minecraft.mod.entrypoint;

import lombok.EqualsAndHashCode;
import lombok.Value;
import minecraft.mod.VersionDefs;

@Value
@EqualsAndHashCode(of = "minecraftVersion")
public class MinecraftVersionComboItem {

  VersionDefs version;

  public static MinecraftVersionComboItem of(VersionDefs version) {
    return new MinecraftVersionComboItem(version);
  }

  @Override
  public String toString() {
    return this.version
        .name()
        .replaceAll("_", ".")
        .toLowerCase()
        ;
  }
}
