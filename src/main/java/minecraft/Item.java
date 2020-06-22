package minecraft;

import com.mageddo.ramspiderjava.InstanceValue;

import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.Validate;

@Value
@Builder
public class Item {

  InstanceValue instanceValue;

  int quantity;

  String itemType;

  public static Item from(InstanceValue instanceValue) {
    String[] qtdAndItemType = instanceValue.getValue().split(" ");
    Validate.isTrue(qtdAndItemType.length == 2, "not two pieces : %s", instanceValue);
    return Item
      .builder()
      .instanceValue(instanceValue)
      .quantity(Integer.parseInt(qtdAndItemType[0]))
      .itemType(qtdAndItemType[1])
      .build()
      ;
  }
}
