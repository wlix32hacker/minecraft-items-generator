package minecraft.mod.clientinfo;

import com.mageddo.ramspiderjava.ClassId;
import com.mageddo.ramspiderjava.FieldId;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ItemDef {
  ClassId classId;
  FieldId quantityField;
  FieldId itemTypeField;
}
