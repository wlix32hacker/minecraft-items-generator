package minecraft.mod.classmapping;

import com.mageddo.ramspiderjava.MethodId;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class MethodMapping {
  MethodId vanilla;
  MethodId obfuscated;
}
