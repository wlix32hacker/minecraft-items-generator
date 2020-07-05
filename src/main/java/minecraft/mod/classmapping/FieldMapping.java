package minecraft.mod.classmapping;

import lombok.Builder;
import lombok.Value;

/**
 * Essa classe não é ncessária, a map no {@link ClassMappingsListener} deve ser de um FieldId para outro
 * como está sendo feito com os metodos
 */
@Deprecated
@Value
@Builder
public class FieldMapping {

  String name;
  String obfuscatedName;

  String typeName;
  String obfuscatedType;

}
