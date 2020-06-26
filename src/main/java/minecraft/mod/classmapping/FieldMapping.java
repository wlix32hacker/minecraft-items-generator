package minecraft.mod.classmapping;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FieldMapping {

  String name;
  String obfuscatedName;

  String typeName;
  String obfuscatedType;

}
