package com.mageddo.jvmti.minecraft;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mageddo.jvmti.ExternalJvmAttach;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;

@Slf4j
public class MinecraftMod {
  @SneakyThrows
  public static void main(String[] args) {

    log.info("status=loading minecraft mod...");
    log.info("status=attaching to minecraft process, pid={}...", args[0]);
    ExternalJvmAttach.attach(args[0]);
    log.info("status=attached!!!");

    final Injector ctx = Guice.createInjector(new DependencyInjection());
    final MinecraftScanner scanner = ctx.getInstance(MinecraftScanner.class);
    final Set<ItemType> types = scanner.findItemTypes();
    System.out.println("types: " + types);
    final ItemType currentItemType = scanner.filterType(types, "oak_log");
    final ItemType newItemType = scanner.filterType(types, "oak_log");
//    log.info("find={}", scanner.findItems().size());
    scanner.findAndChange(currentItemType, 3, 512, newItemType);

//    final List<Item> items = scanner.findItems(ItemType.of("cobblestone"), 6);
//    System.out.println(ctx.getInstance(ObjectMapper.class).writeValueAsString(items));
//    System.out.println(items.size());


  }
}
