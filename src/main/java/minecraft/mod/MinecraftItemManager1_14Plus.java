package minecraft.mod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.mageddo.ramspiderjava.ClassInstanceService;
import com.mageddo.ramspiderjava.InstanceId;
import com.mageddo.ramspiderjava.InstanceValue;
import com.mageddo.ramspiderjava.MethodId;

import lombok.extern.slf4j.Slf4j;
import minecraft.mod.clientinfo.ItemDef;
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
        .scanAndGetValues(getItemDef()
            .getClassId())
        .stream()
        .map(Item::from)
        .filter(it -> it.getQuantity() > 0)
        .collect(Collectors.toList())
        ;
  }

  @Override
  public Set<ItemType> findItemTypes() {
    return this.classInstanceService
        .scanAndGetValues(this.version.getItemTypeDef()
            .getClassId())
        .stream()
        .map(ItemType::of)
        .collect(Collectors.toSet())
        ;
  }

  @Override
  public void changeQuantity(Item item, int quantity) {
    if(item.getQuantity() == quantity){
      log.info("status=already-same-quantity, item={}", item);
      return ;
    }
    this.classInstanceService.setFieldValue(
        item.getInstanceValue()
            .getId(),
        getItemDef()
            .getQuantityField(),
        InstanceValue.of(quantity)
    );
  }

  @Override
  public void changeType(Item item, ItemType itemType) {
    if(item.getItemType().equals(itemType.getName())){
      log.info("status=already-same-type, item={}", item);
      return;
    }
    this.classInstanceService.setFieldValue(
        item.getInstanceValue()
            .getId(),
        getItemDef()
            .getItemTypeField(),
        InstanceValue.of(itemType.getInstance()
            .getId())
    );
  }

  @Override
  public void changeXP(Player player, int xp) {
    this.classInstanceService.setFieldValue(
        player.id(),
        getPlayerDef().getXp(),
        InstanceValue.of(xp)
    );
    log.info("from={}, to={}", player.getXp(), xp);
  }

  @Override
  public Set<Player> findPlayers() {
    final PlayerDef playerDef = getPlayerDef();
    final List<Player> players = this.classInstanceService
        .scanAndGetValues(playerDef.getClassId())
        .stream()
        .filter(it -> it.getValue()
            .contains("ServerLevel"))
        .map(it -> Player.from(it, this.getXp(it.getId())))
        .collect(Collectors.toList());

    final Set<Player> uniquePlayers = new LinkedHashSet<>();
    for (Player player : players) {
      if(uniquePlayers.contains(player)){
        uniquePlayers.remove(player);
      }
      uniquePlayers.add(player);
    }
    return uniquePlayers;
  }

  @Override
  public List<Item> findHotBarItems(Player player) {
    final InstanceValue inventory = this.findPlayerInventory(player);
    final MethodId inventoryGetItem = getPlayerDef()
        .getInventoryDef()
        .getGetItem();
    final List<Item> items = new ArrayList<>();
    for (int i = 0; i < 9; i++) {
      final InstanceValue item = this.classInstanceService
          .methodInvoke(
              inventory.getId(),
              inventoryGetItem.getName(),
              Collections.singletonList(InstanceValue.of(i))
          );
      items.add(
          Item
              .from(item)
              .toBuilder()
              .repairCost(this.findRepairCost(item.getId()))
              .build()
      );
    }
    return items;
  }

  @Override
  public void changeRepairCost(InstanceId itemId, int repairCost){
    final MethodId methodId = this.getItemDef().getSetRepairCost();
    this.classInstanceService.methodInvoke(
        itemId,
        methodId.getName(),
        Collections.singletonList(InstanceValue.of(repairCost))
    );
  }

  int findRepairCost(InstanceId itemId) {
    final MethodId methodId = this.getItemDef()
        .getGetBaseRepairCost();
    final InstanceValue instanceValue = this.classInstanceService.methodInvoke(
        itemId,
        methodId.getName(),
        Collections.emptyList()
    );
    return this.parseInt(instanceValue);
  }

  int getXp(InstanceId playerId) {
    return this.parseInt(this.classInstanceService.getFieldValue(playerId, this.getPlayerDef().getXp()));
  }

  int parseInt(InstanceValue value) {
    try {
      return Integer.parseInt(value.getValue());
    } catch (NumberFormatException e) {
      throw new RuntimeException(String.format("Couldn't parse to int: %s", value), e);
    }
  }

  InstanceValue findPlayerInventory(Player player) {
    return this.classInstanceService.getFieldValue(player.id(), getPlayerDef().getInventory());
  }

  ItemDef getItemDef() {
    return this.version.getItemDef();
  }

  PlayerDef getPlayerDef() {
    return this.version.getPlayerDef();
  }


}
