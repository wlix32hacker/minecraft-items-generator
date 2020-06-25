package minecraft.mod;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.mageddo.ramspiderjava.ClassInstanceService;
import com.mageddo.ramspiderjava.InstanceValue;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class MinecraftItemScanner {

  private final ClassInstanceService classInstanceService;

  @Inject
  public MinecraftItemScanner(ClassInstanceService classInstanceService) {
    this.classInstanceService = classInstanceService;
  }

  /**
   * Get all minecraft items loaded at the game just now
   */
  public List<Item> findItems(MinecraftVersion version) {
    return this.classInstanceService
      .scanAndGetValues(version.getItemDef().getClassId())
      .stream()
      .map(Item::from)
      .filter(it -> it.getQuantity() > 0)
      .collect(Collectors.toList())
      ;
  }

  public List<Item> findItems(MinecraftVersion version, ItemType itemType, int quantity) {
    final List<Item> foundItems = this.findItems(version)
      .stream()
      .filter(it -> {
        return it.getItemType().equals(itemType.getName())
          && it.getQuantity() == quantity
          ;
      })
      .collect(Collectors.toList());
    log.debug("found={}, type={}, quantity={}", foundItems.size(), itemType.getName(), quantity);
    return foundItems;
  }

  public int findAndChange(MinecraftVersion version, ItemType itemType, int quantity, int newQuantity) {
    final List<Item> items = this.findItems(version, itemType, quantity);
    items.forEach(it -> this.changeQuantity(it, version, newQuantity));
    log.info("{} items changed", items.size());
    return items.size();
  }

  public int findAndChange(
      MinecraftVersion version,
      ItemType itemType,
      int quantity,
      ItemType newItemType, int newQuantity
  ) {
    final List<Item> items = this.findItems(version, itemType, quantity);
    items.forEach(it -> {
      try {
        log.debug("status=changing-status, it={}", it);
        this.changeQuantity(it, version, newQuantity);
        this.changeType(it, version, newItemType);
      } catch (Exception e){
        log.warn("status=can't-change-item, from={}, to={}, item={}", itemType, newItemType, it, e);
      }
    });
    log.info("{} items changed", items.size());
    return items.size();
  }

  void changeType(Item item, MinecraftVersion version, ItemType itemType) {
    this.classInstanceService.setFieldValue(
      item.getInstanceValue().getId(),
      version.getItemDef().getItemTypeField(),
      InstanceValue.of(itemType.getInstance().getId())
    );
  }

  void changeQuantity(Item item, MinecraftVersion version, int newQuantity) {
    this.classInstanceService.setFieldValue(
      item.getInstanceValue().getId(),
      version.getItemDef().getQuantityField(),
      InstanceValue.of(newQuantity)
    );
  }

  public Set<ItemType> findItemTypes(MinecraftVersion version) {
    return this.classInstanceService
      .scanAndGetValues(version.getItemTypeDef().getClassId())
      .stream()
      .map(ItemType::of)
      .collect(Collectors.toSet())
      ;
  }

  public ItemType filterType(Set<ItemType> types, String itemTypeCode) {
    return types
      .stream()
      .filter(it -> it.getName().equals(itemTypeCode))
      .findFirst()
      .get();
  }
}
