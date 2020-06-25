package minecraft.mod.entrypoint;

import lombok.EqualsAndHashCode;
import lombok.Value;
import minecraft.mod.Version;

@Value
@EqualsAndHashCode(of = "minecraftVersion")
public class MinecraftVersionComboItem {

  Version version;

  public static MinecraftVersionComboItem of(Version version) {
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
