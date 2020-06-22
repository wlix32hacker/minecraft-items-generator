package minecraft.mod.entrypoint;

import lombok.EqualsAndHashCode;
import lombok.Value;
import minecraft.mod.ItemType;

import java.util.Objects;

@Value
public class ItemTypeComboItem {

  ItemType itemType;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ItemTypeComboItem that = (ItemTypeComboItem) o;
    return this.itemType.getName().equals(that.itemType.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.itemType.getName());
  }

  @Override
  public String toString() {
    return this.getItemType().getName();
  }

  public static ItemTypeComboItem of(ItemType itemType){
    return new ItemTypeComboItem(itemType);
  }
}
