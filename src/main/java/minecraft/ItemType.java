package com.mageddo.jvmti.minecraft;

import com.mageddo.jvmti.InstanceValue;
import lombok.Value;

@Value
public class ItemType {

  InstanceValue instance;
  String name;

  public static ItemType of(InstanceValue instanceValue) {
    return new ItemType(instanceValue, instanceValue.getValue());
  }
}
