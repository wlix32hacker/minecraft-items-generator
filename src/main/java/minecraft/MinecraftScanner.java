package com.mageddo.jvmti.minecraft;

import com.mageddo.jvmti.ClassId;
import com.mageddo.jvmti.ClassInstanceService;
import com.mageddo.jvmti.FieldId;
import com.mageddo.jvmti.InstanceValue;
import com.mageddo.jvmti.classdelegate.InstanceId;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Singleton
public class MinecraftScanner {

  private final ClassInstanceService classInstanceService;

  @Inject
  public MinecraftScanner(ClassInstanceService classInstanceService) {
    this.classInstanceService = classInstanceService;
  }

  /**
   * Get all minecraft items loaded at the game just now
   */
  public List<Item> findItems() {
    return this.classInstanceService
      .scanAndGetValues(ClassId.of("ben"))
      .stream()
      .map(Item::from)
      .filter(it -> it.getQuantity() > 0)
      .collect(Collectors.toList())
      ;
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

  public int findAndChange(ItemType itemType, int quantity, int newQuantity, ItemType newItemType) {
    final List<Item> items = this.findItems(itemType, quantity);
    items.forEach(it -> {
      log.debug("status=changing-status, it={}", it);
      this.changeQuantity(it, newQuantity);
      this.changeType(it, newItemType);
    });
    log.info("{} items changed", items.size());
    return items.size();
  }

  void changeType(Item item, ItemType itemType) {
    this.classInstanceService.setFieldValue(
      item.getInstanceValue().getId(),
      FieldId.of(item.getInstanceValue().getClassId(), "f"),
      InstanceValue.of(itemType.getInstance().getId())
    );
  }

  void changeQuantity(Item item, int newQuantity) {
    this.classInstanceService.setFieldValue(
      item.getInstanceValue().getId(),
      FieldId.of(item.getInstanceValue().getClassId(), "d"),
      InstanceValue.of(newQuantity)
    );
  }

  public Set<ItemType> findItemTypes() {
    return this.classInstanceService
      .scanAndGetValues(ClassId.of("bei"))
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
