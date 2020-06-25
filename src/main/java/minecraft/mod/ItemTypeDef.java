package minecraft.mod;

import com.mageddo.ramspiderjava.ClassId;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ItemTypeDef {
  ClassId classId;
}
