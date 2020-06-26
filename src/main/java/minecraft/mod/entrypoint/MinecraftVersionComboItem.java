package minecraft.mod.entrypoint;

import lombok.EqualsAndHashCode;
import lombok.Value;
import minecraft.mod.clientinfo.VersionDefs;

@Value
@EqualsAndHashCode(of = "version")
public class MinecraftVersionComboItem {

  VersionDefs version;

  public static MinecraftVersionComboItem of(VersionDefs version) {
    return new MinecraftVersionComboItem(version);
  }

  @Override
  public String toString() {
    return this.version
        .getVersion()
        .getName();
  }
}
