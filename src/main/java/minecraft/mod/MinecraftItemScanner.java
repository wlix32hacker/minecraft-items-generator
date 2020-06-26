package minecraft.mod;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class MinecraftItemScanner {

  private final MinecraftItemManagerFactory itemManagerFactory;

  @Inject
  public MinecraftItemScanner(MinecraftItemManagerFactory itemManagerFactory) {
    this.itemManagerFactory = itemManagerFactory;
  }

  /**
   * Get all minecraft items loaded at the game just now
   */
  public List<Item> findItems() {
    final List<Item> items = this.getManager()
        .findItems();
    log.info("searching in {} items", items.size());
    return items;
  }

  public Set<ItemType> findItemTypes() {
    return this.getManager().findItemTypes();
  }

  public List<Item> findItems(ItemType itemType, int quantity) {
    final List<Item> foundItems = this.findItems()
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

  public int findAndChange(ItemType itemType, int quantity, int newQuantity) {
    final List<Item> items = this.findItems(itemType, quantity);
    items.forEach(it -> this.changeQuantity(it, newQuantity));
    log.info("{} items changed", items.size());
    return items.size();
  }

  public int findAndChange(ItemType itemType, int quantity, ItemType newItemType, int newQuantity) {
    final List<Item> items = this.findItems(itemType, quantity);
    items.forEach(it -> {
      try {
        log.debug("status=changing-status, it={}", it);
        this.changeQuantity(it, newQuantity);
        this.changeType(it, newItemType);
      } catch (Exception e){
        log.warn("status=can't-change-item, from={}, to={}, item={}", itemType, newItemType, it, e);
      }
    });
    log.info("{} items changed", items.size());
    return items.size();
  }

  public ItemType filterType(Set<ItemType> types, String itemTypeCode) {
    return types
        .stream()
        .filter(it -> it.getName().equals(itemTypeCode))
        .findFirst()
        .get();
  }

  void changeType(Item item, ItemType itemType) {
    this.getManager().changeType(item, itemType);
  }

  void changeQuantity(Item item, int newQuantity) {
    this.getManager().changeQuantity(item, newQuantity);
  }

  MinecraftItemManager getManager() {
    return this.itemManagerFactory.getInstance();
  }
}
