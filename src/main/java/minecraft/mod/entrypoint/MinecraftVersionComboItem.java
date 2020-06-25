package minecraft.mod.entrypoint;

import lombok.EqualsAndHashCode;
import lombok.Value;
import minecraft.mod.MinecraftVersion;

@Value
@EqualsAndHashCode(of = "minecraftVersion")
public class MinecraftVersionComboItem {

  MinecraftVersion minecraftVersion;

  public static MinecraftVersionComboItem of(MinecraftVersion minecraftVersion) {
    return new MinecraftVersionComboItem(minecraftVersion);
  }

  @Override
  public String toString() {
    return this.minecraftVersion
        .name()
        .replaceAll("_", ".")
        .toLowerCase()
        ;
  }
}
