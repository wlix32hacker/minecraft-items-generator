package minecraft;

import com.mageddo.ramspiderjava.InstanceValue;

import lombok.Value;

@Value
public class ItemType {

  InstanceValue instance;
  String name;

  public static ItemType of(InstanceValue instanceValue) {
    return new ItemType(instanceValue, instanceValue.getValue());
  }
}
