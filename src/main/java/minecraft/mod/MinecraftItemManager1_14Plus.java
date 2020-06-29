package minecraft.mod;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.mageddo.ramspiderjava.ClassInstanceService;
import com.mageddo.ramspiderjava.InstanceValue;

import lombok.extern.slf4j.Slf4j;
import minecraft.mod.clientinfo.PlayerDef;
import minecraft.mod.clientinfo.VersionDefs;

@Slf4j
@Singleton
public class MinecraftItemManager1_14Plus implements MinecraftItemManager {

  private final ClassInstanceService classInstanceService;
  private final VersionDefs version;

  @Inject
  public MinecraftItemManager1_14Plus(ClassInstanceService classInstanceService, VersionDefs version) {
    this.classInstanceService = classInstanceService;
    this.version = version;
  }

  /**
   * Get all minecraft items loaded at the game just now
   */
  @Override
  public List<Item> findItems() {
    return this.classInstanceService
        .scanAndGetValues(version.getItemDef().getClassId())
        .stream()
        .map(Item::from)
        .filter(it -> it.getQuantity() > 0)
        .collect(Collectors.toList())
        ;
  }

  @Override
  public Set<ItemType> findItemTypes() {
    return this.classInstanceService
        .scanAndGetValues(version.getItemTypeDef().getClassId())
        .stream()
        .map(ItemType::of)
        .collect(Collectors.toSet())
        ;
  }

  @Override
  public void changeQuantity(Item item, int quantity) {
    this.classInstanceService.setFieldValue(
        item.getInstanceValue().getId(),
        this.version.getItemDef().getQuantityField(),
        InstanceValue.of(quantity)
    );
  }

  @Override
  public void changeType(Item item, ItemType itemType) {
    this.classInstanceService.setFieldValue(
        item.getInstanceValue().getId(),
        this.version.getItemDef().getItemTypeField(),
        InstanceValue.of(itemType.getInstance().getId())
    );
  }

  @Override
  public void changeXP(Player player, int xp) {
    final PlayerDef playerDef = this.playerDef();
    this.classInstanceService.setFieldValue(player.id(), playerDef.getXp(), InstanceValue.of(xp));
    log.info("from={}, to={}", player.getXp(), xp);
  }

  @Override
  public List<Player> findPlayers(){
    final PlayerDef playerDef = this.playerDef();
    return this.classInstanceService
        .scanAndGetValues(playerDef.getClassId())
        .stream()
        .map(it -> {
          final InstanceValue xp = this.classInstanceService.getFieldValue(it.getId(), playerDef.getXp());
          return Player.from(it, Integer.parseInt(xp.getValue()));
        })
        .collect(Collectors.toList());
  }

  private PlayerDef playerDef() {
    return PlayerDef.of(this.version.getMappingsListener());
  }
}
