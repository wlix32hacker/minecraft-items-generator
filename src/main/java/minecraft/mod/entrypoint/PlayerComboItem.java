package minecraft.mod.entrypoint;

import lombok.Builder;
import lombok.Value;
import minecraft.mod.Player;

@Value
@Builder
public class PlayerComboItem {

  Player player;

  @Override
  public String toString() {
    return this.player.getMapName();
  }

  public static PlayerComboItem of(Player player) {
    return PlayerComboItem
        .builder()
        .player(player)
        .build()
        ;
  }
}
