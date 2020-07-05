package minecraft.mod;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mageddo.ramspiderjava.InstanceId;
import com.mageddo.ramspiderjava.InstanceValue;

import org.apache.commons.lang3.Validate;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(of = "mapName")
public class Player {

  InstanceValue value;
  String mapName;
  int xp;

  public static Player from(InstanceValue value, int xp){
    final Matcher matcher = Pattern
        .compile("ServerLevel\\[([^\\]]+)\\]")
        .matcher(value.getValue());
    Validate.isTrue( matcher.find(), "Couldn't find map name: %s", value.getValue());
    return new Player(value, matcher.group(1), xp);
  }

  public InstanceId id(){
    return this.value.getId();
  }
}
