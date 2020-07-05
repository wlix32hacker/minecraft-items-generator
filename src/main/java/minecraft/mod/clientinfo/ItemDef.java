package minecraft.mod.clientinfo;

import com.mageddo.ramspiderjava.ClassId;
import com.mageddo.ramspiderjava.FieldId;
import com.mageddo.ramspiderjava.MethodId;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ItemDef {
  ClassId classId;
  FieldId quantityField;
  FieldId itemTypeField;
  FieldId tag;
  MethodId getBaseRepairCost;
  MethodId setRepairCost;
//      853:856:int getBaseRepairCost() -> B
//    860:861:void setRepairCost(int) -> c
}
