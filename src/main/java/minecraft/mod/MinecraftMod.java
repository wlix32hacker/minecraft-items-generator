package minecraft.mod;

import com.mageddo.ramspiderjava.client.JavaRamSpider;

public class MinecraftMod {

  public static void main(String[] args) {

    final MinecraftScanner minecraftScanner = JavaRamSpider.attach(0, MinecraftScanner.class);

//    final com.mageddo.jvmti.minecraft.MinecraftScanner scanner = ctx.getInstance(
//        com.mageddo.jvmti.minecraft.MinecraftScanner.class);
//    final Set<com.mageddo.jvmti.minecraft.ItemType> types = scanner.findItemTypes();
//    System.out.println("types: " + types);
//    final com.mageddo.jvmti.minecraft.ItemType currentItemType = scanner.filterType(types, "oak_log");
//    final com.mageddo.jvmti.minecraft.ItemType newItemType = scanner.filterType(types, "oak_log");
//    log.info("find={}", scanner.findItems().size());
//    scanner.findAndChange(currentItemType, 3, 512, newItemType);

//    final List<Item> items = scanner.findItems(ItemType.of("cobblestone"), 6);
//    System.out.println(ctx.getInstance(ObjectMapper.class).writeValueAsString(items));
//    System.out.println(items.size());


  }
}
