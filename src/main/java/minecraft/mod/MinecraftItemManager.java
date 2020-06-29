package minecraft.mod;

import java.util.List;
import java.util.Set;

public interface MinecraftItemManager {

  List<Item> findItems();

  Set<ItemType> findItemTypes();

  void changeQuantity(Item item, int quantity);

  void changeType(Item item, ItemType itemType);

  void changeXP(Player player, int xp);

  List<Player> findPlayers();
}
