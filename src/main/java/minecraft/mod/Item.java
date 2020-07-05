package minecraft.mod;

import com.mageddo.ramspiderjava.InstanceValue;

import org.apache.commons.lang3.Validate;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Item {

  InstanceValue value;

  int quantity;

  int repairCost;

  String itemType;

  public static Item from(InstanceValue instanceValue) {
    String[] qtdAndItemType = instanceValue.getValue().split(" ");
    Validate.isTrue(qtdAndItemType.length == 2, "not two pieces : %s", instanceValue);
    return Item
      .builder()
      .value(instanceValue)
      .quantity(Integer.parseInt(qtdAndItemType[0]))
      .itemType(qtdAndItemType[1])
      .build()
      ;
  }
}
